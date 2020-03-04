package com.example.chatground2.adapter.adapterContract

import com.example.chatground2.Model.DTO.CommentDto

interface CommentsAdapterContract {
    interface View {
        var onReplySendClick:((Int,String) -> Unit)?
        var onReplyCameraClick:((String?) -> Unit)?

        fun notifyAdapter()
    }

    interface Model {
        fun setReplyImagePathString(path:String?)
        fun addItems(commentItems:ArrayList<CommentDto>)
        fun clearItems()
        fun getItem(position:Int): CommentDto
    }
}