package network.kotlin.flow9.net.networkbasic.network.request

import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

import network.kotlin.flow9.net.networkbasic.domain.Melon

class MelonRequest(private val page: Int, private val count: Int) : NetworkRequest<Melon>() {

    override// String urlText = MELON_CHART_URL + String.format(MELON_CHART_PARAMS, VERSION, page, count);
    val url: URL
        @Throws(MalformedURLException::class)
        get() = URL("")

    override fun setRequestHeader(conn: HttpURLConnection) {
        super.setRequestHeader(conn)
        conn.setRequestProperty("Accept", "" /* HEADER_ACCEPT_JSON */)
        conn.setRequestProperty("appKey", "" /* HEADER_APPKEY */)
    }

    @Throws(IOException::class)
    override fun parse(`is`: InputStream): Melon? {
        // Gson gson = new Gson();
        // MelonResult result = gson.fromJson(new InputStreamReader(is), MelonResult.class);
        return null
    }


}
