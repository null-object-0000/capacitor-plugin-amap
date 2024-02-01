package site.snewbie.plugins.amap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;

@RequiresApi(api = Build.VERSION_CODES.R)
@CapacitorPlugin(name = "CapacitorAMap", permissions = {
        @Permission(
                strings = {
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                        Manifest.permission.FOREGROUND_SERVICE,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                },
                alias = "location")
})
public class CapacitorAMapPlugin extends Plugin {
    private final Map<String, CapacitorAMap> maps = new HashMap<>();
    private final Map<String, MutableList<MotionEvent>> cachedTouchEvents = new HashMap<>();

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public void load() {
        super.load();

        // 禁用 WebView 的点击事件拦截
        super.bridge.getWebView().setOnTouchListener((v, event) -> {
            // 不知道为啥这样写，参考的 capacitor-plugins-google-maps
            // https://github.com/ionic-team/capacitor-plugins/blob/main/google-maps/android/src/main/java/com/capacitorjs/plugins/googlemaps/CapacitorGoogleMapsPlugin.kt#L50
            if (event == null || event.getSource() == -1) {
                return v == null || v.onTouchEvent(event);
            }

            for (CapacitorAMap map : maps.values()) {
                if (BooleanUtil.isFalse(map.isTouchEnabled())) {
                    continue;
                }

                float touchX = event.getX();
                float touchY = event.getY();

                Rect mapRect = map.getMapBounds();
                if (mapRect.contains((int) touchX, (int) touchY)) {
                    MutableList<MotionEvent> events = cachedTouchEvents.get(map.getId());
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (events == null) {
                            events = new MutableList<>();
                            cachedTouchEvents.put(map.getId(), events);
                        }

                        events.clear();
                    }

                    MotionEvent motionEvent = MotionEvent.obtain(event);
                    if (events != null) {
                        events.add(motionEvent);
                    }

                    JSObject payload = new JSObject();
                    payload.put("x", touchX / map.getConfig().getDevicePixelRatio());
                    payload.put("y", touchY / map.getConfig().getDevicePixelRatio());
                    payload.put("mapId", map.getId());

                    notifyListeners("isMapInFocus", payload);
                    return true;
                }
            }

            return v == null || v.onTouchEvent(event);
        });
    }

    @Override
    protected void handleOnDestroy() {
        super.handleOnDestroy();
        maps.values().removeIf(map -> {
            map.getMapView().onDestroy();
            return true;
        });
    }

    @Override
    protected void handleOnResume() {
        super.handleOnResume();
        maps.values().forEach(map -> map.getMapView().onResume());
    }

    @Override
    protected void handleOnPause() {
        super.handleOnPause();
        maps.values().forEach(map -> map.getMapView().onPause());
    }

    @PluginMethod
    public void create(PluginCall call) {
        try {
            String id = call.getString("id");
            if (null == id || id.isEmpty()) {
                throw new IllegalArgumentException("id is required");
            }

            JSObject config = call.getObject("config");
            if (null == config) {
                throw new IllegalArgumentException("config object is missing");
            }

            Boolean forceCreate = call.getBoolean("forceCreate", false);

            if (maps.containsKey(id)) {
                if (ObjUtil.notEqual(forceCreate, true)) {
                    call.resolve();
                    return;
                }

                CapacitorAMap oldMap = maps.remove(id);
                if (oldMap != null) {
                    oldMap.getMapView().onDestroy();
                }
            }

            CapacitorAMap map = new CapacitorAMap(id, new AMapConfig(config), this, call);
            maps.put(id, map);
        } catch (Exception e) {
            call.reject(e.getMessage(), e);
        }
    }

    @PluginMethod
    public void destroy(PluginCall call) {
        try {
            String id = call.getString("id");
            if (null == id || id.isEmpty()) {
                throw new IllegalArgumentException("id is required");
            }

            CapacitorAMap removedMap = maps.remove(id);
            if (removedMap == null) {
                throw new IllegalArgumentException("map not found");
            }

            removedMap.getMapView().onDestroy();
            call.resolve();
        } catch (Exception e) {
            call.reject(e.getMessage(), e);
        }
    }

    @PluginMethod
    public void enableTouch(PluginCall call) {
        this.setTouchEnabled(call, true);
    }

    @PluginMethod
    public void disableTouch(PluginCall call) {
        this.setTouchEnabled(call, false);
    }

    @PluginMethod
    public void onScroll(PluginCall call) {
        this.onResize(call);
    }

    @PluginMethod
    public void onResize(PluginCall call) {
        try {
            CapacitorAMap map = this.getMap(call);

            JSONObject boundsObj = call.getObject("mapBounds");
            if (null == boundsObj) {
                throw new IllegalArgumentException("mapBounds object is missing");
            }

            RectF bounds = this.boundsObjectToRect(boundsObj);

            map.updateRender(bounds);

            call.resolve();
        } catch (Exception e) {
            call.reject(e.getMessage(), e);
        }
    }

    @PluginMethod
    public void onDisplay(PluginCall call) {
        call.unavailable("this call is not available on android");
    }

    @PluginMethod
    public void dispatchMapEvent(PluginCall call) {
        try {
            CapacitorAMap map = this.getMap(call);

            String id = call.getString("id");

            boolean focus = Boolean.TRUE.equals(call.getBoolean("focus", false));

            MutableList<MotionEvent> events = cachedTouchEvents.get(id);
            if (events != null) {
                while (events.size() > 0) {
                    MotionEvent event = events.first();
                    if (focus) {
                        map.getMapView().dispatchTouchEvent(event);
                    } else {
                        this.bridge.getWebView().onTouchEvent(event);
                    }
                    events.removeFirst();
                }
            }

            call.resolve();
        } catch (Exception e) {
            call.reject(e.getMessage(), e);
        }
    }

    @NonNull
    private CapacitorAMap getMap(PluginCall call) {
        String id = call.getString("id");
        if (null == id || id.isEmpty()) {
            throw new IllegalArgumentException("id is required");
        }

        CapacitorAMap map = maps.get(id);
        if (map == null) {
            throw new IllegalArgumentException("map not found");
        }

        return map;
    }

    private void setTouchEnabled(PluginCall call, boolean enabled) {
        try {
            String id = call.getString("id");
            if (null == id || id.isEmpty()) {
                throw new IllegalArgumentException("id is required");
            }

            CapacitorAMap map = maps.get(id);
            if (map == null) {
                throw new IllegalArgumentException("map not found");
            }

            map.setTouchEnabled(enabled);
            call.resolve();
        } catch (Exception e) {
            call.reject(e.getMessage(), e);
        }
    }

    private RectF boundsObjectToRect(JSONObject jsonObject) throws JSONException {
        if (!jsonObject.has("width")) {
            throw new IllegalArgumentException("AMapConfig object is missing the required 'width' property");
        }

        if (!jsonObject.has("height")) {
            throw new IllegalArgumentException("AMapConfig object is missing the required 'height' property");
        }

        if (!jsonObject.has("x")) {
            throw new IllegalArgumentException("AMapConfig object is missing the required 'x' property");
        }

        if (!jsonObject.has("y")) {
            throw new IllegalArgumentException("AMapConfig object is missing the required 'y' property");
        }

        Double width = jsonObject.getDouble("width");
        Double height = jsonObject.getDouble("height");
        Double x = jsonObject.getDouble("x");
        Double y = jsonObject.getDouble("y");

        return new RectF(x.floatValue(), y.floatValue(), (float) (x + width), (float) (y + height));
    }

    private static class MutableList<E> extends ArrayList<E> {
        public MutableList() {
            super();
        }

        public E first() {
            if (super.isEmpty()) {
                throw new NoSuchElementException("List is empty.");
            } else {
                return super.get(0);
            }
        }

        public E removeFirst() {
            if (super.isEmpty()) {
                throw new NoSuchElementException("List is empty.");
            } else {
                return super.remove(0);
            }
        }
    }
}
