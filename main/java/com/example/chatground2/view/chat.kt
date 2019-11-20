package com.example.chatground2.view

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
import android.os.Handler
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Base64
import android.view.View
import android.widget.Toast
import com.example.chatground2.R
import com.example.chatground2.api.socket
import com.example.chatground2.chatAdapter
import com.example.chatground2.model.chatitemdata
import io.socket.client.Socket
import java.net.URISyntaxException
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.coroutines.*
import org.json.JSONObject
import org.json.JSONException
import java.io.*
import java.nio.ByteBuffer
import java.util.*


class chat : AppCompatActivity() {
    private var mSocket: Socket = socket.mSocket
    private lateinit var pref: SharedPreferences
    private lateinit var authUserEmail: String
    private lateinit var authUserNickname: String
    private var items: ArrayList<chatitemdata> = ArrayList()
    private var handler: Handler = Handler()
    private lateinit var r: Runnable
    private val OPEN_GALLERY = 1
    private val OPEN_VIDEOGALLERY = 2

    private lateinit var offersubjectthread:Job

    private lateinit var editor: SharedPreferences.Editor

    override fun onStop() {
        super.onStop()

        System.out.println("chat Stop")
    }

    override fun onPause() {
        super.onPause()

        System.out.println("chat Pause")
    }

    override fun onDestroy() {
        super.onDestroy()

        val intent:Intent = Intent()
        setResult(Activity.RESULT_OK,intent)
        System.out.println("chat Destroy")

        mSocket.off("OfferSubject",OfferSubject)
        mSocket.off("VoteComplete", VoteComplete)
        mSocket.off("OpinionAgreeResult", OpinionAgreeResult)
        mSocket.off("talk",talk)
        mSocket.off("silent",silent)
        mSocket.off("verifyResponse", ontokenReceived)
        mSocket.off("ServerMessage", ServerMessage)
        mSocket.off("ServerVideoMessage", ServerVideoMessage)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        System.out.println("chat Create 채팅방 만듬")

        pref = this.getSharedPreferences("chatgroundlogin", Context.MODE_PRIVATE)
        editor = pref.edit()

        val mAdapter = chatAdapter(this, items, pref)

        r = Runnable { mAdapter.notifyDataSetChanged() }

        val lm = LinearLayoutManager(this)
        chatRecycle.adapter = mAdapter
        chatRecycle.layoutManager = lm
        chatRecycle.setHasFixedSize(true)

        try {
            mSocket.on("OfferSubject",OfferSubject)
            mSocket.on("VoteComplete", VoteComplete)
            mSocket.on("OpinionAgreeResult", OpinionAgreeResult)
            mSocket.on("talk",talk)
            mSocket.on("silent",silent)
            mSocket.on("verifyResponse", ontokenReceived)
            mSocket.on("ServerMessage", ServerMessage)
            mSocket.on("ServerVideoMessage", ServerVideoMessage)
            mSocket.emit("joinroomsucces")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        chatsendMessage.setOnClickListener {
            if (chatinputMessage.text.toString().replace(" ", "") != "") {
                var content = chatinputMessage.text.toString()

                var data: JSONObject = JSONObject().put("type", "Client").put("data", "text").put("content", content)
                    .put("email", pref.getString("UserEmail", "Logout"))
                    .put("nickname", pref.getString("UserNickname", "Logout"))
                mSocket.emit("ClientMessage", data)

                chatinputMessage.setText("")
            } else {
                Toast.makeText(this@chat, "내용을 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        }

        chatplus.setOnClickListener {

            val items = arrayOf("이미지", "동영상")

            var dialog = AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
            dialog.setTitle("이미지 또는 동영상 전송")
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
                    } else {
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
                            openVideoGallery()
                        }
                    }
                }
                .create()
                .show()
        }

        chatagree.setOnClickListener {
            val data: JSONObject = JSONObject().put("Email", pref.getString("UserEmail", "Logout")).put("Opinion","agree")
            mSocket.emit("AgreeOrOppose",data)
            chatOpinion.visibility = View.GONE
        }
        chatoppose.setOnClickListener {
            val data: JSONObject = JSONObject().put("Email", pref.getString("UserEmail", "Logout")).put("Opinion","oppose")
            mSocket.emit("AgreeOrOppose",data)
            chatOpinion.visibility = View.GONE
        }
    }

    private val OpinionAgreeResult:Emitter.Listener = Emitter.Listener { args ->
        lateinit var time:String
        lateinit var opinionobject:JSONObject

        val getData:Job = CoroutineScope(Dispatchers.IO).launch {
            val receivedData = args[0] as JSONObject

            time = receivedData.get("time").toString()
            opinionobject = receivedData.getJSONObject("result")
        }

        CoroutineScope(Dispatchers.Main).launch {
            getData.join()

            if(opinionobject.get(pref.getString("UserEmail", "Logout")) == "agree")
            {
                chatinputMessage.isEnabled = true
                chatsendMessage.isEnabled = true
                chatplus.isEnabled = true
            }

            for(i:Int in time.toInt()/1000 downTo 0)
            {
                var sec:Int = (time.toInt()/1000)%60
                var min:Int = (time.toInt()/1000)/60
                when {
                    min == 0 -> when {
                        sec == 0//0분 0초
                        -> {
                            chatinputMessage.isEnabled = false
                            chatsendMessage.isEnabled = false
                            chatplus.isEnabled = false
                            mSocket.emit("AgreeOpinionComplete")
                        }
                        sec < 10 -> chatTime?.text = "00:0$sec"
                        else -> chatTime?.text = "00:$sec"
                    }
                    min < 10 -> when {
                        sec < 10 -> chatTime?.text = "0$min:0$sec"
                        else -> chatTime?.text = "0$min:$sec"
                    }
                    else -> when {
                        sec < 10 -> chatTime?.text = "$min:0$sec"
                        else -> chatTime?.text = "$min:$sec"
                    }
                }
                delay(1000)
            }
        }
    }

    private val talk:Emitter.Listener = Emitter.Listener { args ->
        chatinputMessage.isEnabled = true
        chatsendMessage.isEnabled = true
        chatplus.isEnabled = true
    }

    private val silent:Emitter.Listener = Emitter.Listener { args ->
        chatinputMessage.isEnabled = false
        chatsendMessage.isEnabled = false
        chatplus.isEnabled = false
    }

    private val VoteComplete:Emitter.Listener = Emitter.Listener { args ->
        if(offersubjectthread.isActive)
        {
            offersubjectthread.cancel()
            CoroutineScope(Dispatchers.Main).launch {
                chatTime?.text = "00:00"
                chatinputMessage.isEnabled = true
                chatsendMessage.isEnabled = true
                chatplus.isEnabled = true
            }
        }
    }

    private val OfferSubject = Emitter.Listener { args ->
        // 전달받은 데이터는 아래와 같이 추출할 수 있습니다.
        // your code...
        lateinit var subject:String
        lateinit var time:String

        val getData:Job = CoroutineScope(Dispatchers.IO).launch {
            val receivedData = args[0] as JSONObject

            subject = receivedData.get("subject").toString()
            time = receivedData.get("time").toString()
        }

        offersubjectthread = CoroutineScope(Dispatchers.Main).launch {
            getData.join()

            chatinputMessage.isEnabled = false
            chatsendMessage.isEnabled = false
            chatplus.isEnabled = false

            for(i:Int in 0..subject.length)
            {
                chatSubject?.text = subject.subSequence(0,i)
                delay(100)
            }

            chatOpinion.visibility = View.VISIBLE
            for(i:Int in time.toInt()/1000 downTo 0)
            {
                if(i == 0)
                {
                    if(chatOpinion.visibility == View.VISIBLE)
                    {
                        val data: JSONObject = JSONObject().put("Email", pref.getString("UserEmail", "Logout")).put("Opinion","giveup")
                        mSocket.emit("AgreeOrOppose",data)
                        chatOpinion.visibility = View.GONE
                    }
                }
                if(i < 10)
                {
                    chatTime?.text = "00:0$i"
                }
                else
                {
                    chatTime?.text = "00:$i"
                }
                delay(1000)
            }
        }
    }

    private val ontokenReceived = Emitter.Listener { args ->
        // 전달받은 데이터는 아래와 같이 추출할 수 있습니다.
        val receivedData = args[0] as JSONObject
        // your code...

        System.out.println("토큰 리스폰 : $receivedData")

        if (receivedData.get("token") == "Logout" || receivedData.get("token") == "Failed") {
            editor.clear()
            editor.commit()
            mSocket.disconnect()
            finish()
            startActivity(Intent(this@chat, MainActivity::class.java))
        } else {
            authUserEmail = receivedData.getJSONObject("token").get("Email").toString()
            authUserNickname = receivedData.getJSONObject("token").get("Nickname").toString()
        }
    }

    private val ServerMessage = Emitter.Listener { args ->
        // 전달받은 데이터는 아래와 같이 추출할 수 있습니다.
        val receivedData = args[0] as JSONObject
        // your code...

        System.out.println("메시지데이터 : $receivedData")

        val type = receivedData.get("type").toString()
        val data = receivedData.get("data").toString()
        val content = receivedData.get("content").toString()
        val email = receivedData.get("email").toString()
        val nickname = receivedData.get("nickname").toString()
        val date = receivedData.get("date").toString()

        items.add(chatitemdata(type, data, email, nickname, content, date))
        handler.post(r)
    }

    private val ServerVideoMessage = Emitter.Listener { args ->
        // 전달받은 데이터는 아래와 같이 추출할 수 있습니다.
        val receivedData = args[0] as JSONObject
        val bynarydata: ByteArray = args[1] as ByteArray
        // your code...

        val file:File = ConvertBytetoVideo(bynarydata)

        val type = receivedData.get("type").toString()
        val data = receivedData.get("data").toString()
        val content = file.path
        val email = receivedData.get("email").toString()
        val nickname = receivedData.get("nickname").toString()
        val date = receivedData.get("date").toString()

        items.add(chatitemdata(type, data, email, nickname, content, date))
        handler.post(r)
    }

    private fun openGallery() {
        val intent: Intent = Intent(Intent.ACTION_PICK)
        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, OPEN_GALLERY)
    }

    private fun openVideoGallery() {
        val uri: Uri = Uri.parse("content://media/external/images/media")
        val intent: Intent = Intent(Intent.ACTION_VIEW, uri)
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "video/*"
        startActivityForResult(intent, OPEN_VIDEOGALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                OPEN_GALLERY -> {
                    var currentImageUrl: Uri? = data?.data
                    var path = getPath(currentImageUrl!!)
                    sendImage(path)
                }
                OPEN_VIDEOGALLERY -> {
                    var currentVideoUrl: Uri? = data?.data
                    System.out.println("비디오 uri : $currentVideoUrl")
                    var path = getvideoPath(currentVideoUrl!!)
                    var file: File = File(path)
                    sendVideo(file)
                }
            }
        } else {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
        }
    }

    private fun sendImage(path: String) {
        try {
            val data: JSONObject =
                JSONObject().put("type", "Client").put("data", "image").put("content", encodeImage(path))
                    .put("email", pref.getString("UserEmail", "Logout"))
                    .put("nickname", pref.getString("UserNickname", "Logout"))
            mSocket.emit("ClientMessage", data)
        } catch (e: JSONException) {
            System.out.println(e)
        }
    }

    private fun sendVideo(file: File) {
        try {
            val byteArray: ByteArray = ConvertVideotoByte(file)
            val data: JSONObject = JSONObject().put("type", "Client").put("data", "video")
                .put("email", pref.getString("UserEmail", "Logout"))
                .put("nickname", pref.getString("UserNickname", "Logout"))
            mSocket.emit("ClientMessagevideo", data, byteArray)
        } catch (e: JSONException) {
            System.out.println(e)
        }
    }

    private fun getvideoPath(uri: Uri): String {
        var id: String = DocumentsContract.getDocumentId(uri).split(":")[1]
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var selection: String = MediaStore.Files.FileColumns._ID + " = " + id
        var c: Cursor = contentResolver.query(MediaStore.Files.getContentUri("external"), proj, selection, null, null)
        var index = c.getColumnIndexOrThrow(proj[0])
        c.moveToFirst()
        return c.getString(index)
    }

    private fun getPath(uri: Uri): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor = contentResolver.query(uri, proj, null, null, null)
        var index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c.moveToFirst()
        return c.getString(index)
    }

    private fun ConvertVideotoByte(file: File): ByteArray {
        var fis: FileInputStream? = null

        try {
            fis = FileInputStream(file);
        } catch (e: FileNotFoundException) {
            e.printStackTrace();
        }

        return fis.use { input ->
            input!!.readBytes()
        }
//        val videoBytes = FileInputStream(file).use { input -> input.readBytes() }
    }

    private fun ConvertBytetoVideo(binarydata: ByteArray):File {
        var fis: FileOutputStream? = null

        var outputDir:File = this.cacheDir
        var ouputFile:File = File.createTempFile("prefix","extension",outputDir)

        try {
            fis = FileOutputStream(ouputFile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace();
        }

        fis.use { output ->
            output!!.write(binarydata)
        }

        return ouputFile
    }

    private fun encodeImage(path: String): String {
        val imagefile: File = File(path)
        var fis: FileInputStream? = null

        try {
            fis = FileInputStream(imagefile);
        } catch (e: FileNotFoundException) {
            e.printStackTrace();
        }

        val bm: Bitmap = BitmapFactory.decodeStream(fis)
        val baos: ByteArrayOutputStream = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()

        return Base64.encodeToString(b, Base64.DEFAULT)
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
                    Toast.makeText(this@chat, "권한이 거부되었습니다", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    override fun onBackPressed() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("정말 채팅방을 나가시겠습니까?")
            .setPositiveButton("예") { _, which ->
                val intent:Intent = Intent()
                setResult(Activity.RESULT_OK,intent)
                super.onBackPressed()
            }
            .setNegativeButton("아니오") { _, which ->
                return@setNegativeButton
            }
            .create()
            .show()
    }
}
