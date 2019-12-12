package com.example.chatground2

import android.Manifest
import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v7.app.AlertDialog
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.chatground2.api.commentClient
import com.example.chatground2.model.DefaultResponse
import com.example.chatground2.model.commentitemdata
import com.example.chatground2.model.forumitemdata
import kotlinx.android.synthetic.main.activity_readforum.*
import kotlinx.android.synthetic.main.commentitem.view.*
import kotlinx.android.synthetic.main.forumsitem.view.*
import kotlinx.android.synthetic.main.recommentitem.view.*
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.Socket
import java.net.URL


class commentAdapter(val context: Context, List: ArrayList<commentitemdata>, val pref: SharedPreferences, val F_idx:Int, val mSocket:io.socket.client.Socket) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items:ArrayList<commentitemdata> = List
    private val REOPEN_GALLERY = 42

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
        return when (viewType) {
            0 -> {
                view = LayoutInflater.from(context).inflate(R.layout.commentitem, parent, false)
                commentViewHolder(view)
            }
            1 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.recommentitem, parent, false)
                recommentViewHolder(view)
            }
            else -> throw RuntimeException("알 수 없는 뷰 타입 에러")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            items[position].type == "comment" -> 0
            items[position].type == "recomment" -> 1

            else -> 100
        }
    }

    inner class commentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val CI_content = itemView.CI_content
        val CI_date = itemView.CI_date
        val CI_nickname = itemView.CI_nickname
        val CI_profile = itemView.CI_profile
        val CI_image = itemView.CI_image
        val CI_writerecomment = itemView.CI_writerecomment
        val CI_bottom = itemView.CI_bottom
        val CI_card = itemView.CI_card
        val CI_recommentsend = itemView.CI_recommentsend
        val CI_recommentmessage = itemView.CI_recommentmessage
        val CI_recommentcamera = itemView.CI_recommentcamera
    }

    inner class recommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val RCI_content = itemView.RCI_content
        val RCI_date = itemView.RCI_date
        val RCI_nickname = itemView.RCI_nickname
        val RCI_profile = itemView.RCI_profile
        val RCI_image = itemView.RCI_image
    }

    override fun getItemCount(): Int = items.size

    var IsImage:Boolean = false
    var file:File? = null
    var bitmap:Bitmap? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (item.type) {
            "comment" -> {
                (holder as commentAdapter.commentViewHolder)

                holder.CI_content.text = item.content
                holder.CI_date.text = item.date
                holder.CI_nickname.text = item.nickname
                holder.CI_profile.setImageBitmap(connectprofile("uploads/" + item.Email + "_profile.jpg"))

                if(IsImage)
                {
                    holder.CI_recommentcamera.setImageBitmap(bitmap)
                }
                else
                {
                    holder.CI_recommentcamera.setImageResource(R.drawable.writeforum_camera_icon)
                }

                holder.CI_recommentcamera.setOnClickListener {
                    if(IsImage)
                    {
                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("알림")
                        builder.setMessage("이미지를 지우시겠습니까?")
                        builder.setNegativeButton("취소", null)
                        builder.setPositiveButton("삭제") { dialogInterface, i ->
                            IsImage = false
                            file = null
                            holder.CI_recommentcamera.setImageResource(R.drawable.writeforum_camera_icon)
                        }
                        builder.show()
                    }
                    else
                    {
                        val items = arrayOf("이미지")
                        var dialog = AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
                        dialog.setTitle("이미지 첨부")
                            .setItems(items) { _, which ->
                                val selected: String = items[which]
                                if (selected == "이미지") {
                                    if (ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.READ_EXTERNAL_STORAGE
                                        ) != PackageManager.PERMISSION_GRANTED
                                    ) {
                                        //권한이 허용되지 않음
                                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                                (context as Activity),
                                                Manifest.permission.READ_EXTERNAL_STORAGE
                                            )
                                        ) {
                                            //이전에 이미 권한이 거부되었을 때 설명
                                            setupPermissions()
                                        } else {
                                            //권한요청
                                            ActivityCompat.requestPermissions(
                                                (context as Activity),
                                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                                100
                                            )
                                        }

                                    } else {
                                        //권한이 이미 허용됨
                                        openGallery(position)
                                    }
                                }
                            }
                            .create()
                            .show()
                    }
                }

                holder.CI_recommentsend.setOnClickListener {
                    if (holder.CI_recommentmessage.text.toString().replace(" ", "") != "") {
                        var commentuid = item.uid
                        var content = holder.CI_recommentmessage.text.toString()
                        var email = pref.getString("UserEmail", "Logout")
                        var nickname = pref.getString("UserNickname", "Logout")
                        writerecomment(commentuid,email,content,nickname)

                        holder.CI_recommentmessage.setText("")
                    } else {
                        Toast.makeText(context, "내용을 입력해주세요!", Toast.LENGTH_SHORT).show()
                    }
                }

                if(item.IsImage)
                {
                    holder.CI_image.visibility = View.VISIBLE
                    holder.CI_image.setImageBitmap(connectimage(item.Imagepath!!))
                }
                else
                {
                    holder.CI_image.visibility = View.GONE
                }

                if(item.recommentstate)
                {
                    holder.CI_writerecomment.background = ContextCompat.getDrawable(context,R.drawable.tedury3)
                    holder.CI_bottom.visibility = View.VISIBLE
                }
                else
                {
                    holder.CI_writerecomment.background = ContextCompat.getDrawable(context,R.drawable.tedury2)
                    holder.CI_bottom.visibility = View.GONE
                }

                holder.CI_writerecomment.setOnClickListener {
                    for(i in 0 until itemCount)
                    {
                        if(item.recommentstate)
                        {
                            continue
                        }
                        items[i].recommentstate = false
                    }

                    if(item.recommentstate)
                    {
                        (context as Activity).RF_bottom.visibility = View.VISIBLE
                        item.recommentstate = false
                        (context as Activity).RF_commentmessage.requestFocus()
                    }
                    else
                    {
                        (context as Activity).RF_bottom.visibility = View.GONE
                        item.recommentstate = true
                        (context as Activity).RF_commentmessage.clearFocus()

                        CoroutineScope(Dispatchers.Main).launch {
                            holder.CI_recommentmessage.requestFocus()
                        }
                    }
                    notifyDataSetChanged()
                }
            }
            "recomment" -> {
                (holder as commentAdapter.recommentViewHolder)
                holder.RCI_content.text = item.content
                holder.RCI_date.text = item.date
                holder.RCI_nickname.text = item.nickname
                holder.RCI_profile.setImageBitmap(connectprofile("uploads/" + item.Email + "_profile.jpg"))

                if(item.IsImage)
                {
                    holder.RCI_image.visibility = View.VISIBLE
                    holder.RCI_image.setImageBitmap(connectimage(item.Imagepath!!))
                }
                else
                {
                    holder.RCI_image.visibility = View.GONE
                }
            }
        }
    }

    private fun writerecomment(commentuid:String, email:String,content:String,nickname:String)
    {
        if(IsImage)
        {
//            this.IsImage = false
//            this.file = null
//            notifyDataSetChanged()

            var fileReqBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
            var part: MultipartBody.Part = MultipartBody.Part.createFormData("upload", file!!.name, fileReqBody)

            commentClient.instance.uploadrecomment(F_idx,commentuid,email,nickname,content,IsImage,part).enqueue(object :
                Callback<DefaultResponse> {
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if(response.body()!!.type)
                    {
                        Toast.makeText(context, "댓글이 작성되었습니다!", Toast.LENGTH_SHORT).show()
                        items.clear()
                        mSocket.emit("ReturnForum", F_idx)
                    }
                    else
                    {
                        System.out.println("DB저장 에러 : ${response.body()!!.data}")
                        Toast.makeText(context, "댓글 작성이 실패했습니다!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(context, "댓글 작성이 실패했습니다!", Toast.LENGTH_SHORT).show()
                    System.out.println("에러 : $t")
                }
            })
        }
        else
        {
            commentClient.instance.uploadrecomment(F_idx,commentuid,email,nickname,content,IsImage,null).enqueue(object :
                Callback<DefaultResponse> {
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if(response.body()!!.type)
                    {
                        Toast.makeText(context, "댓글이 작성되었습니다!", Toast.LENGTH_SHORT).show()
                        items.clear()
                        mSocket.emit("ReturnForum", F_idx)
                    }
                    else
                    {
                        System.out.println("DB저장 에러 : ${response.body()!!.data}")
                        Toast.makeText(context, "댓글 작성이 실패했습니다!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    System.out.println("에러 : $t")
                    Toast.makeText(context, "댓글 작성이 실패했습니다!", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun connectimage(imagpath: String): Bitmap {
        lateinit var bitmap:Bitmap
        try {
            runBlocking {
                launch {
                    withContext(Dispatchers.IO)
                    {
                        val url: URL = URL(ipadress.ipadress + imagpath)

                        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                        conn.doInput = true
                        conn.connect()
                        val ins: InputStream = conn.inputStream
                        bitmap = BitmapFactory.decodeStream(ins)
                    }
                }
            }
        }catch (e:FileNotFoundException)
        {
            bitmap = BitmapFactory.decodeResource(context.resources,R.drawable.noimage)
        }
        return bitmap
    }

    fun openGallery(position:Int) {
        val intent: Intent = Intent(Intent.ACTION_PICK)
        intent.putExtra("position",position)
        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
        (context as Activity).startActivityForResult(intent, REOPEN_GALLERY)
    }

    private fun connectprofile(imagpath: String): Bitmap {
        lateinit var bitmap: Bitmap
        try {
            runBlocking {
                launch {
                    withContext(Dispatchers.IO)
                    {
                        val url: URL = URL(ipadress.ipadress + imagpath)

                        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                        conn.doInput = true
                        conn.connect()
                        val ins: InputStream = conn.inputStream
                        bitmap = BitmapFactory.decodeStream(ins)
                    }
                }
            }
        }catch (e: FileNotFoundException)
        {
            bitmap = BitmapFactory.decodeResource(context.resources,R.drawable.defaultprofile)
        }
        return bitmap
    }

    fun setupPermissions() {
        //스토리지 읽기 퍼미션을 permission 변수에 담는다
        val permission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            100
        )
    }
}