package com.example.chatground2.view.mainActivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class MainPresenter(
    private val context: Context,
    val view: MainContract.IMainView
) : MainContract.IMainPresenter {

    private val intentFilter: IntentFilter = IntentFilter()

    override fun setBroadCastReceiver() {
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter)
    }

    override fun removeBroadCastReceiver() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
    }

    init {
        //받을 브로드캐스트 리시버
        intentFilter.addAction("onMakeRoom")
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "onMakeRoom") {
                println("피시방가자 onMakeRoom")
            }
        }
    }
}