package com.example.chatground2.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.chatground2.Api.SocketIo
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException


class SocketService:Service() {

    var mBinder:IBinder = SocketBinder()


    class SocketBinder:Binder(){
        fun getService() : SocketService{ // 서비스 객체를 리턴
            return SocketService()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()

        SocketIo.mSocket.on(Socket.EVENT_CONNECT, onConnect)
        SocketIo.mSocket.on("makeRoom", onMakeRoom)
    }

    override fun onDestroy() {
        super.onDestroy()

        SocketIo.mSocket.off(Socket.EVENT_CONNECT, onConnect)
        SocketIo.mSocket.off("makeRoom", onMakeRoom)
    }

    fun isConnect():Boolean = SocketIo.mSocket.connected()

    fun connectSocket()
    {
        println("소켓연결함")
        try {
            if(!SocketIo.mSocket.connected())
            {
                SocketIo.mSocket.connect()
            }
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }
    fun disconnectSocket()
    {
        println("소켓연결해제함")
        SocketIo.mSocket.disconnect()
    }

    private val onMakeRoom = Emitter.Listener {
        val intent:Intent = Intent("onMakeRoom")
        intent.putExtra("value",it)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private val onConnect = Emitter.Listener {
        //커넥트
        val intent:Intent = Intent("onConnect")
        intent.putExtra("onConnectValue",it)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}