package site.snewbie.plugins.amap.extend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class OfflineMapActivity extends com.amap.api.maps.offlinemap.OfflineMapActivity {
    public final static String ON_CREATED_ACTION = "site.snewbie.plugins.amap.extend.OfflineMapActivity_Created";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        super.sendBroadcast(new Intent(ON_CREATED_ACTION));
    }
}
