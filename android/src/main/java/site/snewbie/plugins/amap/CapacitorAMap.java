package site.snewbie.plugins.amap;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.getcapacitor.Bridge;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;

public class CapacitorAMap {
    private final String id;
    private final AMapConfig config;
    private final Plugin delegate;

    private final MapView mapView;
    private boolean touchEnabled;

    public String getId() {
        return id;
    }

    public AMapConfig getConfig() {
        return config;
    }

    public MapView getMapView() {
        return mapView;
    }

    public boolean isTouchEnabled() {
        return touchEnabled;
    }

    public void setTouchEnabled(boolean touchEnabled) {
        this.touchEnabled = touchEnabled;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public CapacitorAMap(String id, AMapConfig config, Plugin delegate, PluginCall call) {
        this.id = id;
        this.config = config;
        this.delegate = delegate;

        AMapOptions mapOptions = new AMapOptions()
                .scaleControlsEnabled(false)
                .compassEnabled(false)
                .rotateGesturesEnabled(false)
                .scrollGesturesEnabled(false)
                .tiltGesturesEnabled(false)
                .zoomControlsEnabled(false)
                .zoomGesturesEnabled(false);

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
                layoutParams.leftMargin = getScaledPixels(bridge, config.getX());
                layoutParams.topMargin = getScaledPixels(bridge, config.getY());

                mapViewParent.setTag(this.id);

                this.mapView.setLayoutParams(layoutParams);
                mapViewParent.addView(this.mapView);

                ((ViewGroup) (bridge.getWebView().getParent())).addView(mapViewParent);

                bridge.getWebView().bringToFront();
                bridge.getWebView().setBackgroundColor(Color.TRANSPARENT);

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

}
