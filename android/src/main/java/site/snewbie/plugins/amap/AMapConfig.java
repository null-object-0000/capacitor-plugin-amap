package site.snewbie.plugins.amap;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.getcapacitor.JSObject;

import org.json.JSONException;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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

    private CameraOptions cameraOptions;

    public AMapConfig(JSObject fromJSONObject) throws JSONException {
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

        JSObject cameraOptionsObj = fromJSONObject.getJSObject("cameraOptions");
        if (cameraOptionsObj != null) {
            cameraOptions = new CameraOptions(cameraOptionsObj);
        }

        width = fromJSONObject.getInt("width");
        height = fromJSONObject.getInt("height");
        x = fromJSONObject.getInt("x");
        y = fromJSONObject.getInt("y");
    }
}
