package com.example.chatground2.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatground2.R
import com.example.chatground2.adapter.adapterContract.ChatAdapterContract
import com.example.chatground2.adapter.holder.ChatSystemViewHolder
import com.example.chatground2.adapter.holder.ChatTextLeftViewHolder
import com.example.chatground2.adapter.holder.ChatTextRightViewHolder
import com.example.chatground2.model.Constants
import com.example.chatground2.model.DTO.ChatDto
import com.example.chatground2.model.DTO.UserDto
import com.google.gson.Gson

class ChatAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ChatAdapterContract.Model, ChatAdapterContract.View {

    private val sp: SharedPreferences =
        context.getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE)
    private val gson = Gson()

    override var onClickFunc: ((Int) -> Unit)? = null

    override fun getItem(position: Int): ChatDto = items[position]

    override fun getItemSize(): Int = itemCount

    val items: ArrayList<ChatDto> = ArrayList()

    override fun getItemViewType(position: Int): Int {
        return when (items[position].type) {
            "system" -> 0
            "text" -> {
                if (items[position].user?._id == getUser()._id) {
                    1//right
                } else {
                    2//left
                }
            }
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_system, parent, false)
                ChatSystemViewHolder(context, view)
            }
            1 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_text_right, parent, false)
                ChatTextRightViewHolder(context, view)
            }
            2 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_text_left, parent, false)
                ChatTextLeftViewHolder(context, view)
            }
            else -> throw RuntimeException("알 수 없는 뷰 타입 에러")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (items[position].type) {
            "system" -> {
                (holder as ChatSystemViewHolder)
                holder.onBind(items, position)
            }
            "text" -> {
                if (items[position].user?._id == getUser()._id) {
                    1//right
                    (holder as ChatTextRightViewHolder)
                    holder.onBind(items, position)
                } else {
                    2//left
                    (holder as ChatTextLeftViewHolder)
                    holder.onBind(items, position)
                }
            }
            else -> -1
        }
    }

    override fun getItemCount(): Int = items.size

    override fun notifyAdapter() = notifyDataSetChanged()

    override fun addItems(chatItems: ArrayList<ChatDto>) {
        this.items.addAll(chatItems)
    }

    override fun addItem(chatItem: ChatDto) {
        this.items.add(chatItem)
        println("채팅아이템 : ${this.items}")
    }

    override fun clearItems() {
        items.clear()
    }

    private fun getUser(): UserDto {
        val json = sp.getString("User", "")
        return gson.fromJson(json, UserDto::class.java)
    }
}