package com.example.chatground2.view.gameReady

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.chatground2.Api.SocketIo

import com.example.chatground2.R
import com.example.chatground2.service.SocketService
import io.socket.client.Socket
import kotlinx.android.synthetic.main.fragment_game_ready.view.*

class GameReadyFragment : Fragment(),View.OnClickListener {

    private var socketService : SocketService? = null

    private val mConnection:ServiceConnection = object : ServiceConnection
    {
        override fun onServiceDisconnected(name: ComponentName?) {
            println("서비스 끊김")
            socketService = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            println("서비스 연결")

            val mBinder = service as SocketService.SocketBinder
            socketService = mBinder.getService()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_game_ready, container, false)

        return uiInitialize(view)
    }

//    override fun onResume() {
//        super.onResume()
//
//        context?.let { LocalBroadcastManager.getInstance(it).registerReceiver(receiver,intentFilter)}
//    }
//
//    override fun onPause() {
//        super.onPause()
//
//        context?.let { LocalBroadcastManager.getInstance(it).unregisterReceiver(receiver)}
//    }

    private fun initialize() {
        val intent = Intent(context, SocketService::class.java)
        context?.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
//        intentFilter.addAction("onConnect")
//        intentFilter.addAction("onMakeRoom")
    }

    private fun uiInitialize(view: View): View {
        view.run {

            GR_ready.setOnClickListener(this@GameReadyFragment)

            return this
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        context?.unbindService(mConnection)
    }

//    private val receiver:BroadcastReceiver = object : BroadcastReceiver()
//    {
//        override fun onReceive(context: Context?, intent: Intent?) {
//
//        }
//    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.GR_ready -> {
                if(socketService?.isConnect()!!)
                {
                    println("1")
                    socketService?.disconnectSocket()
                }else
                {
                    println("2")
                    socketService?.connectSocket()
                }
            }
        }
    }
}