package network.kotlin.flow9.net.networkbasic.rss

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log

import org.w3c.dom.DOMException
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

import network.kotlin.flow9.net.networkbasic.R

class RssActivity : AppCompatActivity() {

    private var progressDialog: ProgressDialog? = null
    private var handler = Handler()
    private var newsItemList: ArrayList<RSSNewsItem>? = null


    private var updateRSSRunnable: Runnable = Runnable {
        //            try {
        //
        //                Resources res = getResources();
        //                Drawable rssIcon = res.getDrawable(R.drawable.rss_icon);
        //                for (int i = 0; i < newsItemList.size(); i++) {
        //                    RSSNewsItem newsItem = (RSSNewsItem) newsItemList.get(i);
        //                    newsItem.setIcon(rssIcon);
        //                    adapter.addItem(newsItem);
        //                }
        //
        //                adapter.notifyDataSetChanged();
        //
        //                progressDialog.dismiss();
        //            } catch(Exception ex) {
        //                ex.printStackTrace();
        //            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rss)
    }

    private fun showRSS(urlStr: String) {
        try {
            progressDialog = ProgressDialog.show(this, "RSS Refresh", "RSS 정보 업데이트 중...", true, true)

            val thread = RefreshThread(urlStr)
            thread.start()

        } catch (e: Exception) {
            Log.e(TAG, "Error", e)
        }

    }

    private inner class RefreshThread(var urlStr: String) : Thread() {

        override fun run() {

            try {

                val builderFactory = DocumentBuilderFactory.newInstance()
                val builder = builderFactory.newDocumentBuilder()

                val urlForHttp = URL(urlStr)

                val instream = getInputStreamUsingHTTP(urlForHttp)
                val document = builder.parse(instream)
                val countItem = processDocument(document)
                Log.d(TAG, countItem.toString() + " news item processed.")

                // post for the display of fetched RSS info.
                handler.post(updateRSSRunnable)

            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }
    }

    @Throws(Exception::class)
    fun getInputStreamUsingHTTP(url: URL): InputStream {
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.doInput = true
        conn.doOutput = true

        val resCode = conn.responseCode
        Log.d(TAG, "Response Code : " + resCode)

        return conn.inputStream
    }

    /**
     * process DOM document for RSS
     *
     * @param doc
     */
    private fun processDocument(doc: Document): Int {
        newsItemList!!.clear()

        val docEle = doc.documentElement
        val nodelist = docEle.getElementsByTagName("item")
        var count = 0
        if (nodelist != null && nodelist.length > 0) {
            for (i in 0 until nodelist.length) {
                val newsItem = dissectNode(nodelist, i)
                if (newsItem != null) {
                    newsItemList!!.add(newsItem)
                    count++
                }
            }
        }

        return count
    }

    private fun dissectNode(nodelist: NodeList, index: Int): RSSNewsItem? {
        var newsItem: RSSNewsItem? = null

        try {
            val entry = nodelist.item(index) as Element

            val title = entry.getElementsByTagName("title").item(0) as Element
            val link = entry.getElementsByTagName("link").item(0) as Element
            val description = entry.getElementsByTagName("description").item(0) as Element

            var pubDataNode: NodeList? = entry.getElementsByTagName("pubDate")
            if (pubDataNode == null) {
                pubDataNode = entry.getElementsByTagName("dc:date")
            }
            val pubDate = pubDataNode!!.item(0) as Element

            val author = entry.getElementsByTagName("author").item(0) as Element
            val category = entry.getElementsByTagName("category").item(0) as Element

            var titleValue: String? = null
            if (title != null) {
                val firstChild = title.firstChild
                if (firstChild != null) {
                    titleValue = firstChild.nodeValue
                }
            }
            var linkValue: String? = null
            if (link != null) {
                val firstChild = link.firstChild
                if (firstChild != null) {
                    linkValue = firstChild.nodeValue
                }
            }

            var descriptionValue: String? = null
            if (description != null) {
                val firstChild = description.firstChild
                if (firstChild != null) {
                    descriptionValue = firstChild.nodeValue
                }
            }

            var pubDateValue: String? = null
            if (pubDate != null) {
                val firstChild = pubDate.firstChild
                if (firstChild != null) {
                    pubDateValue = firstChild.nodeValue
                }
            }

            var authorValue: String? = null
            if (author != null) {
                val firstChild = author.firstChild
                if (firstChild != null) {
                    authorValue = firstChild.nodeValue
                }
            }

            var categoryValue: String? = null
            if (category != null) {
                val firstChild = category.firstChild
                if (firstChild != null) {
                    categoryValue = firstChild.nodeValue
                }
            }

            Log.d(TAG, "item node : " + titleValue + ", " + linkValue + ", " + descriptionValue +
                    ", " + pubDateValue + ", " + authorValue + ", " + categoryValue)

            newsItem = RSSNewsItem(titleValue, linkValue, descriptionValue,
                    pubDateValue, authorValue, categoryValue)

        } catch (e: DOMException) {
            e.printStackTrace()
        }

        return newsItem
    }

    companion object {

        private val TAG = "MainActivity"

        private val rssUrl = "http://api.sbs.co.kr/xml/news/rss.jsp?pmDiv=entertainment"
    }

}
