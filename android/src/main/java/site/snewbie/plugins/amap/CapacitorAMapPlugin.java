package site.snewbie.plugins.amap;

import static com.amap.api.maps.AMap.MAP_TYPE_NAVI_NIGHT;
import static com.amap.api.maps.AMap.MAP_TYPE_NORMAL;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MyLocationStyle;
import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import site.snewbie.plugins.amap.extend.OfflineMapActivity;

@RequiresApi(api = Build.VERSION_CODES.R)
@CapacitorPlugin(name = "CapacitorAMap", permissions = {
        @Permission(strings = {
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        }, alias = "location")
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

                    this.notifyListeners(map.getId(), "isMapInFocus", payload);
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
    public void updatePrivacyShow(PluginCall call) {
        try {
            // isContains: 隐私权政策是否包含高德开平隐私权政策  true是包含
            // isShow: 隐私权政策是否弹窗展示告知用户 true是展示
            boolean isContains = Boolean.TRUE.equals(call.getBoolean("isContains", false));
            boolean isShow = Boolean.TRUE.equals(call.getBoolean("isShow", false));

            MapsInitializer.updatePrivacyShow(super.getContext(), isContains, isShow);
            call.resolve();
        } catch (Exception e) {
            call.reject(e.getMessage(), e);
        }
    }

    @PluginMethod
    public void updatePrivacyAgree(PluginCall call) {
        try {
            // isAgree: 隐私权政策是否取得用户同意  true是用户同意
            boolean isAgree = Boolean.TRUE.equals(call.getBoolean("isAgree", false));

            MapsInitializer.updatePrivacyAgree(super.getContext(), isAgree);
            call.resolve();
        } catch (Exception e) {
            call.reject(e.getMessage(), e);
        }
    }

    @PluginMethod
    public void setTerrainEnable(PluginCall call) {
        try {
            boolean isTerrainEnable = Boolean.TRUE.equals(call.getBoolean("isTerrainEnable", false));

            MapsInitializer.setTerrainEnable(isTerrainEnable);
            call.resolve();
        } catch (Exception e) {
            call.reject(e.getMessage(), e);
        }
    }

    @PluginMethod
    public void openOfflineMapActivity(PluginCall call) {
        try {
            Intent intent = new Intent(this.bridge.getActivity(), OfflineMapActivity.class);
            this.bridge.getActivity().startActivity(intent);

            // 在调用 openOfflineMapActivity 的地方
            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (OfflineMapActivity.ON_CREATED_ACTION.equals(intent.getAction())) {
                        call.resolve();
                    }
                }
            };

            IntentFilter filter = new IntentFilter(OfflineMapActivity.ON_CREATED_ACTION);
            this.bridge.getActivity().registerReceiver(receiver, filter);
        } catch (Exception e) {
            call.reject(e.getMessage(), e);
        }
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
    public void showIndoorMap(PluginCall call) {
        try {
            CapacitorAMap map = this.getMap(call);
            boolean enable = Boolean.TRUE.equals(call.getBoolean("enable", false));
            map.getMapView().getMap().showIndoorMap(enable);
            call.resolve();
        } catch (Exception e) {
            call.reject(e.getMessage(), e);
        }
    }

    @PluginMethod
    public void setMapType(PluginCall call) {
        try {
            CapacitorAMap map = this.getMap(call);
            if (!call.hasOption("type")) {
                throw new IllegalArgumentException("type is required");
            }

            Integer type = call.getInt("type", null);
            if (type == null || type < MAP_TYPE_NORMAL || type > MAP_TYPE_NAVI_NIGHT) {
                throw new IllegalArgumentException("type is invalid");
            }

            map.getMapView().getMap().setMapType(type);
            call.resolve();
        } catch (Exception e) {
            call.reject(e.getMessage(), e);
        }
    }

    @PluginMethod
    public void setTrafficEnabled(PluginCall call) {
        try {
            CapacitorAMap map = this.getMap(call);
            boolean enable = Boolean.TRUE.equals(call.getBoolean("enable", false));
            map.getMapView().getMap().setTrafficEnabled(enable);
            call.resolve();
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

    @PluginMethod
    public void enableMyLocation(PluginCall call) {
        try {
            CapacitorAMap map = this.getMap(call);

            if (super.getPermissionState("location") != PermissionState.GRANTED) {
                super.requestPermissionForAlias("location", call, "locationPermsCallback");
                return;
            }

            map.getMapView().getMap().setMyLocationEnabled(true);

            call.resolve();
        } catch (Exception e) {
            call.reject(e.getMessage(), e);
        }
    }

    @PermissionCallback
    private void locationPermsCallback(PluginCall call) {
        if (super.getPermissionState("location") == PermissionState.GRANTED) {
            this.enableMyLocation(call);
        } else {
            call.reject("Permission is required to enable location");
        }
    }

    @PluginMethod
    public void disableMyLocation(PluginCall call) {
        try {
            CapacitorAMap map = this.getMap(call);
            map.getMapView().getMap().setMyLocationEnabled(false);
            call.resolve();
        } catch (Exception e) {
            call.reject(e.getMessage(), e);
        }
    }

    @PluginMethod
    public void setMyLocationStyle(PluginCall call) {
        try {
            CapacitorAMap map = this.getMap(call);

            JSObject style = call.getObject("style");
            if (null == style) {
                throw new IllegalArgumentException("style object is missing");
            }

            MyLocationStyle myLocationStyle = new MyLocationStyle();
            if (style.has("interval")) {
                myLocationStyle.interval(style.getInt("interval"));
            }
            if (style.has("myLocationType")) {
                myLocationStyle.myLocationType(style.getInt("myLocationType"));
            }
            if (style.has("showMyLocation")) {
                myLocationStyle.showMyLocation(style.getBoolean("showMyLocation"));
            }

            map.getMapView().getMap().setMyLocationStyle(myLocationStyle);

            call.resolve();
        } catch (Exception e) {
            call.reject(e.getMessage(), e);
        }
    }

    @PluginMethod
    public void setUiSettings(PluginCall call) {
        try {
            CapacitorAMap map = this.getMap(call);

            UiSettings uiSettings = map.getMapView().getMap().getUiSettings();
            if (call.hasOption("myLocationButtonEnabled")) {
                uiSettings.setMyLocationButtonEnabled(Boolean.TRUE.equals(call.getBoolean("myLocationButtonEnabled", false)));
            }

            call.resolve();
        } catch (Exception e) {
            call.reject(e.getMessage(), e);
        }
    }

    @PluginMethod
    public void cameraUpdatePosition(PluginCall call) {
        try {
            CapacitorAMap map = this.getMap(call);

            if (!call.getData().has("cameraOptions")) {
                throw new IllegalArgumentException("cameraOptions is required");
            }

            CameraOptions cameraOptions = new CameraOptions(call.getObject("cameraOptions"));

            // 改变地图的中心点
            // 参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraOptions.toCameraPosition());

            if (cameraOptions.isAnimated()) {
                map.getMapView().getMap().animateCamera(cameraUpdate);
            } else {
                map.getMapView().getMap().moveCamera(cameraUpdate);
            }

            call.resolve();
        } catch (Exception e) {
            call.reject(e.getMessage(), e);
        }
    }

    @PluginMethod
    public void cameraZoomTo(PluginCall call) {
        try {
            CapacitorAMap map = this.getMap(call);

            boolean animated = Boolean.TRUE.equals(call.getBoolean("animated", false));
            Integer zoom = call.getInt("zoom", null);
            if (zoom == null) {
                throw new IllegalArgumentException("zoom is required");
            }

            // 改变地图的缩放级别
            // 设置希望展示的地图缩放级别
            CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(zoom);

            if (animated) {
                map.getMapView().getMap().animateCamera(mCameraUpdate);
            } else {
                map.getMapView().getMap().moveCamera(mCameraUpdate);
            }

            call.resolve();
        } catch (Exception e) {
            call.reject(e.getMessage(), e);
        }
    }

    @PluginMethod
    public void setMapStatusLimits(PluginCall call) {
        try {
            CapacitorAMap map = this.getMap(call);

            Location southwest = Location.fromJSObject(call, "southwest");
            if (null == southwest) {
                throw new IllegalArgumentException("southwest object is missing");
            }

            Location northeast = Location.fromJSObject(call, "northeast");
            if (null == northeast) {
                throw new IllegalArgumentException("northeast object is missing");
            }

            LatLngBounds latLngBounds = new LatLngBounds(southwest.toLatLng(), northeast.toLatLng());
            map.getMapView().getMap().setMapStatusLimits(latLngBounds);

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

    public void notifyListeners(String mapId, String event, JSObject data) {
        if (data == null) {
            data = new JSObject();
        }

        data.put("mapId", mapId);

        super.notifyListeners(event, data);
    }

}
