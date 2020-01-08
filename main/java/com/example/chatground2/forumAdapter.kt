package com.example.chatground2

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.Activity
import android.content.Intent
import com.example.chatground2.model.forumitemdata
import kotlinx.android.synthetic.main.loadingitem.view.*
import kotlinx.android.synthetic.main.forumsitem.view.*


class forumAdapter(val context: Context, List: ArrayList<forumitemdata>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items:ArrayList<forumitemdata> = List

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
        return when (viewType) {
            0 -> {
                view = LayoutInflater.from(context).inflate(R.layout.forumsitem, parent, false)
                ViewHolder(view)
            }
            else -> throw RuntimeException("알 수 없는 뷰 타입 에러")
        }
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

    override fun getItemCount(): Int {
        return if (items == null) 0 else items.size
    }

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

    fun addItems(forumItems: ArrayList<forumitemdata>) {
        items.addAll(forumItems)
    }

    fun getItem(position: Int): forumitemdata {
        return items[position]
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun changeList(filteredlist: ArrayList<forumitemdata>) {
        items = filteredlist
        notifyDataSetChanged()
    }
}