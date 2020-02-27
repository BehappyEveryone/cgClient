import androidx.appcompat.app.AppCompatActivity

//package com.example.chatground2
//
//import android.Manifest
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.content.SharedPreferences
//import android.content.pm.PackageManager
//import android.database.Cursor
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.net.Uri
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.provider.MediaStore
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.appcompat.app.AlertDialog
//import android.view.View
//import android.widget.ArrayAdapter
//import android.widget.ImageButton
//import android.widget.Toast
//import com.example.chatground2.Model.DefaultResponse
//import kotlinx.android.synthetic.main.activity_modifyforum.*
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.runBlocking
//import kotlinx.coroutines.withContext
//import okhttp3.MediaType
//import okhttp3.MultipartBody
//import okhttp3.RequestBody
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.io.File
//import java.io.FileNotFoundException
//import java.io.InputStream
//import java.net.HttpURLConnection
//import java.net.URL
//import java.util.ArrayList
//
class modifyforum : AppCompatActivity()
//
//
//class modifyforum : AppCompatActivity() {
//    private val OPEN_GALLERY = 1
//
//    private var F_idx: Int = 0
//    lateinit var image0:String
//    lateinit var image1:String
//    lateinit var image2:String
//    lateinit var image3:String
//    lateinit var image4:String
//    private lateinit var imagepathArrayList: ArrayList<String>
//    lateinit var arraAdapter: ArrayAdapter<CharSequence>
//    private lateinit var pref: SharedPreferences
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_modifyforum)
//
//        imagepathArrayList = ArrayList()
//
//        pref = getSharedPreferences("chatgroundlogin", Context.MODE_PRIVATE)
//        arraAdapter = ArrayAdapter.createFromResource(this, R.array.interest, android.R.layout.simple_spinner_item)
//        MF_subject.adapter = arraAdapter
//        MF_subject.setSelection(arraAdapter.getPosition(intent.getStringExtra("Subject")))
//
//        F_idx = intent.getIntExtra("idx", 0)
//        MF_title.setText(intent.getStringExtra("Title"))
//        MF_content.setText(intent.getStringExtra("Content"))
//
//        if(intent.hasExtra("Image0"))
//        {
//            image0 = intent.getStringExtra("Image0")
//            System.out.println("Image0 : $image0")
//            imagepathArrayList.add(image0)
//        }
//        if(intent.hasExtra("Image1"))
//        {
//            image1 = intent.getStringExtra("Image1")
//            System.out.println("Image1 : $image1")
//            imagepathArrayList.add(image1)
//        }
//        if(intent.hasExtra("Image2"))
//        {
//            image2 = intent.getStringExtra("Image2")
//            System.out.println("Image2 : $image2")
//            imagepathArrayList.add(image2)
//        }
//        if(intent.hasExtra("Image3"))
//        {
//            image3 = intent.getStringExtra("Image3")
//            System.out.println("Image3 : $image3")
//            imagepathArrayList.add(image3)
//        }
//        if(intent.hasExtra("Image4"))
//        {
//            image4 = intent.getStringExtra("Image4")
//            System.out.println("Image4 : $image4")
//            imagepathArrayList.add(image4)
//        }
//        setImage()
//
//        MF_camerabutton.setOnClickListener {
//            val items = arrayOf("이미지")
//            var dialog = AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
//            dialog.setTitle("이미지 첨부")
//                .setItems(items) { _, which ->
//                    val selected: String = items[which]
//                    if (selected == "이미지") {
//                        if (ContextCompat.checkSelfPermission(
//                                this,
//                                Manifest.permission.READ_EXTERNAL_STORAGE
//                            ) != PackageManager.PERMISSION_GRANTED
//                        ) {
//                            //권한이 허용되지 않음
//                            if (ActivityCompat.shouldShowRequestPermissionRationale(
//                                    this,
//                                    Manifest.permission.READ_EXTERNAL_STORAGE
//                                )
//                            ) {
//                                //이전에 이미 권한이 거부되었을 때 설명
//                                setupPermissions()
//                            } else {
//                                //권한요청
//                                ActivityCompat.requestPermissions(
//                                    this,
//                                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                                    100
//                                )
//                            }
//
//                        } else {
//                            //권한이 이미 허용됨
//
//                            if (imagepathArrayList.size >= 5) {
//                                Toast.makeText(this@modifyforum, "이미지는 5개까지 추가가 됩니다.", Toast.LENGTH_SHORT).show()
//                            } else {
//                                openGallery()
//                            }
//                        }
//                    }
//                }
//                .create()
//                .show()
//        }
//
//        MF_showimage1.setOnClickListener {
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle("알림")
//            builder.setMessage("이미지를 지우시겠습니까?")
//            builder.setNegativeButton("취소", null)
//            builder.setPositiveButton("삭제") { dialogInterface, i ->
//                imagepathArrayList.removeAt(0)
//                setImage()
//            }
//            builder.show()
//        }
//        MF_showimage2.setOnClickListener {
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle("알림")
//            builder.setMessage("이미지를 지우시겠습니까?")
//            builder.setNegativeButton("취소", null)
//            builder.setPositiveButton("삭제") { dialogInterface, i ->
//                imagepathArrayList.removeAt(1)
//                setImage()
//            }
//            builder.show()
//        }
//        MF_showimage3.setOnClickListener {
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle("알림")
//            builder.setMessage("이미지를 지우시겠습니까?")
//            builder.setNegativeButton("취소", null)
//            builder.setPositiveButton("삭제") { dialogInterface, i ->
//                imagepathArrayList.removeAt(2)
//                setImage()
//            }
//            builder.show()
//        }
//        MF_showimage4.setOnClickListener {
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle("알림")
//            builder.setMessage("이미지를 지우시겠습니까?")
//            builder.setNegativeButton("취소", null)
//            builder.setPositiveButton("삭제") { dialogInterface, i ->
//                imagepathArrayList.removeAt(3)
//                setImage()
//            }
//            builder.show()
//        }
//        MF_showimage5.setOnClickListener {
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle("알림")
//            builder.setMessage("이미지를 지우시겠습니까?")
//            builder.setNegativeButton("취소", null)
//            builder.setPositiveButton("삭제") { dialogInterface, i ->
//                imagepathArrayList.removeAt(4)
//                setImage()
//            }
//            builder.show()
//        }
//
//        MF_savebutton.setOnClickListener {
//            if (!MF_title.text.isEmpty() && !MF_content.text.isEmpty()) {
//                MF_savebutton.isEnabled = false
//
//                val Title: String = MF_title.text.toString()
//                val Subject: String = MF_subject.selectedItem.toString()
//                val Content: String = MF_content.text.toString()
//                val ImageNum: Int = imagepathArrayList.size
//                when (ImageNum) {
//                    0 -> {
//                        ApiClient.instance.modifyforum(
//                            F_idx,
//                            Title,
//                            Subject,
//                            Content,
//                            ImageNum,
//                            null,
//                            null,
//                            null,
//                            null,
//                            null
//                        )
//                            .enqueue(object : Callback<DefaultResponse> {
//                                override fun onResponse(
//                                    call: Call<DefaultResponse>,
//                                    response: Response<DefaultResponse>
//                                ) {
//                                    if (response.body()!!.type) {
//                                        saveComplete()
//                                    } else {
//                                        System.out.println("DB저장 에러 : ${response.body()!!.data}")
//                                        saveFailed()
//                                    }
//                                }
//
//                                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
//                                    saveFailed()
//                                    System.out.println("실패 : $t")
//                                }
//                            })
//                    }
//                    1 -> {
//                        lateinit var part1:MultipartBody.Part
//                        part1 = if(imagepathArrayList[0].subSequence(0,6) == "ForumsFragment") {
//                            MultipartBody.Part.createFormData("part1", imagepathArrayList[0])
//                        } else {
//                            var file1: File = File(imagepathArrayList[0])
//                            var fileReqBody1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1)
//                            MultipartBody.Part.createFormData("upload", file1.name, fileReqBody1)
//                        }
//
//                        ApiClient.instance.modifyforum(
//                            F_idx,
//                            Title,
//                            Subject,
//                            Content,
//                            ImageNum,
//                            part1,
//                            null,
//                            null,
//                            null,
//                            null
//                        )
//                            .enqueue(object : Callback<DefaultResponse> {
//                                override fun onResponse(
//                                    call: Call<DefaultResponse>,
//                                    response: Response<DefaultResponse>
//                                ) {
//                                    if (response.body()!!.type) {
//                                        saveComplete()
//                                    } else {
//                                        System.out.println("DB저장 에러 : ${response.body()!!.data}")
//                                        saveFailed()
//                                    }
//                                }
//
//                                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
//                                    saveFailed()
//                                    System.out.println("실패 : $t")
//                                }
//                            })
//                    }
//                    2 -> {
//                        lateinit var part1:MultipartBody.Part
//                        lateinit var part2:MultipartBody.Part
//                        part1 = if(imagepathArrayList[0].subSequence(0,6) == "ForumsFragment") {
//                            MultipartBody.Part.createFormData("part1", imagepathArrayList[0])
//                        } else {
//                            var file1: File = File(imagepathArrayList[0])
//                            var fileReqBody1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1)
//                            MultipartBody.Part.createFormData("upload", file1.name, fileReqBody1)
//                        }
//                        part2 = if(imagepathArrayList[1].subSequence(0,6) == "ForumsFragment") {
//                            MultipartBody.Part.createFormData("part2", imagepathArrayList[0])
//                        } else {
//                            var file1: File = File(imagepathArrayList[1])
//                            var fileReqBody1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1)
//                            MultipartBody.Part.createFormData("upload", file1.name, fileReqBody1)
//                        }
//
//                        ApiClient.instance.modifyforum(
//                            F_idx,
//                            Title,
//                            Subject,
//                            Content,
//                            ImageNum,
//                            part1,
//                            part2,
//                            null,
//                            null,
//                            null
//                        )
//                            .enqueue(object : Callback<DefaultResponse> {
//                                override fun onResponse(
//                                    call: Call<DefaultResponse>,
//                                    response: Response<DefaultResponse>
//                                ) {
//                                    if (response.body()!!.type) {
//                                        saveComplete()
//                                    } else {
//                                        System.out.println("DB저장 에러 : ${response.body()!!.data}")
//                                        saveFailed()
//                                    }
//                                }
//
//                                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
//                                    saveFailed()
//                                    System.out.println("실패 : $t")
//                                }
//                            })
//                    }
//                    3 -> {
//                        lateinit var part1:MultipartBody.Part
//                        lateinit var part2:MultipartBody.Part
//                        lateinit var part3:MultipartBody.Part
//                        part1 = if(imagepathArrayList[0].subSequence(0,6) == "ForumsFragment") {
//                            MultipartBody.Part.createFormData("part1", imagepathArrayList[0])
//                        } else {
//                            var file1: File = File(imagepathArrayList[0])
//                            var fileReqBody1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1)
//                            MultipartBody.Part.createFormData("upload", file1.name, fileReqBody1)
//                        }
//                        part2 = if(imagepathArrayList[1].subSequence(0,6) == "ForumsFragment") {
//                            MultipartBody.Part.createFormData("part2", imagepathArrayList[1])
//                        } else {
//                            var file1: File = File(imagepathArrayList[1])
//                            var fileReqBody1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1)
//                            MultipartBody.Part.createFormData("upload", file1.name, fileReqBody1)
//                        }
//                        part3 = if(imagepathArrayList[2].subSequence(0,6) == "ForumsFragment") {
//                            MultipartBody.Part.createFormData("part3", imagepathArrayList[2])
//                        } else {
//                            var file1: File = File(imagepathArrayList[2])
//                            var fileReqBody1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1)
//                            MultipartBody.Part.createFormData("upload", file1.name, fileReqBody1)
//                        }
//
//                        ApiClient.instance.modifyforum(
//                            F_idx,
//                            Title,
//                            Subject,
//                            Content,
//                            ImageNum,
//                            part1,
//                            part2,
//                            part3,
//                            null,
//                            null
//                        )
//                            .enqueue(object : Callback<DefaultResponse> {
//                                override fun onResponse(
//                                    call: Call<DefaultResponse>,
//                                    response: Response<DefaultResponse>
//                                ) {
//                                    if (response.body()!!.type) {
//                                        saveComplete()
//                                    } else {
//                                        System.out.println("DB저장 에러 : ${response.body()!!.data}")
//                                        saveFailed()
//                                    }
//                                }
//
//                                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
//                                    saveFailed()
//                                    System.out.println("실패 : $t")
//                                }
//                            })
//                    }
//                    4 -> {
//                        lateinit var part1:MultipartBody.Part
//                        lateinit var part2:MultipartBody.Part
//                        lateinit var part3:MultipartBody.Part
//                        lateinit var part4:MultipartBody.Part
//                        part1 = if(imagepathArrayList[0].subSequence(0,6) == "ForumsFragment") {
//                            MultipartBody.Part.createFormData("part1", imagepathArrayList[0])
//                        } else {
//                            var file1: File = File(imagepathArrayList[0])
//                            var fileReqBody1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1)
//                            MultipartBody.Part.createFormData("upload", file1.name, fileReqBody1)
//                        }
//                        part2 = if(imagepathArrayList[1].subSequence(0,6) == "ForumsFragment") {
//                            MultipartBody.Part.createFormData("part2", imagepathArrayList[1])
//                        } else {
//                            var file1: File = File(imagepathArrayList[1])
//                            var fileReqBody1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1)
//                            MultipartBody.Part.createFormData("upload", file1.name, fileReqBody1)
//                        }
//                        part3 = if(imagepathArrayList[2].subSequence(0,6) == "ForumsFragment") {
//                            MultipartBody.Part.createFormData("part3", imagepathArrayList[2])
//                        } else {
//                            var file1: File = File(imagepathArrayList[2])
//                            var fileReqBody1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1)
//                            MultipartBody.Part.createFormData("upload", file1.name, fileReqBody1)
//                        }
//                        part4 = if(imagepathArrayList[3].subSequence(0,6) == "ForumsFragment") {
//                            MultipartBody.Part.createFormData("part4", imagepathArrayList[3])
//                        } else {
//                            var file1: File = File(imagepathArrayList[3])
//                            var fileReqBody1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1)
//                            MultipartBody.Part.createFormData("upload", file1.name, fileReqBody1)
//                        }
//
//                        ApiClient.instance.modifyforum(
//                            F_idx,
//                            Title,
//                            Subject,
//                            Content,
//                            ImageNum,
//                            part1,
//                            part2,
//                            part3,
//                            part4,
//                            null
//                        )
//                            .enqueue(object : Callback<DefaultResponse> {
//                                override fun onResponse(
//                                    call: Call<DefaultResponse>,
//                                    response: Response<DefaultResponse>
//                                ) {
//                                    if (response.body()!!.type) {
//                                        saveComplete()
//                                    } else {
//                                        System.out.println("DB저장 에러 : ${response.body()!!.data}")
//                                        saveFailed()
//                                    }
//                                }
//
//                                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
//                                    saveFailed()
//                                    System.out.println("실패 : $t")
//                                }
//                            })
//                    }
//                    5 -> {
//                        lateinit var part1:MultipartBody.Part
//                        lateinit var part2:MultipartBody.Part
//                        lateinit var part3:MultipartBody.Part
//                        lateinit var part4:MultipartBody.Part
//                        lateinit var part5:MultipartBody.Part
//                        part1 = if(imagepathArrayList[0].subSequence(0,6) == "ForumsFragment") {
//                            MultipartBody.Part.createFormData("part1", imagepathArrayList[0])
//                        } else {
//                            var file1: File = File(imagepathArrayList[0])
//                            var fileReqBody1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1)
//                            MultipartBody.Part.createFormData("upload", file1.name, fileReqBody1)
//                        }
//                        part2 = if(imagepathArrayList[1].subSequence(0,6) == "ForumsFragment") {
//                            MultipartBody.Part.createFormData("part2", imagepathArrayList[1])
//                        } else {
//                            var file1: File = File(imagepathArrayList[1])
//                            var fileReqBody1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1)
//                            MultipartBody.Part.createFormData("upload", file1.name, fileReqBody1)
//                        }
//                        part3 = if(imagepathArrayList[2].subSequence(0,6) == "ForumsFragment") {
//                            MultipartBody.Part.createFormData("part3", imagepathArrayList[2])
//                        } else {
//                            var file1: File = File(imagepathArrayList[2])
//                            var fileReqBody1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1)
//                            MultipartBody.Part.createFormData("upload", file1.name, fileReqBody1)
//                        }
//                        part4 = if(imagepathArrayList[3].subSequence(0,6) == "ForumsFragment") {
//                            MultipartBody.Part.createFormData("part4", imagepathArrayList[3])
//                        } else {
//                            var file1: File = File(imagepathArrayList[3])
//                            var fileReqBody1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1)
//                            MultipartBody.Part.createFormData("upload", file1.name, fileReqBody1)
//                        }
//                        part5 = if(imagepathArrayList[4].subSequence(0,6) == "ForumsFragment") {
//                            MultipartBody.Part.createFormData("part5", imagepathArrayList[4])
//                        } else {
//                            var file1: File = File(imagepathArrayList[4])
//                            var fileReqBody1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1)
//                            MultipartBody.Part.createFormData("upload", file1.name, fileReqBody1)
//                        }
//
//                        ApiClient.instance.modifyforum(
//                            F_idx,
//                            Title,
//                            Subject,
//                            Content,
//                            ImageNum,
//                            part1,
//                            part2,
//                            part3,
//                            part4,
//                            part5
//                        )
//                            .enqueue(object : Callback<DefaultResponse> {
//                                override fun onResponse(
//                                    call: Call<DefaultResponse>,
//                                    response: Response<DefaultResponse>
//                                ) {
//                                    if (response.body()!!.type) {
//                                        saveComplete()
//                                    } else {
//                                        System.out.println("DB저장 에러 : ${response.body()!!.data}")
//                                        saveFailed()
//                                    }
//                                }
//
//                                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
//                                    saveFailed()
//                                    System.out.println("실패 : $t")
//                                }
//                            })
//                    }
//                }
//            }
//        }
//    }
//
//    private fun saveComplete() {
//        Toast.makeText(this, "수정이 완료되었습니다.", Toast.LENGTH_LONG).show()
//        val intent:Intent = Intent()
//        setResult(Activity.RESULT_OK,intent)
//        finish()
//    }
//
//    private fun saveFailed() {
//        Toast.makeText(this, "수정이 실패했습니다.\n다시 시도해주세요", Toast.LENGTH_LONG).show()
//        MF_savebutton.isEnabled = true
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (resultCode == Activity.RESULT_OK) {
//            when (requestCode) {
//                OPEN_GALLERY -> {
//                    var currentImageUrl: Uri? = data?.data
//                    var path = getPath(currentImageUrl!!)
//                    imagepathArrayList.add(path)
//                    setImage()
//                }
//            }
//        } else {
//            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    private fun setImage() {
//        when {
//            imagepathArrayList.size == 0 -> {
//                MF_showimage1.visibility = View.GONE
//                MF_showimage2.visibility = View.GONE
//                MF_showimage3.visibility = View.GONE
//                MF_showimage4.visibility = View.GONE
//                MF_showimage5.visibility = View.GONE
//            }
//            imagepathArrayList.size == 1 -> {
//                setImagebitmap(MF_showimage1, imagepathArrayList[0])
//                MF_showimage2.visibility = View.GONE
//                MF_showimage3.visibility = View.GONE
//                MF_showimage4.visibility = View.GONE
//                MF_showimage5.visibility = View.GONE
//            }
//            imagepathArrayList.size == 2 -> {
//                setImagebitmap(MF_showimage1, imagepathArrayList[0])
//                setImagebitmap(MF_showimage2, imagepathArrayList[1])
//                MF_showimage3.visibility = View.GONE
//                MF_showimage4.visibility = View.GONE
//                MF_showimage5.visibility = View.GONE
//            }
//            imagepathArrayList.size == 3 -> {
//                setImagebitmap(MF_showimage1, imagepathArrayList[0])
//                setImagebitmap(MF_showimage2, imagepathArrayList[1])
//                setImagebitmap(MF_showimage3, imagepathArrayList[2])
//                MF_showimage4.visibility = View.GONE
//                MF_showimage5.visibility = View.GONE
//            }
//            imagepathArrayList.size == 4 -> {
//                setImagebitmap(MF_showimage1, imagepathArrayList[0])
//                setImagebitmap(MF_showimage2, imagepathArrayList[1])
//                setImagebitmap(MF_showimage3, imagepathArrayList[2])
//                setImagebitmap(MF_showimage4, imagepathArrayList[3])
//                MF_showimage5.visibility = View.GONE
//            }
//            imagepathArrayList.size == 5 -> {
//                setImagebitmap(MF_showimage1, imagepathArrayList[0])
//                setImagebitmap(MF_showimage2, imagepathArrayList[1])
//                setImagebitmap(MF_showimage3, imagepathArrayList[2])
//                setImagebitmap(MF_showimage4, imagepathArrayList[3])
//                setImagebitmap(MF_showimage5, imagepathArrayList[4])
//            }
//        }
//    }
//
//    private fun setImagebitmap(imageButton: ImageButton, path: String) {
//        imageButton.visibility = View.VISIBLE
//        imageButton.setImageBitmap(connectimage(path))
//    }
//
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
//            System.out.println("경로 : $imagpath")
//            bitmap = try {
//                BitmapFactory.decodeFile(imagpath)
//            }catch (e: IllegalAccessException) {
//                BitmapFactory.decodeResource(this.resources,R.drawable.noimage)
//            }
//        }
//        return bitmap
//    }
//
//    private fun getPath(uri: Uri): String {
//        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
//        var c: Cursor = contentResolver.query(uri, proj, null, null, null)
//        var index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//        c.moveToFirst()
//        return c.getString(index)
//    }
//
//    private fun openGallery() {
//        val intent: Intent = Intent(Intent.ACTION_PICK)
//        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
//        startActivityForResult(intent, OPEN_GALLERY)
//    }
//
//    private fun setupPermissions() {
//        //스토리지 읽기 퍼미션을 permission 변수에 담는다
//        val permission = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        )
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            makeRequest()
//        }
//    }
//
//    private fun makeRequest() {
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//            100
//        )
//    }
//
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        when (requestCode) {
//            100 -> {
////                if(grantResults.isNotEmpty()
////                            && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this@modifyforum, "권한이 거부되었습니다", Toast.LENGTH_SHORT).show()
//                }
//                return
//            }
//        }
//    }
//}
