package network.kotlin.flow9.net.networkbasic.util

import android.sax.RootElement
import android.sax.StartElementListener
import android.util.Log
import android.util.Xml

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.Attributes
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import org.xml.sax.XMLReader
import org.xml.sax.helpers.DefaultHandler
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory

import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.StringReader
import java.io.UnsupportedEncodingException

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory

class Parser {

    companion object {

        var XML =
                "<order>" +
                "<good>Laptop</good>" +
                "</order>"

        var XML2 =
                "<order>" +
                "<good maker=\"Orion\" price=\"1000\">Choco</good>" +
                "<good maker=\"Lotte\" price=\"2000\">Pie</good>" +
                "<good maker=\"Crown\" price=\"3000\">Sand</good>" +
                "</order>"

        fun DomParseSingle(xml: String): String {
            var itemName = ""
            try {
                val factory = DocumentBuilderFactory.newInstance()
                val builder = factory.newDocumentBuilder()
                val inputStream = ByteArrayInputStream(xml.toByteArray(charset("utf-8")))
                val doc = builder.parse(inputStream)
                val order = doc.documentElement
                val items = order.getElementsByTagName("good")
                val item = items.item(0)
                val text = item.firstChild
                itemName = text.nodeValue
            } catch (e: ParserConfigurationException) {

            } catch (e: UnsupportedEncodingException) {

            } catch (e: SAXException) {

            } catch (e: IOException) {

            }
            return itemName
        }

        fun DomParseMultiple(xml: String): String {
            var result = ""
            try {
                val factory = DocumentBuilderFactory.newInstance()
                val builder = factory.newDocumentBuilder()
                val inputStream = ByteArrayInputStream(xml.toByteArray(charset("utf-8")))
                val doc = builder.parse(inputStream)
                val order = doc.documentElement
                val items = order.getElementsByTagName("good")
                for (i in 0 until items.length) {
                    val item = items.item(i)
                    val text = item.firstChild
                    val itemName = text.nodeValue
                    result += itemName + " : "

                    val attrs = item.attributes
                    for (j in 0 until attrs.length) {
                        val attr = attrs.item(j)
                        result += attr.nodeName + " = " + attr.nodeValue + " "
                    }
                    result += "\n"
                }
            } catch (e: ParserConfigurationException) {

            } catch (e: UnsupportedEncodingException) {

            } catch (e: SAXException) {

            } catch (e: IOException) {

            }
            return result
        }

        fun SAXParse(xml: String): String {
            var result = ""
            try {
                val factory = SAXParserFactory.newInstance()
                val parser = factory.newSAXParser()
                val reader = parser.xmlReader
                val handler = SAXParseHandler()
                reader.contentHandler = handler
                val inputStream = ByteArrayInputStream(xml.toByteArray(charset("utf-8")))
                reader.parse(InputSource(inputStream))
                result += handler.item
            } catch (e: ParserConfigurationException) {

            } catch (e: SAXException) {

            } catch (e: UnsupportedEncodingException) {

            } catch (e: IOException) {

            }

            return result
        }

        class SAXParseHandler : DefaultHandler() {

            var initem = false
            var item = StringBuilder()

            @Throws(SAXException::class)
            override fun startDocument() {

            }

            @Throws(SAXException::class)
            override fun endDocument() {

            }

            @Throws(SAXException::class)
            override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {

            }

            @Throws(SAXException::class)
            override fun endElement(uri: String, localName: String, qName: String) {

            }

            @Throws(SAXException::class)
            override fun characters(ch: CharArray, start: Int, length: Int) {

            }
        }

        fun SAXParseTwo(xml: String) {
            val root = RootElement("current")
            val cityElement = root.getChild("city")
            val tempElement = root.getChild("temperature")

            cityElement.setStartElementListener { attributes -> Log.d("SAXParseTwo", attributes.getValue("name")) }
            tempElement.setStartElementListener { attributes -> Log.d("SAXParseTwo", attributes.getValue("name")) }

            try {
                val `is` = ByteArrayInputStream(xml.toByteArray(charset("utf-8")))
                Xml.parse(`is`, Xml.Encoding.UTF_8, root.contentHandler)
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            } catch (e: SAXException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        fun XMLPullParse(xml: String): String {
            var result = ""
            var initem = false
            var itemName = ""
            try {
                val factory = XmlPullParserFactory.newInstance()
                val parser = factory.newPullParser()
                parser.setInput(StringReader(xml))
                // 현재 사건을 조사하며 next 메서드로 다음 사건을 조사하면서 문서를 처음부터 순회한다
                var eventType = parser.eventType
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    when (eventType) {
                        XmlPullParser.START_DOCUMENT -> {
                        }
                        XmlPullParser.END_DOCUMENT -> {
                        }
                        XmlPullParser.START_TAG ->
                            // 최초 문서 시작 이벤트에서 시작하여 문서가 끝날 때까지 태그나 텍스트를 만나면 각 사건마다 getName
                            // getText 메서드로 태그 및 텍스트 내용을 조사하여 원하는 정보를 추출한
                            if (parser.name == "item") {
                                initem = true
                            }
                        XmlPullParser.END_TAG -> {
                        }
                        XmlPullParser.TEXT -> if (initem) {
                            itemName = parser.text
                            initem = false
                        }
                    }
                    eventType = parser.next()
                }
                result += itemName
            } catch (e: XmlPullParserException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return result
        }

        fun XMLPullParseTwo(xml: String) {

            try {
                val `is` = ByteArrayInputStream(xml.toByteArray(charset("utf-8")))
                val parser = Xml.newPullParser()
                parser.setInput(`is`, null)
                var eventType = parser.eventType
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    var name: String? = null
                    if (eventType == XmlPullParser.START_TAG) {
                        name = parser.name
                        if (name == "city") {

                        } else if (name == "temperature") {

                        }
                    }
                    eventType = parser.next()

                }
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            } catch (e: XmlPullParserException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        fun JsonParseArray(json: String): String {
            var result = ""
            try {
                var sum = 0
                val jsonArray = JSONArray(json)
                for (i in 0 until jsonArray.length()) {
                    sum += jsonArray.getInt(i)
                }
                result += sum.toString() + ""
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return result
        }

        fun JsonParseSimple(json: String): String {
            var result = ""
            try {
                val jsonArray = JSONArray(json)
                for (i in 0 until jsonArray.length()) {
                    val order = jsonArray.getJSONObject(i)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return result
        }
    }

}
