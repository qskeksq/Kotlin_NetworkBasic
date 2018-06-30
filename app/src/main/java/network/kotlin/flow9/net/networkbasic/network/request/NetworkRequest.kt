package network.kotlin.flow9.net.networkbasic.network.request

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

import network.kotlin.flow9.net.networkbasic.network.NetworkManager

abstract class NetworkRequest<T> : Runnable {
    private var manager: NetworkManager? = null
    private var mListener: NetworkManager.OnResultListener<*>? = null
    private val connectionTimeout = DEFAULT_TIMEOUT
    private val readTimeout = DEFAULT_TIMEOUT
    private var mConn: HttpURLConnection? = null

    private var result: T? = null
    private val errorCode = -1
    private val retry = 3

    var isCanceled = false
        private set

    /**
     * 데이터를 얻어오기 위한 URL
     */
    abstract val url: URL

    val requestMethod: String
        get() = METHOD_GET

    /**
     * 얻어온 데이터를 처리하는 메소드
     */
    @Throws(IOException::class)
    abstract fun parse(`is`: InputStream): T

    fun setOnResultListener(listener: NetworkManager.OnResultListener<T>) {
        mListener = listener
    }

    fun setNetworkManager(manager: NetworkManager) {
        this.manager = manager
    }

    /**
     * 네트워크 요청시 서버로 전달할 값을 정의하기 위한 메소드
     */
    open fun setRequestHeader(conn: HttpURLConnection) {}

    fun setConfiguration(conn: HttpURLConnection) {}

    fun writeOutput(conn: OutputStream) {}


    /**
     * 처리 결과를 요청한 객체에 전달하기 위한 처리를 해 주는 메소드
     */
    fun sendSuccess() {
        if (!isCanceled) {
            if (mListener != null) {
                mListener!!.onSuccess(this, result)
            }
        } else {
            manager!!.processCancel(this)
        }

    }

    fun sendFail() {
        if (!isCanceled) {
            if (mListener != null) {
                mListener!!.onFail(this, errorCode)
            }
        } else {
            manager!!.processCancel(this)
        }

    }

    /**
     * 취소
     */
    fun setCancel() {
        isCanceled = true
        if (mConn != null) {
            mConn!!.disconnect()
        }
        manager!!.processCancel(this)
    }

    override fun run() {
        var retryCount = retry
        while (retryCount > 0 && !isCanceled) {
            try {
                val url = url
                val conn = url.openConnection() as HttpURLConnection
                conn.connectTimeout = connectionTimeout
                conn.readTimeout = readTimeout
                val requestMethod = requestMethod
                if (requestMethod == METHOD_POST) {
                    conn.doOutput = true
                }
                conn.requestMethod = requestMethod
                setConfiguration(conn)
                setRequestHeader(conn)
                if (conn.doOutput) {
                    writeOutput(conn.outputStream)
                }
                if (isCanceled) continue
                val responseCode = conn.responseCode
                if (isCanceled) continue
                mConn = conn
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val `is` = conn.inputStream
                    result = parse(`is`)
                    manager!!.sendSuccess(this)
                    return
                } else {
                    retryCount = 0
                }
            } catch (e: MalformedURLException) {
                retryCount = 0
            } catch (e: IOException) {
                e.printStackTrace()
                retryCount--
            }

            manager!!.sendFail(this)
        }
    }

    companion object {

        private val METHOD_GET = "GET"
        private val METHOD_POST = "POST"
        private val DEFAULT_TIMEOUT = 10000
    }
}
