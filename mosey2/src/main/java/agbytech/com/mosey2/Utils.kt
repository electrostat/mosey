package agbytech.com.mosey2

import android.content.Context
import android.content.pm.PackageManager

class Utils {

    companion object {
        fun supportWifiRTT(context: Context): Boolean {
            return context.packageManager.hasSystemFeature(PackageManager.FEATURE_WIFI_RTT)
        }
    }

}