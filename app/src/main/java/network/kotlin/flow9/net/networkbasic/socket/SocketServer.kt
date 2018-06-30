package network.kotlin.flow9.net.networkbasic.socket

import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

class SocketServer {

    fun start() {
        try {
            val port = 3001
            println("Starting Java Socket Server")
            val ss = ServerSocket(port)
            println("Listening at port $port...")

            while (true) {
                val socket = ss.accept()
                val clientHost = socket.localAddress
                val clientPort = socket.port
                println("Client Connected. host : $clientHost, port : $clientPort")

                val ois = ObjectInputStream(socket.getInputStream())
                val obj = ois.readObject()
                print("Input : " + obj)

                val oos = ObjectOutputStream(socket.getOutputStream())
                oos.writeObject(obj.toString() + " from server")
                oos.flush()
                socket.close()
                ss.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

    }

}
