package com.example.chatground2.view.chatGround

import android.content.Intent
import com.example.chatground2.adapter.adapterContract.ChatAdapterContract
import com.example.chatground2.adapter.adapterContract.ChatUserAdapterContract

interface ChatGroundContract {
    interface IChatGroundPresenter{
        var adapterChatModel: ChatAdapterContract.Model?
        var adapterChatView: ChatAdapterContract.View?
        var adapterChatUserModel: ChatUserAdapterContract.Model?
        var adapterChatUserView: ChatUserAdapterContract.View?
        fun getIntent(intent: Intent)
        fun setBroadCastReceiver()
        fun removeBroadCastReceiver()
        fun bindService()
        fun unbindService()
        fun drawerClick()
        fun sendMessage(message:String)
        fun getMessages()
        fun removeMessages()
        fun setOpinion(boolean: Boolean)
    }

    interface IChatGroundView{
        fun openDrawer()
        fun toastMessage(text:String)
        fun setMessageClear()
        fun setChatScrollPosition(position:Int)
        fun setEnable(boolean: Boolean)
        fun setSubjectText(text: String)
        fun setTimeText(text: String)
        fun setOpinionVisible(boolean: Boolean)
        fun setAgreeButtonPressed(boolean: Boolean)
        fun getAgreeButtonPressed():Boolean
        fun setOpposeButtonPressed(boolean: Boolean)
        fun getOpposeButtonPressed():Boolean
    }
}