package com.example.chatground2

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import com.example.chatground2.api.commentClient
import com.example.chatground2.api.forumClient
import com.example.chatground2.api.socket
import com.example.chatground2.model.DefaultResponse
import com.example.chatground2.model.commentitemdata
import com.example.chatground2.model.forumitemdata
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_readforum.*
import kotlinx.android.synthetic.main.fragment_forums.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

class readforum : AppCompatActivity() {
    var mSocket: Socket = socket.mSocket
    private lateinit var pref: SharedPreferences
    private var F_idx:Int = 0
    private var IsImage:Boolean = false
    private val OPEN_GALLERY = 41
    private val REOPEN_GALLERY = 42
    private var file:File? = null
    private var items: ArrayList<commentitemdata> = ArrayList()
    private lateinit var mAdapter:commentAdapter

    override fun onDestroy() {
        super.onDestroy()

        mSocket.off("ReturnForum", ReturnForum)
        mSocket.off("RemoveForum", RemoveForum)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_readforum)
        pref = getSharedPreferences("chatgroundlogin", Context.MODE_PRIVATE)
        F_idx = intent.getIntExtra("idx", 0)

        mAdapter = commentAdapter(this, items,pref,F_idx,mSocket)

        val lm = LinearLayoutManager(this)
        RF_Recy.adapter = mAdapter
        RF_Recy.layoutManager = lm

        var F_Email: String = intent.getStringExtra("Email")

        if (F_Email == pref.getString("UserEmail", "Logout")) {
            RF_editButton.visibility = View.VISIBLE
            RF_deleteButton.visibility = View.VISIBLE
        }
        mSocket.on("ReturnForum", ReturnForum)
        mSocket.on("RemoveForum", RemoveForum)

        mSocket.emit("ReturnForum", F_idx)

        RF_deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("알림")
            builder.setMessage("해당 게시글을 정말 지우시겠습니까?")
            builder.setNegativeButton("취소", null)
            builder.setPositiveButton("삭제") { dialogInterface, i ->
                mSocket.emit("RemoveForum",F_idx)
            }
            builder.show()
        }

        RF_camera.setOnClickListener {
            if(IsImage)
            {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("알림")
                builder.setMessage("이미지를 지우시겠습니까?")
                builder.setNegativeButton("취소", null)
                builder.setPositiveButton("삭제") { dialogInterface, i ->
                    IsImage = false
                    file = null
                    RF_camera.setImageResource(R.drawable.writeforum_camera_icon)
                }
                builder.show()
            }
            else
            {
                val items = arrayOf("이미지")
                var dialog = AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
                dialog.setTitle("이미지 첨부")
                    .setItems(items) { _, which ->
                        val selected: String = items[which]
                        if (selected == "이미지") {
                            if (ContextCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                //권한이 허용되지 않음
                                if (ActivityCompat.shouldShowRequestPermissionRationale(
                                        this,
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                    )
                                ) {
                                    //이전에 이미 권한이 거부되었을 때 설명
                                    setupPermissions()
                                } else {
                                    //권한요청
                                    ActivityCompat.requestPermissions(
                                        this,
                                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                        100
                                    )
                                }

                            } else {
                                //권한이 이미 허용됨
                                openGallery()
                            }
                        }
                    }
                    .create()
                    .show()
            }
        }

        RF_commentsend.setOnClickListener {
            if (RF_commentmessage.text.toString().replace(" ", "") != "") {
                var content = RF_commentmessage.text.toString()
                var email = pref.getString("UserEmail", "Logout")
                var nickname = pref.getString("UserNickname", "Logout")
                writecomment(F_idx,email,content,IsImage,nickname,file)

                RF_commentmessage.setText("")
            } else {
                Toast.makeText(this@readforum, "내용을 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val RemoveForum = Emitter.Listener { args ->
        val receivedData: JSONObject = args[0] as JSONObject

        if(receivedData.getBoolean("type"))
        {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(this@readforum, "게시글이 삭제되었습니다.", Toast.LENGTH_LONG).show();
            }
            val intent:Intent = Intent()
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
        else
        {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(this@readforum, "실패했습니다.", Toast.LENGTH_LONG).show();
            }
            System.out.println("DB저장 에러 : ${receivedData.get("data")}")
        }
    }
    private val ReturnForum = Emitter.Listener { args ->
        val receivedData: JSONObject = args[0] as JSONObject
        // your code...
        if (receivedData.getBoolean("type")) {
            var F_Email: String = receivedData.getJSONObject("data").getString("Email")
            var F_Nickname: String = receivedData.getJSONObject("data").getString("Nickname")
            var F_Content: String = receivedData.getJSONObject("data").getString("Content")
            var F_CommentNum: Int = receivedData.getJSONObject("data").getJSONArray("Comments").length()
            var F_Title: String = receivedData.getJSONObject("data").getString("Title")
            var F_Subject: String = receivedData.getJSONObject("data").getString("Subject")
            var F_Recommend: Int = receivedData.getJSONObject("data").getInt("Recommend")
            var F_ImageNum: Int = receivedData.getJSONObject("data").getInt("ImageNum")
            var F_Date: String = receivedData.getJSONObject("data").getString("Date")
            lateinit var F_Image0path: String
            lateinit var F_Image1path: String
            lateinit var F_Image2path: String
            lateinit var F_Image3path: String
            lateinit var F_Image4path: String
            if (receivedData.getJSONObject("data").has("Image0path")) {
                F_Image0path = receivedData.getJSONObject("data").getString("Image0path")
            }
            if (receivedData.getJSONObject("data").has("Image1path")) {
                F_Image1path = receivedData.getJSONObject("data").getString("Image1path")
            }
            if (receivedData.getJSONObject("data").has("Image2path")) {
                F_Image2path = receivedData.getJSONObject("data").getString("Image2path")
            }
            if (receivedData.getJSONObject("data").has("Image3path")) {
                F_Image3path = receivedData.getJSONObject("data").getString("Image3path")
            }
            if (receivedData.getJSONObject("data").has("Image4path")) {
                F_Image4path = receivedData.getJSONObject("data").getString("Image4path")
            }

            for (i: Int in 0 until F_CommentNum) {
                var C_Objectid: String = receivedData.getJSONObject("data").getJSONArray("Comments").getJSONObject(i).getString("_id")
                var C_Email: String = receivedData.getJSONObject("data").getJSONArray("Comments").getJSONObject(i).getString("Email")
                var C_Nickname: String = receivedData.getJSONObject("data").getJSONArray("Comments").getJSONObject(i).getString("Nickname")
                var C_Date: String = receivedData.getJSONObject("data").getJSONArray("Comments").getJSONObject(i).getString("comment_Date")
                var C_content: String = receivedData.getJSONObject("data").getJSONArray("Comments").getJSONObject(i).getString("comment_Content")
                var C_IsImage:Boolean = receivedData.getJSONObject("data").getJSONArray("Comments").getJSONObject(i).getBoolean("IsImage")
                var C_Imagepath:String? = null
                if(receivedData.getJSONObject("data").getJSONArray("Comments").getJSONObject(i).has("comment_Imagepath"))
                {
                    C_Imagepath = receivedData.getJSONObject("data").getJSONArray("Comments").getJSONObject(i).getString("comment_Imagepath")
                }

                items.add(commentitemdata("comment",false,C_Objectid,C_Email,C_Nickname,C_content,C_Date,C_IsImage,C_Imagepath))
            }

            CoroutineScope(Dispatchers.Main).launch {
                RF_title?.text = F_Title
                RF_content?.text = F_Content
                RF_subject?.text = F_Subject
                RF_date?.text = F_Date
                RF_nickname?.text = F_Nickname
                RF_commentNum?.text = "댓글 : $F_CommentNum 개"
                RF_recommendNum?.text = "추천 : $F_Recommend 개"
                mAdapter.notifyDataSetChanged()

                RF_pofile?.setImageBitmap(connectprofile("uploads/" + F_Email+ "_profile.jpg"))

                when (F_ImageNum) {
                    0 -> {
                        RF_image0?.visibility = View.GONE
                        RF_image1?.visibility = View.GONE
                        RF_image2?.visibility = View.GONE
                        RF_image3?.visibility = View.GONE
                        RF_image4?.visibility = View.GONE
                    }
                    1 -> {
                        RF_image0?.visibility = View.VISIBLE
                        RF_image1?.visibility = View.GONE
                        RF_image2?.visibility = View.GONE
                        RF_image3?.visibility = View.GONE
                        RF_image4?.visibility = View.GONE

                        if (F_Image0path != null) {
                            RF_image0?.setImageBitmap(connectimage(F_Image0path))
                        }
                    }
                    2 -> {
                        RF_image0?.visibility = View.VISIBLE
                        RF_image1?.visibility = View.VISIBLE
                        RF_image2?.visibility = View.GONE
                        RF_image3?.visibility = View.GONE
                        RF_image4?.visibility = View.GONE

                        if (F_Image0path != null) {
                            RF_image0?.setImageBitmap(connectimage(F_Image0path))
                        }
                        if (F_Image1path != null) {
                            RF_image1?.setImageBitmap(connectimage(F_Image1path))
                        }
                    }
                    3 -> {
                        RF_image0?.visibility = View.VISIBLE
                        RF_image1?.visibility = View.VISIBLE
                        RF_image2?.visibility = View.VISIBLE
                        RF_image3?.visibility = View.GONE
                        RF_image4?.visibility = View.GONE

                        if (F_Image0path != null) {
                            RF_image0?.setImageBitmap(connectimage(F_Image0path))
                        }
                        if (F_Image1path != null) {
                            RF_image1?.setImageBitmap(connectimage(F_Image1path))
                        }
                        if (F_Image2path != null) {
                            RF_image2?.setImageBitmap(connectimage(F_Image2path))
                        }
                    }
                    4 -> {
                        RF_image0?.visibility = View.VISIBLE
                        RF_image1?.visibility = View.VISIBLE
                        RF_image2?.visibility = View.VISIBLE
                        RF_image3?.visibility = View.VISIBLE
                        RF_image4?.visibility = View.GONE

                        if (F_Image0path != null) {
                            RF_image0?.setImageBitmap(connectimage(F_Image0path))
                        }
                        if (F_Image1path != null) {
                            RF_image1?.setImageBitmap(connectimage(F_Image1path))
                        }
                        if (F_Image2path != null) {
                            RF_image2?.setImageBitmap(connectimage(F_Image2path))
                        }
                        if (F_Image3path != null) {
                            RF_image3?.setImageBitmap(connectimage(F_Image3path))
                        }
                    }
                    5 -> {
                        RF_image0?.visibility = View.VISIBLE
                        RF_image1?.visibility = View.VISIBLE
                        RF_image2?.visibility = View.VISIBLE
                        RF_image3?.visibility = View.VISIBLE
                        RF_image4?.visibility = View.VISIBLE

                        if (F_Image0path != null) {
                            RF_image0?.setImageBitmap(connectimage(F_Image0path))
                        }
                        if (F_Image1path != null) {
                            RF_image1?.setImageBitmap(connectimage(F_Image1path))
                        }
                        if (F_Image2path != null) {
                            RF_image2?.setImageBitmap(connectimage(F_Image2path))
                        }
                        if (F_Image3path != null) {
                            RF_image3?.setImageBitmap(connectimage(F_Image3path))
                        }
                        if (F_Image4path != null) {
                            RF_image4?.setImageBitmap(connectimage(F_Image4path))
                        }
                    }
                }
            }
        } else//글삭제됨
        {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                OPEN_GALLERY -> {
                    var currentImageUrl: Uri? = data?.data
                    var path = getPath(currentImageUrl!!)
                    val bitmap: Bitmap = BitmapFactory.decodeFile(path)
                    RF_camera.setImageBitmap(bitmap)

                    file = File(path)
                    IsImage = true
                }
                REOPEN_GALLERY -> {
                    var currentImageUrl: Uri? = data?.data
                    var path = getPath(currentImageUrl!!)
                    val bitmap: Bitmap = BitmapFactory.decodeFile(path)

                    mAdapter.bitmap = bitmap
                    mAdapter.IsImage = true
                    mAdapter.file = File(path)

                    mAdapter.notifyDataSetChanged()
                }
            }
        } else {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
        }
    }

    override fun onBackPressed() {
        val intent:Intent = Intent()
        setResult(Activity.RESULT_OK,intent)
        finish()
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
            bitmap = BitmapFactory.decodeResource(this.resources,R.drawable.noimage)
        }
        return bitmap
    }

    private fun connectprofile(imagpath: String): Bitmap {
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
            bitmap = BitmapFactory.decodeResource(this.resources,R.drawable.profileicon)
        }
        return bitmap
    }

    private fun writecomment(idx:Int, email:String,content:String,isimage:Boolean,nickname:String,file:File?)
    {
        if(isimage)
        {
            RF_camera.setImageResource(R.drawable.writeforum_camera_icon)
            var fileReqBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
            var part: MultipartBody.Part = MultipartBody.Part.createFormData("upload", file!!.name, fileReqBody)

            commentClient.instance.uploadcomment(idx,email,nickname,content,isimage,part).enqueue(object : Callback<DefaultResponse> {
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if(response.body()!!.type)
                    {
                        Toast.makeText(this@readforum, "댓글이 작성되었습니다!", Toast.LENGTH_SHORT).show()
                        items.clear()
                        mSocket.emit("ReturnForum", F_idx)
                    }
                    else
                    {
                        System.out.println("DB저장 에러 : ${response.body()!!.data}")
                        Toast.makeText(this@readforum, "댓글 작성이 실패했습니다!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(this@readforum, "댓글 작성이 실패했습니다!", Toast.LENGTH_SHORT).show()
                    System.out.println("에러 : $t")
                }
            })
        }
        else
        {
            commentClient.instance.uploadcomment(idx,email,nickname,content,isimage,null).enqueue(object : Callback<DefaultResponse> {
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if(response.body()!!.type)
                    {
                        Toast.makeText(this@readforum, "댓글이 작성되었습니다!", Toast.LENGTH_SHORT).show()
                        items.clear()
                        mSocket.emit("ReturnForum", F_idx)
                    }
                    else
                    {
                        System.out.println("DB저장 에러 : ${response.body()!!.data}")
                        Toast.makeText(this@readforum, "댓글 작성이 실패했습니다!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    System.out.println("에러 : $t")
                    Toast.makeText(this@readforum, "댓글 작성이 실패했습니다!", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun getPath(uri: Uri): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor = contentResolver.query(uri, proj, null, null, null)
        var index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c.moveToFirst()
        return c.getString(index)
    }

    fun openGallery() {
        val intent: Intent = Intent(Intent.ACTION_PICK)
        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, OPEN_GALLERY)
    }

    fun setupPermissions() {
        //스토리지 읽기 퍼미션을 permission 변수에 담는다
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            100
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            100 -> {
//                if(grantResults.isNotEmpty()
//                            && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@readforum, "권한이 거부되었습니다", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}