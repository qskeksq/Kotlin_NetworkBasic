package network.kotlin.flow9.net.networkbasic.network

import android.os.Handler
import android.os.Looper
import android.os.Message

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

import network.kotlin.flow9.net.networkbasic.network.request.NetworkRequest

class NetworkManager {
    // ThreadPool
    private val mExecutor: ThreadPoolExecutor
    private val mRequestQueue = LinkedBlockingQueue<Runnable>()

    /**
     * 메인 스레드로 네트워크 처리 결과를 전달하기 위한 handler
     */
    private val mHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val request = msg.obj as NetworkRequest<*>
            when (msg.what) {
                MESSAGE_SEND_SUCCESS -> request.sendSuccess()
                MESSAGE_FAIL -> request.sendFail()
            }
        }
    }

    init {
        mExecutor = ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME.toLong(),
                TimeUnit.MILLISECONDS,
                mRequestQueue
        )
    }

    fun sendSuccess(request: NetworkRequest<*>) {
        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SEND_SUCCESS, request))
    }

    fun sendFail(request: NetworkRequest<*>) {
        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, request))
    }

    /**
     * 처리 결과를 전달하기 위한 리스너
     */
    interface OnResultListener<T> {
        fun onSuccess(request: NetworkRequest<T>, result: T)
        fun onFail(request: NetworkRequest<T>, code: Int)
    }

    /**
     * 별도의 스레드로 NetworkRequest 처리
     */
    fun <T> getNetworkData(request: NetworkRequest<T>, listener: OnResultListener<T>) {
        request.setOnResultListener(listener)
        getNetworkData(request)
    }

    fun <T> getNetworkData(request: NetworkRequest<T>) {
        request.setNetworkManager(this)
        mExecutor.execute(request)
    }

    /**
     * 취소
     */
    fun processCancel(request: NetworkRequest<*>) {
        mRequestQueue.remove(request)
    }

    companion object {

        private var inst: NetworkManager? = null
        private val CORE_POOL_SIZE = 5
        private val MAXIMUM_POOL_SIZE = 64
        private val KEEP_ALIVE_TIME = 5000
        // Callback Message
        val MESSAGE_SEND_SUCCESS = 1
        val MESSAGE_FAIL = 2

        fun getInstance(): NetworkManager {
            if (inst == null) {
                inst = NetworkManager()
            }
            return inst as NetworkManager
        }
    }

}
