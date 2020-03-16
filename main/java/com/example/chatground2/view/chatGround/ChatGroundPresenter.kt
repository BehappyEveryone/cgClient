package com.example.chatground2.view.chatGround

import android.content.*
import android.os.Handler
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.chatground2.Api.SocketIo.mSocket
import com.example.chatground2.adapter.adapterContract.ChatAdapterContract
import com.example.chatground2.adapter.adapterContract.ChatUserAdapterContract
import com.example.chatground2.adapter.adapterContract.CommentsAdapterContract
import com.example.chatground2.model.Constants
import com.example.chatground2.model.DTO.ChatDto
import com.example.chatground2.model.DTO.ChatUserDto
import com.example.chatground2.model.DTO.UserDto
import com.example.chatground2.service.SocketService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class ChatGroundPresenter(
    private val context: Context,
    val view: ChatGroundContract.IChatGroundView
) : ChatGroundContract.IChatGroundPresenter {

    private var socketService: SocketService? = null
    private val intentFilter: IntentFilter = IntentFilter()
    private val sp: SharedPreferences =
        context.getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE)
    private val gson = Gson()

    override var adapterChatModel: ChatAdapterContract.Model? = null
    override var adapterChatView: ChatAdapterContract.View? = null
    override var adapterChatUserModel: ChatUserAdapterContract.Model? = null
    override var adapterChatUserView: ChatUserAdapterContract.View? = null

    init {
        //받을 브로드캐스트 리시버
        intentFilter.addAction("onMessage")
        intentFilter.addAction("onOfferSubject")
    }

    override fun getIntent(intent: Intent) {
        val users = JSONArray(intent.getStringExtra("users"))
        val arrayList = ArrayList<ChatUserDto>()
        for (i in 0 until users.length()) {
            arrayList.add(gson.fromJson(users[i].toString(), ChatUserDto::class.java))
        }
        adapterChatUserModel?.addItems(arrayList)
    }

    override fun bindService() {
        val intent = Intent(context, SocketService::class.java)
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun unbindService() {
        context.unbindService(mConnection)
    }

    override fun setBroadCastReceiver() {
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter)
    }

    override fun removeBroadCastReceiver() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
    }

    override fun drawerClick() {
        view.openDrawer()
    }

    override fun sendMessage(message: String) {
        if (message.isNotEmpty()) {
            val data: JSONObject =
                JSONObject().put("type", "text").put("content", message).put("user", getUser()._id)
                    .put("room", Constants.room)
            socketService?.socketEmit("sendMessage", data)

            view.setMessageClear()
        } else {
            view.toastMessage("내용을 입력해주세요!")
        }
    }

    override fun getMessages() {
        val arrayList = ArrayList<ChatDto>()
        if (sp.getString("message", null) != null) {
            println("메세지 : " + sp.getString("message", null))
            val jsonArray = JSONArray(sp.getString("message", null))
            for (i in 0 until jsonArray.length()) {
                arrayList.add(gson.fromJson(jsonArray[i].toString(), ChatDto::class.java))
            }
            adapterChatModel?.addItems(arrayList)
            adapterChatView?.notifyAdapter()
            adapterChatModel?.getItemSize()?.let { view.setChatScrollPosition(it - 1) }
        }
    }

    override fun removeMessages() {
        adapterChatModel?.clearItems()
    }

    override fun setOpinion(boolean: Boolean) {
        if (boolean) {
            if (view.getAgreeButtonPressed()) {
                Constants.opinion = "neutrality"
                view.setAgreeButtonPressed(false)
                view.setOpposeButtonPressed(false)
            } else {
                Constants.opinion = "agree"
                view.setAgreeButtonPressed(true)
                view.setOpposeButtonPressed(false)
            }
        } else {
            if (view.getOpposeButtonPressed()) {
                Constants.opinion = "neutrality"
                view.setAgreeButtonPressed(false)
                view.setOpposeButtonPressed(false)
            } else {
                Constants.opinion = "oppose"
                view.setAgreeButtonPressed(false)
                view.setOpposeButtonPressed(true)
            }
        }
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "onMessage") {
                adapterChatModel?.addItem(
                    gson.fromJson(
                        intent.getStringExtra("onMessageValue"),
                        ChatDto::class.java
                    )
                )
                adapterChatView?.notifyAdapter()
                adapterChatModel?.getItemSize()?.let { view.setChatScrollPosition(it - 1) }
            }

            if (intent?.action == "onOfferSubject") {
                view.setEnable(false)
                if (intent.hasExtra("subject")) {
                    view.setSubjectText(intent.getStringExtra("subject"))
                }
                if (intent.hasExtra("time")) {
                    view.setOpinionVisible(true)
                    val time = intent.getIntExtra("time", -1)
                    when {
                        time == 0 -> view.setOpinionVisible(false)
                        time < 10 -> view.setTimeText("00:0$time")
                        else -> view.setTimeText("00:$time")
                    }
                }
            }
        }
    }

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            println("ChatGround 서비스 끊김")
            socketService = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            println("ChatGround 서비스 연결")

            val mBinder = service as SocketService.SocketBinder
            socketService = mBinder.getService()

            val data: JSONObject = JSONObject().put("room", Constants.room)
            socketService?.socketEmit("onMakeRoom", data)
        }
    }

    private fun getUser(): UserDto {
        val json = sp.getString("User", "")
        return gson.fromJson(json, UserDto::class.java)
    }
}