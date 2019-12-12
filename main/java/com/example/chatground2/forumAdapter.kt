package com.example.chatground2

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatground2.model.chatitemdata
import kotlinx.android.synthetic.main.activity_chatimageitem.view.*
import kotlinx.android.synthetic.main.activity_chattextitem.view.*
import kotlinx.android.synthetic.main.activity_chatvideoitem.view.*
import android.provider.MediaStore
import android.media.ThumbnailUtils
import android.R.attr.bitmap
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.R.attr.start
import android.app.Activity
import android.media.MediaPlayer
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.content.FileProvider
import com.example.chatground2.model.forumitemdata
import com.example.chatground2.view.MainActivity
import kotlinx.android.synthetic.main.forumsitem.view.*
import java.io.File


class forumAdapter(val context: Context, List: ArrayList<forumitemdata>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items:ArrayList<forumitemdata> = List

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =  LayoutInflater.from(context).inflate(R.layout.forumsitem, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val FI_card = itemView.forumcard
        val FI_subject = itemView.FI_subject
        val FI_title = itemView.FI_title
        val FI_nickname = itemView.FI_nickname
        val FI_comment = itemView.FI_comment
        val FI_datetext = itemView.FI_datetext
        val FI_recommendtext = itemView.FI_recommendtext
        val FI_Image = itemView.FI_Image
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as ViewHolder)
        holder.FI_subject.text = "[${item.subject}]"
        holder.FI_title.text = item.title
        holder.FI_datetext.text = item.date
        holder.FI_comment.text = item.commentNum.toString()
        holder.FI_nickname.text = item.nickname
        holder.FI_recommendtext.text = item.recommend.toString()

        if(item.ImageNum == 0)
        {
            holder.FI_Image.visibility = View.GONE
        }
        else
        {
            holder.FI_Image.visibility = View.VISIBLE
        }

        holder.FI_card.setOnClickListener {
            val intent:Intent = Intent(context, readforum::class.java)
            val WRITEFORUMCODE = 500
            intent.putExtra("idx",item.idx)
            intent.putExtra("Email",item.Email)
            (context as Activity).startActivityForResult(intent,WRITEFORUMCODE)
        }
    }
}