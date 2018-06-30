package network.kotlin.flow9.net.networkbasic.socket

import android.util.Log

import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket

class SocketClient {

    fun start() {
        try {
            val port = 3001
            val socket = Socket("localhost", port)
            val oos = ObjectOutputStream(socket.getOutputStream())
            oos.writeObject("hello world")
            oos.flush()

            val ois = ObjectInputStream(socket.getInputStream())
            val obj = ois.readObject() as String
            Log.d("MainActivity", "서버에서 받은 메시지 : " + obj)
            socket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

    }

}
