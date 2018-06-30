package network.kotlin.flow9.net.networkbasic.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.NetworkInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager

class WifiUtil {

    companion object {

        var wifiStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == WifiManager.WIFI_STATE_CHANGED_ACTION) {
                    val state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1)
                    if (state == WifiManager.WIFI_STATE_ENABLED) {

                    }
                } else if (intent.action == WifiManager.NETWORK_STATE_CHANGED_ACTION) {
                    val networkInfo = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
                    if (networkInfo.state == NetworkInfo.State.CONNECTED) {

                    } else if (networkInfo.state == NetworkInfo.State.DISCONNECTED) {

                    }
                }
            }
        }

        fun checkAndSetWifiState(context: Context) {
            val wm = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            if (!wm.isWifiEnabled) {
                if (wm.wifiState != WifiManager.WIFI_STATE_ENABLING) {
                    wm.isWifiEnabled = true
                }
            }
        }

        fun registerWifiStateListener(context: Context) {
            val intentFilter = IntentFilter()
            intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
            intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
            intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION)
            context.registerReceiver(wifiStateReceiver, intentFilter)
        }

    }

}
