package com.example.chatground2.adapter.holder

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.chatground2.Api.IpAddress
import com.example.chatground2.Model.DTO.CommentDto
import com.example.chatground2.Model.DTO.ForumDto
import com.example.chatground2.Model.DTO.UserDto
import com.example.chatground2.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.comment_item.*
import kotlinx.android.synthetic.main.comment_item.view.*
import java.lang.Exception
import java.text.DateFormat

class CommentsViewHolder(
    val context: Context,
    private val onReplySendClick: ((Int, String) -> Unit)?,
    private val onReplyCameraClick: ((String?) -> Unit)?,
    private val onDataSetChanged: ((Int) -> Unit)?,
    itemView: View
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val content = itemView.CI_content
    private val date = itemView.CI_date
    private val nickname = itemView.CI_nickname
    private val replyButton = itemView.CI_replyButton
    private val replyMessage = itemView.CI_replyMessage
    private val bottom = itemView.CI_bottom
    private val image = itemView.CI_image
    private val profile = itemView.CI_profile
    private val replySend = itemView.CI_replySend
    private val replyCamera = itemView.CI_replyCamera

    private var position: Int? = null
    private var replyImagePath: String? = null

    fun onBind(
        items: ArrayList<CommentDto>,
        isBottomVisible: ArrayList<Boolean>,
        position: Int,
        replyImagePath: String?
    ) {
        this.position = position
        this.replyImagePath = replyImagePath

        items[position].let {
            content.text = it.content
            date.text = DateFormat.getDateInstance(DateFormat.LONG).format(it.date)
            nickname.text = (it.userId as UserDto).nickname

            if (replyImagePath.isNullOrEmpty()) {
                Picasso.get().load(R.drawable.write_forum_camera_icon).into(replyCamera)
            } else {
                Picasso.get().load(replyImagePath).into(replyCamera)
            }

            if (isBottomVisible[position]) {
                bottom.visibility = View.VISIBLE
            } else {
                bottom.visibility = View.INVISIBLE
            }

            replyButton.setOnClickListener {
                if (isBottomVisible[position]) {
                    (context as Activity).CI_bottom.visibility = View.VISIBLE
                    isBottomVisible[position] = false

                    onDataSetChanged?.invoke(position)
                } else {
                    (context as Activity).CI_bottom.visibility = View.GONE
                    for (i in isBottomVisible.indices) {
                        if (isBottomVisible[i]) {
                            isBottomVisible[i] = false
                            onDataSetChanged?.invoke(i)
                            continue
                        }
                    }
                    isBottomVisible[position] = false
                    onDataSetChanged?.invoke(position)
                }
            }
            replyCamera.setOnClickListener(this)
            replySend.setOnClickListener(this)

            if (!it.imageUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(IpAddress.BaseURL + it.imageUrl)
                    .into(image, object : Callback {
                        override fun onSuccess() {

                        }

                        override fun onError(e: Exception?) {
                            println("이미지 로드 에러 : $e")
                            Picasso.get().load(R.drawable.noimage).into(image)
                        }
                    })
            }

            if (!(it.userId as UserDto).profile.isNullOrEmpty()) {
                Picasso.get()
                    .load(IpAddress.BaseURL + (it.userId as UserDto).profile)
                    .into(image, object : Callback {
                        override fun onSuccess() {

                        }

                        override fun onError(e: Exception?) {
                            println("이미지 로드 에러 : $e")
                            Picasso.get().load(R.drawable.default_profile).into(profile)
                        }
                    })
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.CI_replySend -> {
                position?.let { onReplySendClick?.invoke(it, replyMessage.text.toString()) }
            }
            R.id.CI_replyCamera -> {
                position?.let { onReplyCameraClick?.invoke(replyImagePath) }
            }
        }
    }
}
/////////////////////////////////
//        when (item.type) {
//            "comment" -> {
//                (holder as commentAdapter.commentViewHolder)
//
//                holder.CI_content.text = item.content
//                holder.CI_date.text = item.date
//                holder.CI_nickname.text = item.nickname
//                holder.CI_profile.setImageBitmap(connectprofile("uploads/" + item.Email + "_profile.jpg"))
//
//                if (IsImage) {
//                    holder.CI_recommentcamera.setImageBitmap(bitmap)
//                } else {
//                    holder.CI_recommentcamera.setImageResource(R.drawable.write_forum_camera_icon)
//                }
//
//                holder.CI_recommentcamera.setOnClickListener {
//                    if (IsImage) {
//                        val builder = AlertDialog.Builder(context)
//                        builder.setTitle("알림")
//                        builder.setMessage("이미지를 지우시겠습니까?")
//                        builder.setNegativeButton("취소", null)
//                        builder.setPositiveButton("삭제") { dialogInterface, i ->
//                            IsImage = false
//                            file = null
//                            holder.CI_recommentcamera.setImageResource(R.drawable.write_forum_camera_icon)
//                        }
//                        builder.show()
//                    } else {
//                        val items = arrayOf("이미지")
//                        var dialog =
//                            AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
//                        dialog.setTitle("이미지 첨부")
//                            .setItems(items) { _, which ->
//                                val selected: String = items[which]
//                                if (selected == "이미지") {
//                                    if (ContextCompat.checkSelfPermission(
//                                            context,
//                                            Manifest.permission.READ_EXTERNAL_STORAGE
//                                        ) != PackageManager.PERMISSION_GRANTED
//                                    ) {
//                                        //권한이 허용되지 않음
//                                        if (ActivityCompat.shouldShowRequestPermissionRationale(
//                                                (context as Activity),
//                                                Manifest.permission.READ_EXTERNAL_STORAGE
//                                            )
//                                        ) {
//                                            //이전에 이미 권한이 거부되었을 때 설명
//                                            setupPermissions()
//                                        } else {
//                                            //권한요청
//                                            ActivityCompat.requestPermissions(
//                                                (context as Activity),
//                                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                                                100
//                                            )
//                                        }
//
//                                    } else {
//                                        //권한이 이미 허용됨
//                                        openGallery(position)
//                                    }
//                                }
//                            }
//                            .create()
//                            .show()
//                    }
//                }
//
//                holder.CI_recommentsend.setOnClickListener {
//                    if (holder.CI_recommentmessage.text.toString().replace(" ", "") != "") {
//                        item.recommentstate = false
//                        notifyItemChanged(position)
//                        (context as Activity).RF_bottom.visibility = View.VISIBLE
//
//                        var commentuid = item.uid
//                        var content = holder.CI_recommentmessage.text.toString()
//                        var email = pref.getString("UserEmail", "Logout")
//                        var nickname = pref.getString("UserNickname", "Logout")
//                        writerecomment(commentuid, email, content, nickname)
//
//                        holder.CI_recommentmessage.setText("")
//                    } else {
//                        Toast.makeText(context, "내용을 입력해주세요!", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                if (item.IsImage) {
//                    holder.CI_image.visibility = View.VISIBLE
//                    holder.CI_image.setImageBitmap(connectimage(item.Imagepath!!))
//                } else {
//                    holder.CI_image.visibility = View.GONE
//                }
//
//                if (item.recommentstate) {
//                    holder.CI_writerecomment.background = ContextCompat.getDrawable(context, R.drawable.tedury3)
//                    holder.CI_bottom.visibility = View.VISIBLE
//                } else {
//                    holder.CI_writerecomment.background = ContextCompat.getDrawable(context, R.drawable.tedury2)
//                    holder.CI_bottom.visibility = View.GONE
//                }
//
//                holder.CI_writerecomment.setOnClickListener {
//                    if (item.recommentstate) {
//                        (context as Activity).RF_bottom.visibility = View.VISIBLE
//                        item.recommentstate = false
//                        notifyItemChanged(position)
//                    } else {
//                        (context as Activity).RF_bottom.visibility = View.GONE
//                        for (i in 0 until itemCount) {
//                            if (items[i].recommentstate) {
//                                items[i].recommentstate = false
//                                notifyItemChanged(i)
//                                continue
//                            }
//                            items[i].recommentstate = false
//                        }
//                        item.recommentstate = true
//                        notifyItemChanged(position)
//                    }
//                }
//            }
//    }
//////////////////////////////


//    private fun connectimage(imagpath: String): Bitmap {
//        lateinit var bitmap: Bitmap
//        try {
//            runBlocking {
//                launch {
//                    withContext(Dispatchers.IO)
//                    {
//                        val url: URL = URL(ipadress.ipadress + imagpath)
//
//                        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
//                        conn.doInput = true
//                        conn.connect()
//                        val ins: InputStream = conn.inputStream
//                        bitmap = BitmapFactory.decodeStream(ins)
//                    }
//                }
//            }
//        } catch (e: FileNotFoundException) {
//            bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.noimage)
//        }
//        return bitmap
//    }
//
//    fun openGallery(position: Int) {
//        val intent: Intent = Intent(Intent.ACTION_PICK)
//        intent.putExtra("position", position)
//        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
//        (context as Activity).startActivityForResult(intent, REOPEN_GALLERY)
//    }
//
//    private fun connectprofile(imagpath: String): Bitmap {
//        lateinit var bitmap: Bitmap
//        try {
//            runBlocking {
//                launch {
//                    withContext(Dispatchers.IO)
//                    {
//                        val url: URL = URL(ipadress.ipadress + imagpath)
//
//                        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
//                        conn.doInput = true
//                        conn.connect()
//                        val ins: InputStream = conn.inputStream
//                        bitmap = BitmapFactory.decodeStream(ins)
//                    }
//                }
//            }
//        } catch (e: FileNotFoundException) {
//            bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.defaultprofile)
//        }
//        return bitmap
//    }
//