package com.example.chatground2.adapter.holder

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatground2.Api.IpAddress
import com.example.chatground2.R
import com.example.chatground2.model.Constants
import com.example.chatground2.model.DTO.ChatDto
import com.example.chatground2.model.DTO.ForumDto
import com.example.chatground2.model.DTO.UserDto
import com.google.gson.Gson
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_chat_text_right.view.*
import kotlinx.android.synthetic.main.item_forums.view.*
import java.lang.Exception
import java.text.DateFormat

class ChatTextRightViewHolder(
    val context: Context,
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val sp: SharedPreferences =
        context.getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE)
    private val gson = Gson()

    private val ctrContent = itemView.CTR_content
    private val ctrDate = itemView.CTR_date


    fun onBind(items: ArrayList<ChatDto>, position: Int) {
        items[position].let {
            ctrContent.text = it.content
            ctrDate.text = DateFormat.getDateInstance(DateFormat.LONG).format(it.date)
        }
    }

    private fun getUser(): UserDto {
        val json = sp.getString("User", "")
        return gson.fromJson(json, UserDto::class.java)
    }
}