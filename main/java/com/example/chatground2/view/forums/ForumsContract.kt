package com.example.chatground2.view.forums

import com.example.chatground2.model.dto.ForumDto
import com.example.chatground2.adapter.adapterContract.ForumsAdapterContract

interface ForumsContract {
    interface IForumsPresenter{
        var adapterModel: ForumsAdapterContract.Model?
        var adapterView: ForumsAdapterContract.View?
        fun callForums()
        fun callForums(searchSpinner:String,searchText:String)
        fun refresh()
        fun writeClick()
        fun searchClick()
        fun bestForumsClick()
        fun searching(text:String)
    }

    interface IForumsView{
        fun toastMessage(text:String)
        fun progressVisible(boolean: Boolean)
        fun isRefreshing(boolean: Boolean)
        fun isLoading(boolean: Boolean)
        fun searchVisible(boolean: Boolean)
        fun enterWriteForum()
        fun enterDetailForum(idx:Int?)
        fun setBestForumBackground(int:Int)
        fun setSearchBackground(int: Int)
        fun getSearchSpinner():String
    }

    interface Listener
    {
        fun onCallForumsSuccess(result:ArrayList<ForumDto>?)
        fun onCallForumsFailure()
        fun onFailure()
    }
}