package site.snewbie.plugins.amap;

import org.json.JSONException;
import org.json.JSONObject;

public class AMapConfig {
    private Integer width;
    private Integer height;
    private Integer x;
    private Integer y;
    private Float devicePixelRatio = 1.00f;

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
}
