package com.example.chatground2.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.chatground2.Api.SocketIo
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.net.URISyntaxException
import android.preference.PreferenceManager
import android.content.SharedPreferences
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.example.chatground2.model.Constants
import com.example.chatground2.model.DTO.UserDto
import com.example.chatground2.view.chatGround.ChatGroundActivity
import com.google.gson.Gson
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.example.chatground2.model.DTO.ChatDto
import com.example.chatground2.model.DTO.ChatUserDto
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray


class SocketService : Service() {

    var mBinder: IBinder = SocketBinder()

    private var preferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var gson: Gson = Gson()

//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        return START_NOT_STICKY
//    }

    class SocketBinder : Binder() {
        fun getService(): SocketService { // 서비스 객체를 리턴
            return SocketService()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()

        initialize()

        SocketIo.mSocket.on(Socket.EVENT_CONNECT, onConnect)
        SocketIo.mSocket.on("makeRoom", onMakeRoom)
        SocketIo.mSocket.on("matchMaking", onMatchMaking)
        SocketIo.mSocket.on("message", onMessage)
        SocketIo.mSocket.on("roomInfoChange", onRoomInfoChange)
        SocketIo.mSocket.on("offerSubject", onOfferSubject)
    }

    private fun initialize() {
        editor = preferences?.edit()
        editor?.apply()
        preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE)
    }

    override fun onDestroy() {
        super.onDestroy()

        SocketIo.mSocket.off(Socket.EVENT_CONNECT, onConnect)
        SocketIo.mSocket.off("makeRoom", onMakeRoom)
        SocketIo.mSocket.off("matchMaking", onMatchMaking)
        SocketIo.mSocket.off("message", onMessage)
        SocketIo.mSocket.off("roomInfoChange", onRoomInfoChange)
        SocketIo.mSocket.off("offerSubject", onOfferSubject)
    }

    fun isConnect(): Boolean = SocketIo.mSocket.connected()

    fun connectSocket() {
        println("소켓연결시도")
        try {
            if (!SocketIo.mSocket.connected()) {
                SocketIo.mSocket.connect()
            }
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun disconnectSocket() {
        println("소켓연결해제시도")
        SocketIo.mSocket.disconnect()
    }

    private val onMatchMaking = Emitter.Listener {
        val receivedData = it[0] as JSONObject

        val data: JSONObject =
            JSONObject().put("room", receivedData["room"]).put("user", getUser()._id)
        SocketIo.mSocket.emit("join", data)
    }

    private val onMakeRoom = Emitter.Listener {
        val receivedData = it[0] as JSONObject

        Constants.room = receivedData["room"].toString()
        val chatIntent: Intent = Intent(this, ChatGroundActivity::class.java)
        chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        chatIntent.putExtra("users", receivedData.getJSONArray("users").toString())
        startActivity(chatIntent)
    }

    private val onRoomInfoChange = Emitter.Listener {
        val receivedData = it[0] as JSONObject

    }

    private val onMessage = Emitter.Listener {
        val editor: SharedPreferences.Editor? = preferences?.edit()
        val receivedData = it[0] as JSONObject

        if (preferences?.getString("message", null) != null) {
            println("message is not null")
            val jsonArray = JSONArray(preferences?.getString("message", null))
            jsonArray.put(receivedData)
            editor?.putString("message", jsonArray.toString())
        } else {
            println("message is null")
            editor?.putString("message", JSONArray().put(receivedData).toString())
        }
        editor?.commit()

        val intent: Intent = Intent("onMessage")
        intent.putExtra("onMessageValue", receivedData.toString())
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private val onOfferSubject = Emitter.Listener {
        val receivedData = it[0] as JSONObject

        val subject = receivedData.get("subject").toString()
        val time = receivedData.get("time").toString()

        val intent: Intent = Intent("onOfferSubject")

        CoroutineScope(Dispatchers.Main).launch {
            for (i: Int in 0..subject.length) {
                intent.putExtra("subject", subject.substring(0, i))
                LocalBroadcastManager.getInstance(this@SocketService).sendBroadcast(intent)
                delay(100)
            }

            for (i: Int in time.toInt() / 1000 downTo 0) {
                intent.putExtra("time", i)
                LocalBroadcastManager.getInstance(this@SocketService).sendBroadcast(intent)
                delay(1000)//1000

                if (i == 0) {
                    val data: JSONObject =
                        JSONObject().put("user", getUser()._id).put("opinion", Constants.opinion)
                    socketEmit("AgreeOrOppose", data)
                }
            }
        }
    }

    private val onConnect = Emitter.Listener {
        //커넥트
        val intent: Intent = Intent("onConnect")
        intent.putExtra("onConnectValue", it)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun getUser(): UserDto {
        val json = preferences?.getString("User", "")
        return gson.fromJson(json, UserDto::class.java)
    }

    fun socketEmit(name: String, value: JSONObject) {
        SocketIo.mSocket.emit(name, value)
        println("emit : $name")
    }
}