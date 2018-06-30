package network.kotlin.flow9.net.networkbasic.util

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.CellInfo
import android.telephony.CellLocation
import android.telephony.PhoneStateListener
import android.telephony.ServiceState
import android.telephony.SignalStrength
import android.telephony.TelephonyManager
import android.util.Log

class PhoneUtil {

    companion object {

        var sTelManager: TelephonyManager? = null
        var TAG = "PhoneUtil"

        var phoneStateListener: PhoneStateListener = object : PhoneStateListener() {

            // 서비스 상태 변경 감지
            override fun onServiceStateChanged(serviceState: ServiceState) {
                when (serviceState.state) {
                    ServiceState.STATE_EMERGENCY_ONLY -> {
                    }
                    ServiceState.STATE_IN_SERVICE -> {
                    }
                    ServiceState.STATE_OUT_OF_SERVICE -> {
                    }
                    ServiceState.STATE_POWER_OFF -> {
                    }
                    else -> {
                    }
                }
            }

            override fun onMessageWaitingIndicatorChanged(mwi: Boolean) {
                super.onMessageWaitingIndicatorChanged(mwi)
            }

            override fun onCallForwardingIndicatorChanged(cfi: Boolean) {
                super.onCallForwardingIndicatorChanged(cfi)
            }

            override fun onCellLocationChanged(location: CellLocation) {
                super.onCellLocationChanged(location)
            }

            // 전화가 걸려온 상태 감지
            override fun onCallStateChanged(state: Int, phoneNumber: String) {
                when (state) {
                    TelephonyManager.CALL_STATE_IDLE -> {
                    }
                    TelephonyManager.CALL_STATE_RINGING -> {
                    }
                    TelephonyManager.CALL_STATE_OFFHOOK -> {
                    }
                }
            }

            override fun onDataConnectionStateChanged(state: Int) {
                super.onDataConnectionStateChanged(state)
            }

            override fun onDataConnectionStateChanged(state: Int, networkType: Int) {
                super.onDataConnectionStateChanged(state, networkType)
            }

            override fun onDataActivity(direction: Int) {
                super.onDataActivity(direction)
            }

            override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
                super.onSignalStrengthsChanged(signalStrength)
            }

            override fun onCellInfoChanged(cellInfo: List<CellInfo>) {
                super.onCellInfoChanged(cellInfo)
            }

            override fun onUserMobileDataStateChanged(enabled: Boolean) {
                super.onUserMobileDataStateChanged(enabled)
            }
        }

        fun setPhoneStateListener(context: Context) {
            if (sTelManager == null) {
                sTelManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            }
            sTelManager!!.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
        }

        fun getNetworkInfo(context: Context) {
            if (sTelManager == null) {
                sTelManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            }
            Log.d(TAG, sTelManager!!.networkCountryIso)
            Log.d(TAG, sTelManager!!.networkOperatorName)
            if (sTelManager!!.networkType == TelephonyManager.NETWORK_TYPE_LTE) {
                Log.d(TAG, "LTE")
            }
            if (sTelManager!!.networkType == TelephonyManager.NETWORK_TYPE_HSDPA) {
                Log.d(TAG, "3G")
            }
        }

        fun getLocale(context: Context): String? {
            // Locale myLocale = context.getResources().getConfiguration().locale.getDefault();
            // Locale myLocale2 = Locale.getDefault();
            return null
        }

        @SuppressLint("MissingPermission")
        fun getPhoneNumber(context: Context): String {
            if (sTelManager == null) {
                sTelManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            }
            return sTelManager!!.line1Number
        }

        @SuppressLint("MissingPermission")
        fun getSubscriberId(context: Context): String {
            if (sTelManager == null) {
                sTelManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            }
            return sTelManager!!.subscriberId
        }

        @SuppressLint("MissingPermission")
        fun getDeviceId(context: Context): String {
            if (sTelManager == null) {
                sTelManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            }
            return sTelManager!!.deviceId
        }

    }

}
