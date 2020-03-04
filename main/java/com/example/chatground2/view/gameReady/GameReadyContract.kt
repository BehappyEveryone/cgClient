package com.example.chatground2.view.gameReady

import com.example.chatground2.Model.DTO.UserDto

interface GameReadyContract {
    interface IGameReadyPresenter{
        fun setBroadCastReceiver()
        fun removeBroadCastReceiver()
        fun bindService()
        fun unbindService()
        fun isSocketConnect(): Boolean?
        fun disconnectSocket()
        fun connectSocket()
    }

    interface IGameReadyView{

    }
}