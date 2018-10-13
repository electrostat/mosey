package agbytech.com.mosey2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.rtt.WifiRttManager
import java.util.logging.Logger

class Mosey {
    lateinit var context: Context

    constructor(context: Context) {
        this.context = context

        Logger.getLogger(Mosey::class.java.name).warning("Is Wifi RTT available: " + Utils.supportWifiRTT(context))

        val filter = IntentFilter(WifiRttManager.ACTION_WIFI_RTT_STATE_CHANGED)
        val myReceiver = object: BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                if (Utils.supportWifiRTT(context)) {
                    Logger.getLogger(Mosey::class.java.name).warning("Wifi RTT available")
                } else {
                    Logger.getLogger(Mosey::class.java.name).warning("Wifi RTT unavailable")
                }
            }
        }
        context.registerReceiver(myReceiver, filter)

        Utils.startWifiScan(context)
    }
}