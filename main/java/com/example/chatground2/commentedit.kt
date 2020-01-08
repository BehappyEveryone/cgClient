package com.example.chatground2

import android.Manifest
import android.app.Activity
import android.content.Intent
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
import android.app.AlertDialog;
import android.view.Window
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_commentedit.*
import kotlinx.android.synthetic.main.activity_readforum.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class commentedit : Activity() {

    private var idx: Int = -1
    private lateinit var type: String
    private lateinit var uid: String
    private var recommentuid: String? = null
    private lateinit var content: String
    private var IsImage: Boolean = false
    private var Imagepath: String? = null
    private val OPEN_GALLERY = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commentedit)

        idx = intent.getIntExtra("idx", -1)
        type = intent.getStringExtra("type")
        uid = intent.getStringExtra("uid")
        recommentuid = intent.getStringExtra("recommentuid")
        content = intent.getStringExtra("content")
        IsImage = intent.getBooleanExtra("IsImage", false)
        Imagepath = intent.getStringExtra("Imagepath")

        edit_commentbox?.setText(content)
        if (IsImage) {
            editcamera.setImageBitmap(connectimage(Imagepath!!))
        }

        editcamera.setOnClickListener {
            if(IsImage)
            {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("알림")
                builder.setMessage("이미지를 지우시겠습니까?")
                builder.setNegativeButton("취소", null)
                builder.setPositiveButton("삭제") { dialogInterface, i ->
                    IsImage = false
                    Imagepath = null
                    editcamera.background = ContextCompat.getDrawable(this, R.drawable.writeforum_camera_icon)
                    editcamera.setImageBitmap(null)
                }
                builder.show()
            }
            else
            {
                openGallery()
            }
        }
    }

    private fun connectimage(imagpath: String): Bitmap {
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
        } catch (e: FileNotFoundException) {
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.noimage)
        }
        return bitmap
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
                    Toast.makeText(this, "권한이 거부되었습니다", Toast.LENGTH_SHORT).show()
                }
                return
            }
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
                    editcamera.setImageBitmap(bitmap)
                    editcamera.background = null

                    Imagepath = path
                    IsImage = true
                }
            }
        } else {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
        }
    }

}
