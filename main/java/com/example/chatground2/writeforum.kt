package com.example.chatground2

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
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
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Toast
import com.example.chatground2.api.forumApi
import com.example.chatground2.api.forumClient
import com.example.chatground2.api.imageClient
import com.example.chatground2.api.socket
import com.example.chatground2.model.DefaultResponse
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_writeforum.*
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Part
import java.io.File
import java.util.ArrayList
import javax.security.auth.Subject

class writeforum : AppCompatActivity() {

    private val OPEN_GALLERY = 1

    private lateinit var pref: SharedPreferences

    lateinit var arraAdapter: ArrayAdapter<CharSequence>
    private lateinit var imagepathArrayList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writeforum)

        imagepathArrayList = ArrayList()

        pref = getSharedPreferences("chatgroundlogin", Context.MODE_PRIVATE)
        arraAdapter = ArrayAdapter.createFromResource(this, R.array.interest, android.R.layout.simple_spinner_item)

        WF_subject.adapter = arraAdapter

        WF_savebutton.setOnClickListener {
            if (!WF_title.text.isEmpty() && !WF_content.text.isEmpty()) {
                WF_savebutton.isEnabled = false

                val Email: String = pref.getString("UserEmail", "Logout")
                val Nickname: String = pref.getString("UserNickname", "Logout")
                val Title: String = WF_title.text.toString()
                val Subject: String = WF_subject.selectedItem.toString()
                val Content: String = WF_content.text.toString()
                val ImageNum: Int = imagepathArrayList.size
                when (ImageNum) {
                    0 -> {
                        forumClient.instance.uploadforum(
                            Email,
                            Nickname,
                            Title,
                            Subject,
                            Content,
                            ImageNum,
                            null,
                            null,
                            null,
                            null,
                            null
                        )
                            .enqueue(object : Callback<DefaultResponse> {
                                override fun onResponse(
                                    call: Call<DefaultResponse>,
                                    response: Response<DefaultResponse>
                                ) {
                                    if(response.body()!!.type)
                                    {
                                        saveComplete()
                                    }
                                    else
                                    {
                                        System.out.println("DB저장 에러 : ${response.body()!!.data}")
                                        saveFailed()
                                    }
                                }

                                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                    saveFailed()
                                }
                            })
                    }
                    1 -> {
                        var file1: File = File(imagepathArrayList[0])
                        var fileReqBody1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1)
                        var part1: MultipartBody.Part =
                            MultipartBody.Part.createFormData("upload", file1.name, fileReqBody1)

                        forumClient.instance.uploadforum(
                            Email,
                            Nickname,
                            Title,
                            Subject,
                            Content,
                            ImageNum,
                            part1,
                            null,
                            null,
                            null,
                            null
                        )
                            .enqueue(object : Callback<DefaultResponse> {
                                override fun onResponse(
                                    call: Call<DefaultResponse>,
                                    response: Response<DefaultResponse>
                                ) {
                                    if(response.body()!!.type)
                                    {
                                        saveComplete()
                                    }
                                    else
                                    {
                                        System.out.println("DB저장 에러 : ${response.body()!!.data}")
                                        saveFailed()
                                    }
                                }

                                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                    saveFailed()
                                }
                            })
                    }
                    2 -> {
                        var file1: File = File(imagepathArrayList[0])
                        var fileReqBody1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1)
                        var part1: MultipartBody.Part =
                            MultipartBody.Part.createFormData("upload", file1.name, fileReqBody1)

                        var file2: File = File(imagepathArrayList[1])
                        var fileReqBody2: RequestBody = RequestBody.create(MediaType.parse("image/*"), file2)
                        var part2: MultipartBody.Part =
                            MultipartBody.Part.createFormData("upload", file2.name, fileReqBody2)

                        forumClient.instance.uploadforum(
                            Email,
                            Nickname,
                            Title,
                            Subject,
                            Content,
                            ImageNum,
                            part1,
                            part2,
                            null,
                            null,
                            null
                        )
                            .enqueue(object : Callback<DefaultResponse> {
                                override fun onResponse(
                                    call: Call<DefaultResponse>,
                                    response: Response<DefaultResponse>
                                ) {
                                    if(response.body()!!.type)
                                    {
                                        saveComplete()
                                    }
                                    else
                                    {
                                        System.out.println("DB저장 에러 : ${response.body()!!.data}")
                                        saveFailed()
                                    }
                                }

                                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                    saveFailed()
                                }
                            })
                    }
                    3 -> {
                        var file1: File = File(imagepathArrayList[0])
                        var fileReqBody1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1)
                        var part1: MultipartBody.Part =
                            MultipartBody.Part.createFormData("upload", file1.name, fileReqBody1)

                        var file2: File = File(imagepathArrayList[1])
                        var fileReqBody2: RequestBody = RequestBody.create(MediaType.parse("image/*"), file2)
                        var part2: MultipartBody.Part =
                            MultipartBody.Part.createFormData("upload", file2.name, fileReqBody2)

                        var file3: File = File(imagepathArrayList[2])
                        var fileReqBody3: RequestBody = RequestBody.create(MediaType.parse("image/*"), file3)
                        var part3: MultipartBody.Part =
                            MultipartBody.Part.createFormData("upload", file3.name, fileReqBody3)

                        forumClient.instance.uploadforum(
                            Email,
                            Nickname,
                            Title,
                            Subject,
                            Content,
                            ImageNum,
                            part1,
                            part2,
                            part3,
                            null,
                            null
                        )
                            .enqueue(object : Callback<DefaultResponse> {
                                override fun onResponse(
                                    call: Call<DefaultResponse>,
                                    response: Response<DefaultResponse>
                                ) {
                                    if(response.body()!!.type)
                                    {
                                        saveComplete()
                                    }
                                    else
                                    {
                                        System.out.println("DB저장 에러 : ${response.body()!!.data}")
                                        saveFailed()
                                    }
                                }

                                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                    saveFailed()
                                }
                            })
                    }
                    4 -> {
                        var file1: File = File(imagepathArrayList[0])
                        var fileReqBody1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1)
                        var part1: MultipartBody.Part =
                            MultipartBody.Part.createFormData("upload", file1.name, fileReqBody1)

                        var file2: File = File(imagepathArrayList[1])
                        var fileReqBody2: RequestBody = RequestBody.create(MediaType.parse("image/*"), file2)
                        var part2: MultipartBody.Part =
                            MultipartBody.Part.createFormData("upload", file2.name, fileReqBody2)

                        var file3: File = File(imagepathArrayList[2])
                        var fileReqBody3: RequestBody = RequestBody.create(MediaType.parse("image/*"), file3)
                        var part3: MultipartBody.Part =
                            MultipartBody.Part.createFormData("upload", file3.name, fileReqBody3)

                        var file4: File = File(imagepathArrayList[3])
                        var fileReqBody4: RequestBody = RequestBody.create(MediaType.parse("image/*"), file4)
                        var part4: MultipartBody.Part =
                            MultipartBody.Part.createFormData("upload", file4.name, fileReqBody4)

                        forumClient.instance.uploadforum(
                            Email,
                            Nickname,
                            Title,
                            Subject,
                            Content,
                            ImageNum,
                            part1,
                            part2,
                            part3,
                            part4,
                            null
                        )
                            .enqueue(object : Callback<DefaultResponse> {
                                override fun onResponse(
                                    call: Call<DefaultResponse>,
                                    response: Response<DefaultResponse>
                                ) {
                                    if(response.body()!!.type)
                                    {
                                        saveComplete()
                                    }
                                    else
                                    {
                                        System.out.println("DB저장 에러 : ${response.body()!!.data}")
                                        saveFailed()
                                    }
                                }

                                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                    saveFailed()
                                }
                            })
                    }
                    5 -> {
                        var file1: File = File(imagepathArrayList[0])
                        var fileReqBody1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1)
                        var part1: MultipartBody.Part =
                            MultipartBody.Part.createFormData("upload", file1.name, fileReqBody1)

                        var file2: File = File(imagepathArrayList[1])
                        var fileReqBody2: RequestBody = RequestBody.create(MediaType.parse("image/*"), file2)
                        var part2: MultipartBody.Part =
                            MultipartBody.Part.createFormData("upload", file2.name, fileReqBody2)

                        var file3: File = File(imagepathArrayList[2])
                        var fileReqBody3: RequestBody = RequestBody.create(MediaType.parse("image/*"), file3)
                        var part3: MultipartBody.Part =
                            MultipartBody.Part.createFormData("upload", file3.name, fileReqBody3)

                        var file4: File = File(imagepathArrayList[3])
                        var fileReqBody4: RequestBody = RequestBody.create(MediaType.parse("image/*"), file4)
                        var part4: MultipartBody.Part =
                            MultipartBody.Part.createFormData("upload", file4.name, fileReqBody4)

                        var file5: File = File(imagepathArrayList[4])
                        var fileReqBody5: RequestBody = RequestBody.create(MediaType.parse("image/*"), file5)
                        var part5: MultipartBody.Part =
                            MultipartBody.Part.createFormData("upload", file5.name, fileReqBody5)

                        forumClient.instance.uploadforum(
                            Email,
                            Nickname,
                            Title,
                            Subject,
                            Content,
                            ImageNum,
                            part1,
                            part2,
                            part3,
                            part4,
                            part5
                        )
                            .enqueue(object : Callback<DefaultResponse> {
                                override fun onResponse(
                                    call: Call<DefaultResponse>,
                                    response: Response<DefaultResponse>
                                ) {
                                    if(response.body()!!.type)
                                    {
                                        saveComplete()
                                    }
                                    else
                                    {
                                        System.out.println("DB저장 에러 : ${response.body()!!.data}")
                                        saveFailed()
                                    }
                                }

                                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                    saveFailed()
                                }
                            })
                    }
                }
            }
        }

        WF_camerabutton.setOnClickListener {
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

                            if (imagepathArrayList.size >= 5) {
                                Toast.makeText(this@writeforum, "이미지는 5개까지 추가가 됩니다.", Toast.LENGTH_SHORT).show()
                            } else {
                                openGallery()
                            }
                        }
                    }
                }
                .create()
                .show()
        }

        WF_showimage1.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("알림")
            builder.setMessage("이미지를 지우시겠습니까?")
            builder.setNegativeButton("취소", null)
            builder.setPositiveButton("삭제") { dialogInterface, i ->
                imagepathArrayList.removeAt(0)
                setImage()
            }
            builder.show()
        }
        WF_showimage2.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("알림")
            builder.setMessage("이미지를 지우시겠습니까?")
            builder.setNegativeButton("취소", null)
            builder.setPositiveButton("삭제") { dialogInterface, i ->
                imagepathArrayList.removeAt(1)
                setImage()
            }
            builder.show()
        }
        WF_showimage3.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("알림")
            builder.setMessage("이미지를 지우시겠습니까?")
            builder.setNegativeButton("취소", null)
            builder.setPositiveButton("삭제") { dialogInterface, i ->
                imagepathArrayList.removeAt(2)
                setImage()
            }
            builder.show()
        }
        WF_showimage4.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("알림")
            builder.setMessage("이미지를 지우시겠습니까?")
            builder.setNegativeButton("취소", null)
            builder.setPositiveButton("삭제") { dialogInterface, i ->
                imagepathArrayList.removeAt(3)
                setImage()
            }
            builder.show()
        }
        WF_showimage5.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("알림")
            builder.setMessage("이미지를 지우시겠습니까?")
            builder.setNegativeButton("취소", null)
            builder.setPositiveButton("삭제") { dialogInterface, i ->
                imagepathArrayList.removeAt(4)
                setImage()
            }
            builder.show()
        }
    }

    private fun saveComplete() {
        Toast.makeText(this, "저장이 완료되었습니다.", Toast.LENGTH_LONG).show()
        val intent:Intent = Intent()
        setResult(Activity.RESULT_OK,intent)
        finish()
    }

    private fun saveFailed() {
        Toast.makeText(this, "저장이 실패했습니다.\n다시 시도해주세요", Toast.LENGTH_LONG).show()
        WF_savebutton.isEnabled = true
    }

    override fun onBackPressed() {
        val intent:Intent = Intent()
        setResult(Activity.RESULT_OK,intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                OPEN_GALLERY -> {
                    var currentImageUrl: Uri? = data?.data
                    var path = getPath(currentImageUrl!!)
                    imagepathArrayList.add(path)
                    setImage()
                }
            }
        } else {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
        }
    }

    private fun setImage() {
        when {
            imagepathArrayList.size == 0 -> {
                WF_showimage1.visibility = View.GONE
                WF_showimage2.visibility = View.GONE
                WF_showimage3.visibility = View.GONE
                WF_showimage4.visibility = View.GONE
                WF_showimage5.visibility = View.GONE
            }
            imagepathArrayList.size == 1 -> {
                setImagebitmap(WF_showimage1, imagepathArrayList[0])
                WF_showimage2.visibility = View.GONE
                WF_showimage3.visibility = View.GONE
                WF_showimage4.visibility = View.GONE
                WF_showimage5.visibility = View.GONE
            }
            imagepathArrayList.size == 2 -> {
                setImagebitmap(WF_showimage1, imagepathArrayList[0])
                setImagebitmap(WF_showimage2, imagepathArrayList[1])
                WF_showimage3.visibility = View.GONE
                WF_showimage4.visibility = View.GONE
                WF_showimage5.visibility = View.GONE
            }
            imagepathArrayList.size == 3 -> {
                setImagebitmap(WF_showimage1, imagepathArrayList[0])
                setImagebitmap(WF_showimage2, imagepathArrayList[1])
                setImagebitmap(WF_showimage3, imagepathArrayList[2])
                WF_showimage4.visibility = View.GONE
                WF_showimage5.visibility = View.GONE
            }
            imagepathArrayList.size == 4 -> {
                setImagebitmap(WF_showimage1, imagepathArrayList[0])
                setImagebitmap(WF_showimage2, imagepathArrayList[1])
                setImagebitmap(WF_showimage3, imagepathArrayList[2])
                setImagebitmap(WF_showimage4, imagepathArrayList[3])
                WF_showimage5.visibility = View.GONE
            }
            imagepathArrayList.size == 5 -> {
                setImagebitmap(WF_showimage1, imagepathArrayList[0])
                setImagebitmap(WF_showimage2, imagepathArrayList[1])
                setImagebitmap(WF_showimage3, imagepathArrayList[2])
                setImagebitmap(WF_showimage4, imagepathArrayList[3])
                setImagebitmap(WF_showimage5, imagepathArrayList[4])
            }
        }
    }

    private fun setImagebitmap(imageButton: ImageButton, path: String) {
        imageButton.visibility = View.VISIBLE
        val bitmap: Bitmap = BitmapFactory.decodeFile(path)
        imageButton.setImageBitmap(bitmap)
    }

    private fun getPath(uri: Uri): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor = contentResolver.query(uri, proj, null, null, null)
        var index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c.moveToFirst()
        return c.getString(index)
    }

    private fun openGallery() {
        val intent: Intent = Intent(Intent.ACTION_PICK)
        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, OPEN_GALLERY)
    }

    private fun setupPermissions() {
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
                    Toast.makeText(this@writeforum, "권한이 거부되었습니다", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}
