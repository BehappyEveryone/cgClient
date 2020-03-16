package com.example.chatground2.adapter.holder

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.chatground2.Api.IpAddress
import com.example.chatground2.R
import com.example.chatground2.model.DTO.ChatUserDto
import com.example.chatground2.model.DTO.ForumDto
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_chat_drawer.view.*
import kotlinx.android.synthetic.main.item_forums.view.*
import java.lang.Exception
import java.text.DateFormat

class ChatUserViewHolder(
    val context: Context, private val listenerFunc: ((Int) -> Unit)?,
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val profile = itemView.CDI_profile
    private val nickname = itemView.CDI_nickname
    private val opinion = itemView.CDI_opinion

    fun onBind(items: ArrayList<ChatUserDto>, position: Int) {
        items[position].let {
            nickname.text = it.nickname

            if (it.profile.isNullOrEmpty()) {
                profile.background = ContextCompat.getDrawable(context, R.drawable.profile_icon)
            } else {
                Picasso.get()
                    .load(IpAddress.BaseURL + it.profile)
                    .into(profile, object : Callback {
                        override fun onSuccess() {

                        }

                        override fun onError(e: Exception?) {
                            println("이미지 로드 에러 : $e")
                            profile.background =
                                ContextCompat.getDrawable(context, R.drawable.profile_icon)
                        }
                    })
            }

            if (it.opinion == null) {
                opinion.visibility = View.INVISIBLE
            } else {
                opinion.visibility = View.VISIBLE
                it.opinion?.let { boolean ->
                    if (boolean) {
                        opinion.text = "찬"
                        opinion.background =
                            ContextCompat.getDrawable(context, R.drawable.chat_drawer_agree)
                    } else {
                        opinion.text = "반"
                        opinion.background =
                            ContextCompat.getDrawable(context, R.drawable.chat_drawer_oppose)
                    }
                }
            }
        }
        itemView.setOnClickListener {
            listenerFunc?.invoke(position)
        }
    }
}