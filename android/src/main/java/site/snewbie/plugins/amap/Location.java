package site.snewbie.plugins.amap;

import com.amap.api.maps.model.LatLng;
import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private double latitude;
    private double longitude;

    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }

    public static LatLng toLatLng(Location location) {
        if (location == null) {
            return null;
        }

        return location.toLatLng();
    }

    public static Location fromLatLng(LatLng latLng) {
        if (latLng == null) {
            return null;
        }

        Location location = new Location();
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        return location;
    }

    public static Location fromObject(Object object) throws JSONException {
        if (object == null) {
            return null;
        }

        if (object instanceof Location) {
            return (Location) object;
        }

        if (object instanceof LatLng) {
            return Location.fromLatLng((LatLng) object);
        }

        if (object instanceof JSONObject jsObject) {
            Location location = new Location();
            location.setLatitude(jsObject.getDouble("latitude"));
            location.setLongitude(jsObject.getDouble("longitude"));
            return location;
        }

        return null;
    }

    public static Location fromJSObject(JSObject jsObject, String key) throws JSONException {
        if (jsObject == null) {
            return null;
        }

        if (jsObject.has(key)) {
            return Location.fromObject(jsObject.get(key));
        }

        return null;
    }

    public static Location fromJSObject(PluginCall call, String key) throws JSONException {
        if (call == null || call.getData() == null) {
            return null;
        }

        return Location.fromObject(call.getData().get(key));
    }
}
