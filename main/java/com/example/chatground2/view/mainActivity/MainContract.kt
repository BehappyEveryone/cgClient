package com.example.chatground2.view.mainActivity

interface MainContract {
    interface IMainPresenter{
        fun setBroadCastReceiver()
        fun removeBroadCastReceiver()
    }

    interface IMainView{

    }
}