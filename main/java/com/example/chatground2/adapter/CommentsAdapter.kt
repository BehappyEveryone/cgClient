package com.example.chatground2.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatground2.Model.DTO.CommentDto
import com.example.chatground2.Model.DTO.ForumDto
import com.example.chatground2.R
import com.example.chatground2.adapter.adapterContract.CommentsAdapterContract
import com.example.chatground2.adapter.adapterContract.ForumsAdapterContract
import com.example.chatground2.adapter.holder.CommentsViewHolder
import com.example.chatground2.adapter.holder.ForumsViewHolder

class CommentsAdapter (val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), CommentsAdapterContract.Model,
    CommentsAdapterContract.View {

    override fun getItem(position: Int): CommentDto = items[position]

    override var onReplyCameraClick: ((String?) -> Unit)? = null
    override var onReplySendClick: ((Int, String) -> Unit)? = null
    private val onDataSetChanged: ((Int) -> Unit)? = {
        notifyItemChanged(it)
    }

    private var replyImagePath:String? = null

    val items: ArrayList<CommentDto> = ArrayList()
    private val isBottomVisible:ArrayList<Boolean> = ArrayList()

    override fun setReplyImagePathString(path: String?) {
        replyImagePath = path
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CommentsViewHolder)
        holder.onBind(items,isBottomVisible, position,replyImagePath)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return CommentsViewHolder(context,onReplySendClick,onReplyCameraClick,onDataSetChanged, view)
    }

    override fun getItemCount(): Int = items.size

    override fun notifyAdapter() = notifyDataSetChanged()

    override fun addItems(commentItems: ArrayList<CommentDto>) {
        this.items.addAll(commentItems)
        for(i in items.indices)
        {
            this.isBottomVisible[i] = false
        }
    }

    override fun clearItems() {
        items.clear()
        isBottomVisible.clear()
    }
}