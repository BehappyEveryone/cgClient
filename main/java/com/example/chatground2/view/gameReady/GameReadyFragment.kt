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

class GameReadyFragment : Fragment(), View.OnClickListener, GameReadyContract.IGameReadyView {

    private var presenter: GameReadyPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()
        presenter?.bindService()
    }

    private fun initialize() {
        presenter = context?.let { GameReadyPresenter(it,this) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_game_ready, container, false)

        return uiInitialize(view)
    }

    private fun uiInitialize(view: View): View {
        view.run {

            GR_ready.setOnClickListener(this@GameReadyFragment)

            return this
        }
    }

    override fun onResume() {
        super.onResume()

        presenter?.setBroadCastReceiver()
    }

    override fun onPause() {
        super.onPause()

        presenter?.removeBroadCastReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter?.unbindService()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.GR_ready -> {
                if (presenter?.isSocketConnect()!!) {
                    presenter?.disconnectSocket()
                } else {
                    presenter?.connectSocket()
                }
            }
        }
    }
}