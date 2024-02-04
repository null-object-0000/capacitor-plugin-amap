package site.snewbie.plugins.amap;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class AMapConfig {
    private Integer width;
    private Integer height;
    private Integer x;
    private Integer y;
    private Float devicePixelRatio = 1.00f;

    private int logoPosition = AMapOptions.LOGO_POSITION_BOTTOM_LEFT;
    private int mapType = AMap.MAP_TYPE_NORMAL;
    private boolean scaleControlsEnabled = false;
    private boolean zoomControlsEnabled = true;
    private boolean compassEnabled = true;
    private boolean scrollGesturesEnabled = true;
    private boolean zoomGesturesEnabled = true;
    private boolean tiltGesturesEnabled = true;
    private boolean rotateGesturesEnabled = true;

    public AMapConfig(JSONObject fromJSONObject) throws JSONException {
        if (!fromJSONObject.has("width")) {
            throw new IllegalArgumentException("AMapConfig object is missing the required 'width' property");
        }

        if (!fromJSONObject.has("height")) {
            throw new IllegalArgumentException("AMapConfig object is missing the required 'height' property");
        }

        if (!fromJSONObject.has("x")) {
            throw new IllegalArgumentException("AMapConfig object is missing the required 'x' property");
        }

        if (!fromJSONObject.has("y")) {
            throw new IllegalArgumentException("AMapConfig object is missing the required 'y' property");
        }

        if (fromJSONObject.has("devicePixelRatio")) {
            devicePixelRatio = Double.valueOf(fromJSONObject.getDouble("devicePixelRatio")).floatValue();
        }

        if (fromJSONObject.has("logoPosition")) {
            logoPosition = fromJSONObject.getInt("logoPosition");
        }

        if (fromJSONObject.has("mapType")) {
            mapType = fromJSONObject.getInt("mapType");
        }

        if (fromJSONObject.has("scaleControlsEnabled")) {
            scaleControlsEnabled = fromJSONObject.getBoolean("scaleControlsEnabled");
        }

        if (fromJSONObject.has("zoomControlsEnabled")) {
            zoomControlsEnabled = fromJSONObject.getBoolean("zoomControlsEnabled");
        }

        if (fromJSONObject.has("compassEnabled")) {
            compassEnabled = fromJSONObject.getBoolean("compassEnabled");
        }

        if (fromJSONObject.has("scrollGesturesEnabled")) {
            scrollGesturesEnabled = fromJSONObject.getBoolean("scrollGesturesEnabled");
        }

        if (fromJSONObject.has("zoomGesturesEnabled")) {
            zoomGesturesEnabled = fromJSONObject.getBoolean("zoomGesturesEnabled");
        }

        if (fromJSONObject.has("tiltGesturesEnabled")) {
            tiltGesturesEnabled = fromJSONObject.getBoolean("tiltGesturesEnabled");
        }

        if (fromJSONObject.has("rotateGesturesEnabled")) {
            rotateGesturesEnabled = fromJSONObject.getBoolean("rotateGesturesEnabled");
        }

        width = fromJSONObject.getInt("width");
        height = fromJSONObject.getInt("height");
        x = fromJSONObject.getInt("x");
        y = fromJSONObject.getInt("y");
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Float getDevicePixelRatio() {
        return devicePixelRatio;
    }

    public void setDevicePixelRatio(Float devicePixelRatio) {
        this.devicePixelRatio = devicePixelRatio;
    }

    public int getLogoPosition() {
        return logoPosition;
    }

    public void setLogoPosition(int logoPosition) {
        this.logoPosition = logoPosition;
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public boolean isScaleControlsEnabled() {
        return scaleControlsEnabled;
    }

    public void setScaleControlsEnabled(boolean scaleControlsEnabled) {
        this.scaleControlsEnabled = scaleControlsEnabled;
    }

    public boolean isZoomControlsEnabled() {
        return zoomControlsEnabled;
    }

    public void setZoomControlsEnabled(boolean zoomControlsEnabled) {
        this.zoomControlsEnabled = zoomControlsEnabled;
    }

    public boolean isCompassEnabled() {
        return compassEnabled;
    }

    public void setCompassEnabled(boolean compassEnabled) {
        this.compassEnabled = compassEnabled;
    }

    public boolean isScrollGesturesEnabled() {
        return scrollGesturesEnabled;
    }

    public void setScrollGesturesEnabled(boolean scrollGesturesEnabled) {
        this.scrollGesturesEnabled = scrollGesturesEnabled;
    }

    public boolean isZoomGesturesEnabled() {
        return zoomGesturesEnabled;
    }

    public void setZoomGesturesEnabled(boolean zoomGesturesEnabled) {
        this.zoomGesturesEnabled = zoomGesturesEnabled;
    }

    public boolean isTiltGesturesEnabled() {
        return tiltGesturesEnabled;
    }

    public void setTiltGesturesEnabled(boolean tiltGesturesEnabled) {
        this.tiltGesturesEnabled = tiltGesturesEnabled;
    }

    public boolean isRotateGesturesEnabled() {
        return rotateGesturesEnabled;
    }

    public void setRotateGesturesEnabled(boolean rotateGesturesEnabled) {
        this.rotateGesturesEnabled = rotateGesturesEnabled;
    }
}
