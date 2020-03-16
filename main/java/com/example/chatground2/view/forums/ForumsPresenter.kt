package com.example.chatground2.view.forums

import android.content.Context
import com.example.chatground2.model.DAO.Model
import com.example.chatground2.model.DTO.ForumDto
import com.example.chatground2.R
import com.example.chatground2.adapter.adapterContract.ForumsAdapterContract
import kotlin.collections.ArrayList

class ForumsPresenter(private val context: Context, val view: ForumsContract.IForumsView) :
    ForumsContract.IForumsPresenter, ForumsContract.Listener {

    private var model: Model = Model(context)

    private var isBestForum = false
    private var pageNum = 0
    private var isRefresh = false//콜백받을 때 clear 유무

    override var adapterModel: ForumsAdapterContract.Model? = null
    override var adapterView: ForumsAdapterContract.View? = null
        set(value) {//커스텀 접근자
            field = value
            field?.onClickFunc = { clickForum(it) }
        }

    override fun callForums() {
        view.isLoading(false)
        view.progressVisible(true)
        pageNum++

        val hashMap = HashMap<String, Any>()
        hashMap["page"] = pageNum
        hashMap["isBestForum"] = isBestForum
        model.callForums(hashMap, this)
    }

    override fun refresh() {
        pageNum = 0
        isRefresh = true
        view.isRefreshing(true)
        callForums()
    }

    override fun writeClick() {
        view.enterWriteForum()
    }

    override fun bestForumsClick() {
        if(isBestForum)
        {
            isBestForum = false
            view.setBestForumBackground(R.drawable.forums_bestforums_icon)
        }else
        {
            isBestForum = true
            view.setBestForumBackground(R.drawable.forumsitem_recommend)
        }
        pageNum = 0
        isRefresh = true
        callForums()
    }

    override fun searchClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun searchCancelClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun searching(text: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun clickForum(position: Int) {
        view.enterDetailForum(adapterModel?.getItem(position)?.idx)
    }

    override fun onCallForumsSuccess(result: ArrayList<ForumDto>?) {
        view.progressVisible(false)
        view.isRefreshing(false)
        if (isRefresh) {
            adapterModel?.clearItems()
            isRefresh = false
        }

        result?.let {
            view.isLoading(true)
            adapterModel?.addItems(it)
            adapterView?.notifyAdapter()
        }
    }

    override fun onCallForumsFailure() {
        view.progressVisible(false)
        view.toastMessage("마지막 글입니다.")
        view.isRefreshing(false)
    }

    override fun onFailure() {
        view.progressVisible(false)
        view.isRefreshing(false)
        view.toastMessage("실패. 다시 시도해주세요")
    }
}