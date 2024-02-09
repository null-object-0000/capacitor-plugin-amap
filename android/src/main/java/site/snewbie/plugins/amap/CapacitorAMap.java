package site.snewbie.plugins.amap;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.Location;
import android.os.Build;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.IndoorBuildingInfo;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps.model.Polyline;
import com.getcapacitor.Bridge;
import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;

import lombok.Getter;
import lombok.Setter;

@Getter
@RequiresApi(api = Build.VERSION_CODES.R)
public class CapacitorAMap {
    private final String id;
    private final AMapConfig config;
    private final CapacitorAMapPlugin delegate;

    private final MapView mapView;
    @Setter
    private boolean touchEnabled;

    @RequiresApi(api = Build.VERSION_CODES.R)
    public CapacitorAMap(String id, AMapConfig config, CapacitorAMapPlugin delegate, PluginCall call) {
        this.id = id;
        this.config = config;
        this.delegate = delegate;

        AMapOptions mapOptions = new AMapOptions()
                .logoPosition(config.getLogoPosition())
                .mapType(config.getMapType())
                .scaleControlsEnabled(config.isScaleControlsEnabled())
                .compassEnabled(config.isCompassEnabled())
                .rotateGesturesEnabled(config.isRotateGesturesEnabled())
                .scrollGesturesEnabled(config.isScrollGesturesEnabled())
                .tiltGesturesEnabled(config.isTiltGesturesEnabled())
                .zoomControlsEnabled(config.isZoomControlsEnabled())
                .zoomGesturesEnabled(config.isZoomGesturesEnabled());

        if (config.getCameraOptions() != null) {
            mapOptions.camera(config.getCameraOptions().toCameraPosition());
        }

        this.mapView = new MapView(delegate.getContext(), mapOptions);
        this.mapView.onCreate(null);

        AMap map = this.mapView.getMap();
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setAllGesturesEnabled(true);

        this.render(call, config);
    }

    private void render(PluginCall call, AMapConfig config) {
        this.delegate.getActivity().runOnUiThread(() -> {
            try {
                Bridge bridge = this.delegate.getBridge();
                FrameLayout mapViewParent = new FrameLayout(bridge.getContext());
                mapViewParent.setMinimumHeight(bridge.getWebView().getHeight());
                mapViewParent.setMinimumWidth(bridge.getWebView().getWidth());

                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        this.getScaledPixels(bridge, config.getWidth()),
                        this.getScaledPixels(bridge, config.getHeight())
                );
                layoutParams.leftMargin = this.getScaledPixels(bridge, config.getX());
                layoutParams.topMargin = this.getScaledPixels(bridge, config.getY());

                mapViewParent.setTag(this.id);

                this.mapView.setLayoutParams(layoutParams);
                mapViewParent.addView(this.mapView);

                ((ViewGroup) (bridge.getWebView().getParent())).addView(mapViewParent);

                bridge.getWebView().bringToFront();
                bridge.getWebView().setBackgroundColor(Color.TRANSPARENT);

                this.setMapEventListeners();

                call.resolve();
            } catch (Exception e) {
                call.reject(e.getMessage(), e);
            }
        });
    }

    public void updateRender(RectF updatedBounds) {
        this.config.setX((int) updatedBounds.left);
        this.config.setY((int) updatedBounds.top);
        this.config.setWidth((int) updatedBounds.width());
        this.config.setHeight((int) updatedBounds.height());

        this.delegate.getActivity().runOnUiThread(() -> {
            Bridge bridge = this.delegate.getBridge();
            RectF mapRect = getScaledRect(bridge, updatedBounds);
            mapView.setX(mapRect.left);
            mapView.setY(mapRect.top);
            if (mapView.getLayoutParams().width != config.getWidth() || mapView.getLayoutParams().height != config.getHeight()) {
                mapView.getLayoutParams().width = getScaledPixels(bridge, config.getWidth());
                mapView.getLayoutParams().height = getScaledPixels(bridge, config.getHeight());
                mapView.requestLayout();
            }
        });
    }

    public Rect getMapBounds() {
        return new Rect(
                this.getScaledPixels(this.delegate.getBridge(), config.getX()),
                this.getScaledPixels(this.delegate.getBridge(), config.getY()),
                this.getScaledPixels(this.delegate.getBridge(), config.getX() + config.getWidth()),
                this.getScaledPixels(this.delegate.getBridge(), config.getY() + config.getHeight())
        );
    }

    private Integer getScaledPixels(Bridge bridge, int pixels) {
        // Get the screen's density scale
        float scale = bridge.getActivity().getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    private Float getScaledPixelsF(Bridge bridge, Float pixels) {
        // Get the screen's density scale
        float scale = bridge.getActivity().getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (pixels * scale + 0.5f);
    }

    private RectF getScaledRect(Bridge bridge, RectF rectF) {
        return new RectF(
                this.getScaledPixelsF(bridge, rectF.left),
                this.getScaledPixelsF(bridge, rectF.top),
                this.getScaledPixelsF(bridge, rectF.right),
                this.getScaledPixelsF(bridge, rectF.bottom)
        );
    }

    private void setMapEventListeners() {
        AMap map = this.mapView.getMap();

        // 设置地图状态的监听接口
        map.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                notifyListeners("onCameraChange", cameraPosition);
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                notifyListeners("onCameraChangeFinish", cameraPosition);
            }
        });
        // 设置室内地图状态监听接口
        map.setOnIndoorBuildingActiveListener(indoorBuildingInfo -> this.notifyListeners("onIndoorBuilding", indoorBuildingInfo));
        // 设置marker的信息窗口点击事件监听接口
        map.setOnInfoWindowClickListener(marker -> this.notifyListeners("onInfoWindowClick", marker));
        // 设置地图点击事件监听接口
        map.setOnMapClickListener(point -> this.notifyListeners("onMapClick", point));
        // 设置地图加载完成监听接口
        map.setOnMapLoadedListener(() -> this.notifyListeners("onMapReady"));
        // 设置地图长按事件监听接口
        map.setOnMapLongClickListener(point -> this.notifyListeners("onMapLongClick", point));
        // 设置地图触摸事件监听接口
        map.setOnMapTouchListener(event -> this.notifyListeners("onMapTouch", event));
        // 设置marker点击事件监听接口
        map.setOnMarkerClickListener(marker -> {
            this.notifyListeners("onMarkerClick", marker);
            return false;
        });
        // marker拖动事件监听接口
        map.setOnMarkerDragListener(new AMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                notifyListeners("onMarkerDragStart", marker);
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                notifyListeners("onMarkerDrag", marker);
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                notifyListeners("onMarkerDragEnd", marker);
            }
        });
        // 设置海量点单击事件监听
        map.setOnMultiPointClickListener(pointItem -> {
            this.notifyListeners("onMultiPointClick", pointItem);
            return false;
        });
        // 设置用户定位信息监听接口。
        map.setOnMyLocationChangeListener(location -> this.notifyListeners("onMyLocationChange", location));
        // 设置底图poi点击事件监听接口
        map.setOnPOIClickListener(poi -> this.notifyListeners("onPOIClick", poi));
        // 设置polyline点击事件监听接口
        map.setOnPolylineClickListener(polyline -> this.notifyListeners("onPolylineClick", polyline));
    }

    public void notifyListeners(String eventName) {
        this.delegate.notifyListeners(this.id, eventName, null);
    }

    public void notifyListeners(String eventName, Object data) {
        Class<?> clazz = data.getClass();
        if (JSObject.class.equals(clazz)) {
            this.delegate.notifyListeners(this.id, eventName, (JSObject) data);
        } else if (LatLng.class.equals(clazz)) {
            this.delegate.notifyListeners(this.id, eventName, this.latLng2JSObject((LatLng) data));
        } else if (CameraPosition.class.equals(clazz)) {
            CameraPosition position = (CameraPosition) data;
            JSObject payload = new JSObject();
            payload.put("target", this.latLng2JSObject(position.target));
            payload.put("zoom", position.zoom);
            payload.put("tilt", position.tilt);
            payload.put("bearing", position.bearing);
            payload.put("isAbroad", position.isAbroad);

            this.delegate.notifyListeners(this.id, eventName, payload);
        } else if (Marker.class.equals(clazz)) {
            Marker marker = (Marker) data;
            JSObject options = new JSObject();
            options.put("title", marker.getOptions().getTitle());
            options.put("snippet", marker.getOptions().getSnippet());

            JSObject payload = new JSObject();
            payload.put("id", marker.getId());
            payload.put("position", this.latLng2JSObject(marker.getPosition()));
            payload.put("options", options);

            this.delegate.notifyListeners(this.id, eventName, payload);
        } else if (MotionEvent.class.equals(clazz)) {
            MotionEvent event = (MotionEvent) data;
            JSObject payload = new JSObject();
            payload.put("x", event.getX() / this.config.getDevicePixelRatio());
            payload.put("y", event.getY() / this.config.getDevicePixelRatio());
            this.delegate.notifyListeners(this.id, eventName, payload);
        } else if (IndoorBuildingInfo.class.equals(clazz)) {
            IndoorBuildingInfo indoorBuildingInfo = (IndoorBuildingInfo) data;
            JSObject payload = new JSObject();
            payload.put("activeFloorName", indoorBuildingInfo.activeFloorName);
            payload.put("activeFloorIndex", indoorBuildingInfo.activeFloorIndex);
            payload.put("poiId", indoorBuildingInfo.poiid);
            payload.put("floorIndexs", indoorBuildingInfo.floor_indexs);
            payload.put("floorNames", indoorBuildingInfo.floor_names);
            this.delegate.notifyListeners(this.id, eventName, payload);
        } else if (Location.class.equals(clazz)) {
            this.delegate.notifyListeners(this.id, eventName, this.location2JSObject((Location) data));
        } else if (AMapLocation.class.equals(clazz)) {
            this.delegate.notifyListeners(this.id, eventName, this.location2JSObject((AMapLocation) data));
        } else if (MultiPointItem.class.equals(clazz)) {
            MultiPointItem pointItem = (MultiPointItem) data;
            JSObject payload = new JSObject();
            payload.put("latLng", this.latLng2JSObject(pointItem.getLatLng()));
            payload.put("point", this.point2JSObject(pointItem.getIPoint()));
            payload.put("object", pointItem.getObject());
            payload.put("customerId", pointItem.getCustomerId());
            payload.put("title", pointItem.getTitle());
            payload.put("snippet", pointItem.getSnippet());
            this.delegate.notifyListeners(this.id, eventName, payload);
        } else if (Poi.class.equals(clazz)) {
            Poi poi = (Poi) data;
            JSObject payload = new JSObject();
            payload.put("name", poi.getName());
            payload.put("coordinate", this.latLng2JSObject(poi.getCoordinate()));
            payload.put("poiId", poi.getPoiId());
            this.delegate.notifyListeners(this.id, eventName, payload);
        } else if (Polyline.class.equals(clazz)) {
            Polyline polyline = (Polyline) data;
            JSObject payload = new JSObject();
            payload.put("id", polyline.getId());
            this.delegate.notifyListeners(this.id, eventName, payload);
        } else {
            throw new IllegalArgumentException("Unsupported data type: " + clazz.getName());
        }
    }

    private JSObject latLng2JSObject(LatLng latLng) {
        JSObject payload = new JSObject();
        payload.put("latitude", latLng.latitude);
        payload.put("longitude", latLng.longitude);
        return payload;
    }

    private JSObject point2JSObject(Point point) {
        JSObject payload = new JSObject();
        payload.put("x", point.x / this.config.getDevicePixelRatio());
        payload.put("y", point.y / this.config.getDevicePixelRatio());
        return payload;
    }

    private JSObject location2JSObject(Location location) {
        JSObject payload = new JSObject();
        payload.put("latitude", location.getLatitude());
        payload.put("longitude", location.getLongitude());
        payload.put("accuracy", location.getAccuracy());
        payload.put("altitude", location.getAltitude());
        payload.put("bearing", location.getBearing());
        payload.put("speed", location.getSpeed());
        payload.put("time", location.getTime());
        return payload;
    }

    private JSObject location2JSObject(AMapLocation location) {
        if (location.getErrorCode() != 0) {
            JSObject payload = new JSObject();
            payload.put("errorCode", location.getErrorCode());
            payload.put("errorInfo", location.getErrorInfo());
            return payload;
        }

        JSObject payload = this.location2JSObject((Location) location);
        payload.put("errorCode", location.getErrorCode());
        payload.put("errorInfo", location.getErrorInfo());
        return payload;
    }

}
