package site.snewbie.plugins.amap;

import com.amap.api.maps.model.CameraPosition;
import com.getcapacitor.JSObject;

import org.json.JSONException;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CameraOptions {
    private Location target;
    private Float zoom;
    private Float tilt;
    private Float bearing;
    private boolean animated = true;

    public CameraOptions(JSObject fromJSONObject) throws JSONException {
        if (!fromJSONObject.has("target")) {
            throw new IllegalArgumentException("CameraOptions object is missing the required 'target' property");
        }

        if (!fromJSONObject.has("zoom")) {
            throw new IllegalArgumentException("CameraOptions object is missing the required 'zoom' property");
        }

        if (!fromJSONObject.has("tilt")) {
            throw new IllegalArgumentException("CameraOptions object is missing the required 'tilt' property");
        }

        if (!fromJSONObject.has("bearing")) {
            throw new IllegalArgumentException("CameraOptions object is missing the required 'bearing' property");
        }

        if (fromJSONObject.has("animated")) {
            animated = fromJSONObject.getBoolean("animated");
        }

        target = Location.fromObject(fromJSONObject.get("target"));
        zoom = Double.valueOf(fromJSONObject.getDouble("zoom")).floatValue();
        tilt = Double.valueOf(fromJSONObject.getDouble("tilt")).floatValue();
        bearing = Double.valueOf(fromJSONObject.getDouble("bearing")).floatValue();
    }

    public CameraPosition toCameraPosition() {
        return new CameraPosition(target.toLatLng(), zoom, tilt, bearing);
    }
}
