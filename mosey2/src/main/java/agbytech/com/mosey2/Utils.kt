package agbytech.com.mosey2

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.net.wifi.rtt.RangingRequest
import android.net.wifi.rtt.RangingResult
import android.net.wifi.rtt.RangingResultCallback
import android.net.wifi.rtt.WifiRttManager
import android.util.Log
import java.util.logging.Logger

class Utils {
    lateinit var wifiManager: WifiManager

    companion object {
        fun supportWifiRTT(context: Context): Boolean {
            return context.packageManager.hasSystemFeature(PackageManager.FEATURE_WIFI_RTT)
        }

        fun getRangingRequest(scanResults: List<ScanResult>): RangingRequest {

            return RangingRequest.Builder().run {
                addAccessPoints(limitResults(scanResults, RangingRequest.getMaxPeers()))
                build()
            }
        }

        fun startWifiScan(context: Context) {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

            val wifiScanReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                    if (success) {
                        scanSuccess(wifiManager, context)
                    } else {
                        scanFailure(wifiManager)
                    }
                }
            }

            val intentFilter = IntentFilter()
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            context.registerReceiver(wifiScanReceiver, intentFilter)

            val success = wifiManager.startScan()

            if (!success) {
                // scan failure handling
                scanFailure(wifiManager)
            }
        }

        private fun scanSuccess(wifiManager: WifiManager, context: Context) {
            val results = wifiManager.scanResults
            val rangingRequest = getRangingRequest(results)
            requestRanging(context, rangingRequest)
        }

        private fun scanFailure(wifiManager: WifiManager) {
            // handle failure: new scan did NOT succeed
            // consider using old scan results: these are the OLD results!
            val results = wifiManager.scanResults
            Log.e("test", "failure results: $results")
        }

        @SuppressLint("MissingPermission")
        fun requestRanging(context: Context, request: RangingRequest) {
            val executor = MyExecutor()
            val mgr = context.getSystemService(Context.WIFI_RTT_RANGING_SERVICE) as WifiRttManager

            Logger.getLogger(Mosey::class.java.name).warning("request: $request")
            Logger.getLogger(Mosey::class.java.name).warning("executor: $executor")

            mgr.startRanging(request, executor, object : RangingResultCallback() {

                override fun onRangingResults(results: List<RangingResult>) {
                    Log.e("test", "Ranging Results: $results")
                }

                override fun onRangingFailure(code: Int) {
                    Logger.getLogger(Mosey::class.java.name).warning("Ranging failure: $code")
                }
            })
        }

        fun limitResults(scanResults: List<ScanResult>, max: Int): List<ScanResult> {
            return scanResults.take(max)
        }
    }
}