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
import java.io.File


class chatAdapter(val context: Context,List: ArrayList<chatitemdata>,sharedPreferences: SharedPreferences) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items:ArrayList<chatitemdata> = List

    var pref = sharedPreferences

    override fun getItemViewType(position: Int): Int {
        return when {
            items[position].data == "text" -> 0
            items[position].data == "image" -> 1
            items[position].data == "video" -> 2
            items[position].data == "Strategictext_a" -> 3
            items[position].data == "Strategictext_o" -> 4
            items[position].data == "Strategictext_g" -> 5

            else -> 100
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
        return when (viewType) {
            0 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.activity_chattextitem, parent, false)
                TextTypeViewHolder(view)
            }
            1 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.activity_chatimageitem, parent, false)
                ImageTypeViewHolder(view)
            }
            2 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.activity_chatvideoitem, parent, false)
                VideoTypeViewHolder(view)
            }
            3 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.activity_chattextitem, parent, false)
                TextTypeViewHolder(view)
            }
            4 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.activity_chattextitem, parent, false)
                TextTypeViewHolder(view)
            }
            5 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.activity_chattextitem, parent, false)
                TextTypeViewHolder(view)
            }

            else -> throw RuntimeException("알 수 없는 뷰 타입 에러")
        }
    }

    inner class TextTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val TCsystembox = itemView.TextCsystembox
        val TCleftitem = itemView.TextCleftitem
        val TCrightitem = itemView.TextCrightitem
        val TCLdate = itemView.TextCLdate
        val TCLinbox = itemView.TextCLinbox
        val TCLnickname = itemView.TextCLnickname
        val TCLprofile = itemView.TextCLprofile
        val TCRdate = itemView.TextCRdate
        val TCRoutbox = itemView.TextCRoutbox
    }

    inner class ImageTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ICsystembox = itemView.ImageCsystembox
        val ICleftitem = itemView.ImageCleftitem
        val ICrightitem = itemView.ImageCrightitem
        val ICLdate = itemView.ImageCLdate
        val ICLnickname = itemView.ImageCLnickname
        val ICLimage = itemView.ImageCLimage
        val ICLprofile = itemView.ImageCLprofile
        val ICRdate = itemView.ImageCRdate
        val ICRimage = itemView.ImageCRimage
    }

    inner class VideoTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val VCsystembox = itemView.VideoCsystembox
        val VCleftitem = itemView.VideoCleftitem
        val VCrightitem = itemView.VideoCrightitem
        val VCLdate = itemView.VideoCLdate
        val VCLnickname = itemView.VideoCLnickname
        val VCLvideo = itemView.VideoCLvideo
        val VCLprofile = itemView.VideoCLprofile
        val VCRdate = itemView.VideoCRdate
        val VCRvideo = itemView.VideoCRvideo
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (item.data) {
            "Strategictext_a" ->{
                (holder as TextTypeViewHolder)
                if(item.type == "System")
                {
                    holder.TCsystembox.visibility = View.VISIBLE
                    holder.TCleftitem.visibility = View.GONE
                    holder.TCrightitem.visibility = View.GONE

                    holder.TCsystembox.text = item.content
                    holder.TCsystembox.setTextColor(ContextCompat.getColor(context,R.color.Blue))
                }
                else if(item.type == "Client")
                {
                    if(item.email == pref.getString("UserEmail","Logout"))
                    {
                        holder.TCsystembox.visibility = View.GONE
                        holder.TCleftitem.visibility = View.GONE
                        holder.TCrightitem.visibility = View.VISIBLE
                        holder.TCRdate.text = item.date
                        holder.TCRoutbox.text = item.content
                        holder.TCRoutbox.setTextColor(ContextCompat.getColor(context,R.color.Blue))
                    }
                    else
                    {
                        holder.TCsystembox.visibility = View.GONE
                        holder.TCleftitem.visibility = View.VISIBLE
                        holder.TCrightitem.visibility = View.GONE

                        holder.TCLnickname.text = item.nickname
                        holder.TCLinbox.text = item.content
                        holder.TCLdate.text = item.date
                        holder.TCLinbox.setTextColor(ContextCompat.getColor(context,R.color.Blue))
                    }
                }
            }
            "Strategictext_o" ->{
                (holder as TextTypeViewHolder)
                if(item.type == "System")
                {
                    holder.TCsystembox.visibility = View.VISIBLE
                    holder.TCleftitem.visibility = View.GONE
                    holder.TCrightitem.visibility = View.GONE

                    holder.TCsystembox.text = item.content
                    holder.TCsystembox.setTextColor(ContextCompat.getColor(context,R.color.Red))
                }
                else if(item.type == "Client")
                {
                    if(item.email == pref.getString("UserEmail","Logout"))
                    {
                        holder.TCsystembox.visibility = View.GONE
                        holder.TCleftitem.visibility = View.GONE
                        holder.TCrightitem.visibility = View.VISIBLE
                        holder.TCRdate.text = item.date
                        holder.TCRoutbox.text = item.content
                        holder.TCRoutbox.setTextColor(ContextCompat.getColor(context,R.color.Red))
                    }
                    else
                    {
                        holder.TCsystembox.visibility = View.GONE
                        holder.TCleftitem.visibility = View.VISIBLE
                        holder.TCrightitem.visibility = View.GONE

                        holder.TCLnickname.text = item.nickname
                        holder.TCLinbox.text = item.content
                        holder.TCLdate.text = item.date
                        holder.TCLinbox.setTextColor(ContextCompat.getColor(context,R.color.Red))
                    }
                }
            }
            "Strategictext_g" ->{
                (holder as TextTypeViewHolder)
                if(item.type == "System")
                {
                    holder.TCsystembox.visibility = View.VISIBLE
                    holder.TCleftitem.visibility = View.GONE
                    holder.TCrightitem.visibility = View.GONE

                    holder.TCsystembox.text = item.content
                    holder.TCsystembox.setTextColor(ContextCompat.getColor(context,R.color.giveupcolor))
                }
                else if(item.type == "Client")
                {
                    if(item.email == pref.getString("UserEmail","Logout"))
                    {
                        holder.TCsystembox.visibility = View.GONE
                        holder.TCleftitem.visibility = View.GONE
                        holder.TCrightitem.visibility = View.VISIBLE
                        holder.TCRdate.text = item.date
                        holder.TCRoutbox.text = item.content
                        holder.TCRoutbox.setTextColor(ContextCompat.getColor(context,R.color.giveupcolor))
                    }
                    else
                    {
                        holder.TCsystembox.visibility = View.GONE
                        holder.TCleftitem.visibility = View.VISIBLE
                        holder.TCrightitem.visibility = View.GONE

                        holder.TCLnickname.text = item.nickname
                        holder.TCLinbox.text = item.content
                        holder.TCLdate.text = item.date
                        holder.TCLinbox.setTextColor(ContextCompat.getColor(context,R.color.giveupcolor))
                    }
                }
            }
            "text" -> {
                (holder as TextTypeViewHolder)
                if(item.type.equals("System"))
                {
                    holder.TCsystembox.visibility = View.VISIBLE
                    holder.TCleftitem.visibility = View.GONE
                    holder.TCrightitem.visibility = View.GONE

                    holder.TCsystembox.text = item.content
                }
                else if(item.type == "Client")
                {
                    if(item.email == pref.getString("UserEmail","Logout"))
                    {
                        holder.TCsystembox.visibility = View.GONE
                        holder.TCleftitem.visibility = View.GONE
                        holder.TCrightitem.visibility = View.VISIBLE
                        holder.TCRdate.text = item.date
                        holder.TCRoutbox.text = item.content
                    }
                    else
                    {
                        holder.TCsystembox.visibility = View.GONE
                        holder.TCleftitem.visibility = View.VISIBLE
                        holder.TCrightitem.visibility = View.GONE

                        holder.TCLnickname.text = item.nickname
                        holder.TCLinbox.text = item.content
                        holder.TCLdate.text = item.date
                    }
                }
            }
            "image" -> {
                (holder as ImageTypeViewHolder)
                if(item.type.equals("System"))
                {
                    holder.ICsystembox.visibility = View.VISIBLE
                    holder.ICleftitem.visibility = View.GONE
                    holder.ICrightitem.visibility = View.GONE

                    holder.ICsystembox.text = item.content
                }
                else if(item.type == "Client")
                {
                    if(item.email == pref.getString("UserEmail","Logout"))
                    {
                        holder.ICsystembox.visibility = View.GONE
                        holder.ICleftitem.visibility = View.GONE
                        holder.ICrightitem.visibility = View.VISIBLE
                        holder.ICRdate.text = item.date
                        holder.ICRimage.setImageBitmap(decodeImage(item.content))
                    }
                    else
                    {
                        holder.ICsystembox.visibility = View.GONE
                        holder.ICleftitem.visibility = View.VISIBLE
                        holder.ICrightitem.visibility = View.GONE

                        holder.ICLnickname.text = item.nickname
                        holder.ICLdate.text = item.date
                        holder.ICLimage.setImageBitmap(decodeImage(item.content))
                    }
                }
            }

            "video" -> {
                (holder as VideoTypeViewHolder)
                if(item.type.equals("System"))
                {
                    holder.VCsystembox.visibility = View.VISIBLE
                    holder.VCleftitem.visibility = View.GONE
                    holder.VCrightitem.visibility = View.GONE

                    holder.VCsystembox.text = item.content
                }
                else if(item.type == "Client")
                {
                    if(item.email == pref.getString("UserEmail","Logout"))
                    {
                        holder.VCsystembox.visibility = View.GONE
                        holder.VCleftitem.visibility = View.GONE
                        holder.VCrightitem.visibility = View.VISIBLE
                        holder.VCRdate.text = item.date

                        val bitmap:Bitmap = ThumbnailUtils.createVideoThumbnail(item.content, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND)
                        val ob:BitmapDrawable = BitmapDrawable(context.resources,bitmap)
                        holder.VCRvideo.background = ob
                        holder.VCRvideo.setOnClickListener {
                            val videoFile = File(item.content)
                            val fileUri:Uri = FileProvider.getUriForFile(context, "com.example.chatground2.fileprovider", videoFile)
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.setDataAndType(fileUri, "video/*")
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)//DO NOT FORGET THIS EVER
                            context.startActivity(intent)
                        }
                    }
                    else
                    {
                        holder.VCsystembox.visibility = View.GONE
                        holder.VCleftitem.visibility = View.VISIBLE
                        holder.VCrightitem.visibility = View.GONE

                        holder.VCLnickname.text = item.nickname
                        holder.VCLdate.text = item.date

                        val bitmap:Bitmap = ThumbnailUtils.createVideoThumbnail(item.content, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND)
                        val ob:BitmapDrawable = BitmapDrawable(context.resources,bitmap)
                        holder.VCLvideo.background = ob
                        holder.VCLvideo.setOnClickListener {
                            val videoFile = File(item.content)
                            val fileUri:Uri = FileProvider.getUriForFile(context, "com.example.chatground2.fileprovider", videoFile)
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.setDataAndType(fileUri, "video/*")
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)//DO NOT FORGET THIS EVER
                            context.startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    private fun decodeImage(data: String): Bitmap
    {
        val b:ByteArray = Base64.decode(data, Base64.DEFAULT)
        val bmp: Bitmap = BitmapFactory.decodeByteArray(b,0,b.size)

        return bmp
    }
}