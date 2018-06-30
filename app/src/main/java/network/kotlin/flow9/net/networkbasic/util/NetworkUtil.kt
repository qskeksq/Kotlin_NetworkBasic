package network.kotlin.flow9.net.networkbasic.util

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.net.Uri
import android.os.Build
import android.os.NetworkOnMainThreadException
import android.os.StrictMode
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.Toast

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class NetworkUtil {

    companion object {

        val ADDRESS = "http://www.googlg.com"

        var cm: ConnectivityManager? = null

        /**
         * receives when download is complete
         */
        var downloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Toast.makeText(context, "다운로드 완료", Toast.LENGTH_LONG).show()
            }
        }

        private var networkStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Toast.makeText(context, "네트워크 상태 변화 감지", Toast.LENGTH_LONG).show()
            }
        }

        /**
         *
         */
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        private var networkStateCallback: ConnectivityManager.NetworkCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                val info = cm!!.getNetworkInfo(network)
                val networkTypeName = info.typeName
            }

            override fun onLost(network: Network) {

            }
        }

        fun getNetworkInfo(context: Context): NetworkInfo {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun getNetworkInfo(context: Context, network: Network): NetworkInfo {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.getNetworkInfo(network)
        }

        fun getAllNetworkInfo(context: Context): Array<NetworkInfo> {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.allNetworkInfo
        }

        fun permitMainThreadNetwork() {
            val policy = StrictMode.ThreadPolicy.Builder()
                    .permitNetwork()
                    .build()
            StrictMode.setThreadPolicy(policy)
        }

        /**
         * get
         */
        fun get() {
            try {
                val url = URL("http://www.google.com")
                val conn = url.openConnection() as HttpURLConnection
                val `is` = BufferedInputStream(conn.inputStream)

            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        /**
         * post
         */
        fun post(address: String) {
            try {
                val url = URL(address)
                val conn = url.openConnection() as HttpURLConnection
                conn.doInput = true
                conn.setChunkedStreamingMode(0)

                val os = BufferedOutputStream(conn.outputStream)
                // os.write();
                // os.flush();

                val `is` = BufferedInputStream(conn.inputStream)
                // read
                os.close()
                `is`.close()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        /**
         * simple HTTP communication
         */
        fun downloadHTML(address: String): String {
            var html = ""
            try {
                val url = URL(address)
                val urlConnection = url.openConnection() as HttpURLConnection
                if (urlConnection != null) {
                    urlConnection.connectTimeout = 50000
                    if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                        val isr = InputStreamReader(urlConnection.inputStream)
                        val br = BufferedReader(isr)
                        var line: String? = br.readLine()
                        while (line != null) {
                            html += line
                            html += "\n"
                            line = br.readLine()
                        }
                        isr.close()
                        br.close()
                    }
                    urlConnection.disconnect()
                }
            } catch (e: NetworkOnMainThreadException) {
                Log.e("네트워크 메인스레드 오류", e.message)
            } catch (e: MalformedURLException) {
                Log.e("네트워크 URL 오류", e.message)
            } catch (e: IOException) {
                Log.e("네트워크 스트림 오류", e.message)
            }
            return html
        }

        /**
         * download image by HTTP communication
         */
        fun downloadImageOne(context: Context, address: String, filename: String): Boolean {
            try {
                val url = URL(address)
                val conn = url.openConnection() as HttpURLConnection
                conn.connectTimeout = 50000
                val length = conn.contentLength
                val buffer = ByteArray(length)
                val `is` = conn.inputStream
                val fos = context.openFileOutput(filename, Context.MODE_PRIVATE)
                var data = `is`.read(buffer)
                while (data > 0) {
                    fos.write(buffer, 0, data)
                    data = `is`.read(buffer)
                }
                `is`.close()
                fos.close()
                conn.disconnect()
            } catch (e: NetworkOnMainThreadException) {
                Log.e("네트워크 메인스레드 오류", e.message)
                return false
            } catch (e: MalformedURLException) {
                Log.e("네트워크 URL 오류", e.message)
                return false
            } catch (e: IOException) {
                Log.e("네트워크 스트림 오류", e.message)
                return false
            }

            return File(context.filesDir.absolutePath, filename).exists()
        }


        /**
         * download image by download manager
         */
        fun downloadImageTwo(context: Context, address: String, br: BroadcastReceiver): Long {
            val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(address)
            val request = DownloadManager.Request(uri)
            request.setTitle("다운로드")
            request.setDescription("이미지 다운로드")
            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            val networkId = dm.enqueue(request)

            val filter = IntentFilter()
            filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            context.registerReceiver(br, filter)
            return networkId
        }

        /**
         * monitor network state around all API Lewvel
         */
        fun registerNetworkStateListener(context: Context) {
            val intentFilter = IntentFilter()
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            context.registerReceiver(networkStateReceiver, intentFilter)
        }

        /**
         * monitor network state API Level over 21(Android 5.0)
         */
        fun registerNetworkStateCallback(context: Context) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val builder = NetworkRequest.Builder()
                builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                cm.registerNetworkCallback(builder.build(), networkStateCallback)
            }
        }

        /**
         * Connection Type
         */
        fun getConnectionType(context: Context): String {
            var type = ""
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            if (networkInfo != null) {
                if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                    type = "wifi"
                } else if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                    type = "mobile"
                }
            } else {

            }
            return type
        }


    }


}
