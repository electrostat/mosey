package agbytech.com.mosey2

import android.content.Context
import java.util.logging.Logger

class Mosey {
    lateinit var context: Context

    constructor(context: Context) {
        this.context = context

        Logger.getLogger(Mosey::class.java.name).warning("Is Wifi RTT available: " + Utils.supportWifiRTT(context))
    }
}