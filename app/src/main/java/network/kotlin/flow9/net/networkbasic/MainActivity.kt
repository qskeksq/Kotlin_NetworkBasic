package network.kotlin.flow9.net.networkbasic

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

import network.kotlin.flow9.net.networkbasic.domain.Melon
import network.kotlin.flow9.net.networkbasic.network.NetworkManager
import network.kotlin.flow9.net.networkbasic.network.RequestFactory
import network.kotlin.flow9.net.networkbasic.network.request.NetworkRequest
import network.kotlin.flow9.net.networkbasic.util.NetworkUtil

class MainActivity : AppCompatActivity() {

    private var htmlStr: String? = null
    private var htmlText: TextView? = null
    private var downloadId: Long = 0
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            htmlText!!.text = htmlStr
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        htmlText = findViewById(R.id.htmltext)
    }

    private fun downloadHtml() {
        Thread(Runnable { htmlStr = NetworkUtil.downloadHTML(NetworkUtil.ADDRESS) }).start()
    }

    override fun onPause() {
        super.onPause()
        if (downloadId != 0L) {
            unregisterReceiver(NetworkUtil.downloadComplete)
        }
        downloadId = 0
    }

    fun getMelonChart() {
        val request = RequestFactory.melonRequest
//        NetworkManager.getInstance().getNetworkData(request, object : NetworkManager.OnResultListener<Melon> {
//            override fun onSuccess(request: NetworkRequest<Melon>, result: Melon) {
//
//            }
//
//            override fun onFail(request: NetworkRequest<Melon>, code: Int) {
//
//            }
//        })
    }

}
