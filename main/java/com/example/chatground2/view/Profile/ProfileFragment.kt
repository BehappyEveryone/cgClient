package com.example.chatground2.view.Profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatground2.R

class ProfileFragment : Fragment()
//    ,CoroutineScope
{

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
//    private var mSocket: Socket = socket.mSocket
//
//    private val OPEN_GALLERY = 40
//    private lateinit var loginpref: SharedPreferences
//    private lateinit var loginedit: SharedPreferences.Editor
//    private lateinit var profilepref: SharedPreferences
//    private lateinit var profileedit: SharedPreferences.Editor
//
//    lateinit var arraAdapter: ArrayAdapter<CharSequence>
//
//    private var PROFILE_CHANGE: Int = 0
//    private lateinit var profilefile: File
//
//    private var job:Job = Job()
//
//    override val coroutineContext: CoroutineContext
//        get() = Dispatchers.Main + job
//
//    override fun onPause() {
//        super.onPause()
//
//        job.cancel()
//        job = Job()
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        retainInstance = true
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//
//        mSocket.off("returnUser", returnUser)
//        mSocket.off("SaveProfile", SaveProfile)
//    }
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//
//        var view: View = inflater.inflate(R.layout.fragment_profile, container, false)
//
//        arraAdapter = ArrayAdapter.createFromResource(context,
//            R.array.interest, android.R.layout.simple_spinner_item)
//
//        PROFILE_CHANGE = 0
//        loginpref = activity!!.getSharedPreferences("chatgroundlogin", Context.MODE_PRIVATE)
//        loginedit = loginpref.edit()
//        profilepref = activity!!.getSharedPreferences("chatgroundprofile", Context.MODE_PRIVATE)
//        profileedit = profilepref.edit()
//
//        view.P_Interest1.adapter = arraAdapter
//        view.P_Interest2.adapter = arraAdapter
//        view.P_Interest3.adapter = arraAdapter
//
//        mSocket.on("returnUser", returnUser)
//        mSocket.on("SaveProfile", SaveProfile)
//        mSocket.emit("returnUser", loginpref.getString("UserEmail", "Logout"))
//
//        view.P_save.setOnClickListener {
//            when (PROFILE_CHANGE) {
//                0//프로필 안바꿨을 때
//                -> {
//                    var introduce = view.P_myintruduce.text
//                    var interest1 = view.P_Interest1.selectedItem.toString()
//                    var interest2 = view.P_Interest2.selectedItem.toString()
//                    var interest3 = view.P_Interest3.selectedItem.toString()
//                    var obj: JSONObject =
//                        JSONObject().put("introduce", introduce).put("interset1", interest1).put("interset2", interest2)
//                            .put("interset3", interest3).put("Email", loginpref.getString("UserEmail", "Logout"))
//
//                    mSocket.emit("SaveProfile", obj)
//                }
//                1//프로필 바꿨을 때
//                -> {
//                    sendImage(profilefile)
//
//                    var introduce = view.P_myintruduce.text
//                    var interest1 = view.P_Interest1.selectedItem.toString()
//                    var interest2 = view.P_Interest2.selectedItem.toString()
//                    var interest3 = view.P_Interest3.selectedItem.toString()
//                    var obj: JSONObject =
//                        JSONObject().put("introduce", introduce).put("interset1", interest1).put("interset2", interest2)
//                            .put("interset3", interest3).put("Email", loginpref.getString("UserEmail", "Logout"))
//
//                    mSocket.emit("SaveProfile", obj)
//                }
//                2//기본 이미지로 바꿨을 떄
//                -> {
//                    mSocket.emit("defaultProfile", loginpref.getString("UserEmail", "Logout"))
//
//                    var introduce = view.P_myintruduce.text
//                    var interest1 = view.P_Interest1.selectedItem.toString()
//                    var interest2 = view.P_Interest2.selectedItem.toString()
//                    var interest3 = view.P_Interest3.selectedItem.toString()
//                    var obj: JSONObject =
//                        JSONObject().put("introduce", introduce).put("interest1", interest1).put("interest2", interest2)
//                            .put("interest3", interest3).put("Email", loginpref.getString("UserEmail", "Logout"))
//
//                    mSocket.emit("SaveProfile", obj)
//                }
//            }
//        }
//
//        view.P_logoutbutton.setOnClickListener {
//            var dialog = AlertDialog.Builder(activity!!, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
//            dialog.setTitle("로그아웃")
//                .setMessage("정말 로그아웃 하시겠습니까?")
//                .setPositiveButton("확인") { _, which ->
//                    loginedit.clear()
//                    loginedit.commit()
//                    mSocket.disconnect()
//                    activity!!.finish()
//                    startActivity(Intent(activity, MainActivity::class.java))
//                }
//                .setNegativeButton("취소") { _, which ->
//                    Toast.makeText(context, "취소하셨습니다", Toast.LENGTH_SHORT).show()
//                }
//                .create()
//                .show()
//        }
//
//        view.P_image.setOnClickListener {
//            val items = arrayOf("프로필 변경", "기본 이미지 변경")
//
//            var dialog = AlertDialog.Builder(activity!!, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
//            dialog.setTitle("프로필 설정")
//                .setItems(items) { _, which ->
//                    val selected: String = items[which]
//                    if (selected == "프로필 변경") {
//                        if (ContextCompat.checkSelfPermission(
//                                view.context,
//                                Manifest.permission.READ_EXTERNAL_STORAGE
//                            ) != PackageManager.PERMISSION_GRANTED
//                        ) {
//                            //권한이 허용되지 않음
//                            if (ActivityCompat.shouldShowRequestPermissionRationale(
//                                    view.context as Activity,
//                                    Manifest.permission.READ_EXTERNAL_STORAGE
//                                )
//                            ) {
//                                //이전에 이미 권한이 거부되었을 때 설명
//                                setupPermissions(view.context)
//                            } else {
//                                //권한요청
//                                ActivityCompat.requestPermissions(
//                                    view.context as Activity,
//                                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                                    100
//                                )
//                            }
//
//                        } else {
//                            //권한이 이미 허용됨
//                            openGallery()
//                        }
//                    } else {
//                        P_image.setImageResource(R.drawable.defaultprofile)
//                        PROFILE_CHANGE = 2
//                    }
//                }
//                .create()
//                .show()
//        }
//
//        view.P_Editmyintruduce.setOnClickListener {
//            val et: EditText = EditText(context)
//            var dialog = AlertDialog.Builder(activity!!, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
//            dialog.setMessage("자기소개 입력창")
//                .setView(et)
//                .setPositiveButton("확인") { _, which ->
//                    view.P_myintruduce.text = et.text.toString()
//                }
//                .setNegativeButton("취소") { _, which ->
//                    Toast.makeText(context, "취소하셨습니다", Toast.LENGTH_SHORT).show()
//                }
//                .create()
//                .show()
//        }
//        return view
//    }
//
//    private val returnUser = Emitter.Listener { args ->
//
//        lateinit var email: String
//        lateinit var nickname: String
//        lateinit var profilepath: String
//        lateinit var introduce: String
//        lateinit var interest1: String
//        lateinit var interest2: String
//        lateinit var interest3: String
//
//        val mainscope = CoroutineScope(Dispatchers.Main)
//        val scope = CoroutineScope(Dispatchers.IO)
//        val imagescope = CoroutineScope(Dispatchers.IO)
//
//        job = scope.launch {
//            val receivedData = args[0] as JSONObject
//
//            System.out.println("리시브데이터: $receivedData")
//
//            if (receivedData.get("type") == true) {
//                email = receivedData.getJSONObject("data").get("Email").toString()
//                nickname = receivedData.getJSONObject("data").get("Nickname").toString()
//                profilepath = try {
//                    receivedData.getJSONObject("data").get("Profile").toString()
//                } catch (e: JSONException) {
//                    ""
//                }
//                introduce = try {
//                    receivedData.getJSONObject("data").get("Introduce").toString()
//                } catch (e: JSONException) {
//                    ""
//                }
//                interest1 = try {
//                    receivedData.getJSONObject("data").get("Hobbit1").toString()
//                } catch (e: JSONException) {
//                    "기타"
//                }
//                interest2 = try {
//                    receivedData.getJSONObject("data").get("Hobbit2").toString()
//                } catch (e: JSONException) {
//                    "기타"
//                }
//                interest3 = try {
//                    receivedData.getJSONObject("data").get("Hobbit3").toString()
//                } catch (e: JSONException) {
//                    "기타"
//                }
//            }
//        }
//
//        val mainscopeComplete = mainscope.launch {
//            job.join()
//
//            P_email?.text = email
//            P_nickname?.text = nickname
//            P_myintruduce?.text = introduce
//
//            P_Interest1?.setSelection(arraAdapter.getPosition(interest1))
//            P_Interest2?.setSelection(arraAdapter.getPosition(interest2))
//            P_Interest3?.setSelection(arraAdapter.getPosition(interest3))
//        }
//
//        val imageScope = imagescope.launch {
//            mainscopeComplete.join()
//
//            if (profilepath == "" || profilepath == "null") {
//                withContext(Dispatchers.Main)
//                {
//                    System.out.println("profilepath : $profilepath")
//                    P_image?.setImageResource(R.drawable.defaultprofile)
//                }
//            } else {
//                val url: URL = URL(ipadress.ipadress + profilepath)
//
//                val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
//                conn.doInput = true
//                conn.connect()
//                val ins: InputStream = conn.inputStream
//                val bitmap = BitmapFactory.decodeStream(ins)
//
//                withContext(Dispatchers.Main)
//                {
//                    P_image?.setImageBitmap(bitmap)
//                }
//            }
//        }
//    }
//
//    private val SaveProfile = Emitter.Listener { args ->
//        val scope = CoroutineScope(Dispatchers.IO)
//        scope.launch {
//            val receivedData = args[0] as JSONObject
//            // your code...
//
//            if (receivedData.get("result") == true) {
//                withContext(Dispatchers.Main)
//                {
//                    Toast.makeText(context, "프로필 변경 성공", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                withContext(Dispatchers.Main)
//                {
//                    Toast.makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//        val receivedData = args[0] as JSONObject
//    }
//
//    private fun setupPermissions(context: Context) {
//        //스토리지 읽기 퍼미션을 permission 변수에 담는다
//        val permission = ContextCompat.checkSelfPermission(
//            context,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        )
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            makeRequest(context)
//        }
//    }
//
//    private fun makeRequest(context: Context) {
//        ActivityCompat.requestPermissions(
//            context as Activity,
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
//                    Toast.makeText(context, "권한이 거부되었습니다", Toast.LENGTH_SHORT).show()
//                } else {
//                    openGallery()
//                }
//                return
//            }
//        }
//    }
//
//    private fun openGallery() {
//        val intent: Intent = Intent(Intent.ACTION_PICK)
//        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
//        activity!!.startActivityForResult(intent, OPEN_GALLERY)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == OPEN_GALLERY) {
//            if (resultCode == Activity.RESULT_OK) {
//                var currentImageUrl: Uri? = data?.data
//                var path = getPath(currentImageUrl!!)
//
//                profilefile = File(path)
//                PROFILE_CHANGE = 1
//
//                setImage(P_image, path)
//            }
//            else {
//                Toast.makeText(context, "취소 되었습니다.", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    private fun setImage(imageButton: ImageButton, path: String) {
//        val bitmap: Bitmap = BitmapFactory.decodeFile(path)
//        imageButton.setImageBitmap(bitmap)
//    }
//
//    private fun sendImage(imageFile: File) {
//        var fileReqBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile)
//        var part: MultipartBody.Part = MultipartBody.Part.createFormData("upload", imageFile.getName(), fileReqBody)
//
//        System.out.println("이미지 실행")
//
//        ApiClient.instance.uploadprofile(loginpref.getString("UserEmail", "Logout"), part)
//            .enqueue(object : Callback<DefaultResponse> {
//                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
//                    if (response.body()!!.type) {
//                        Imagechange(P_image, response.body()!!.data)
//                        profileedit.putString(
//                            loginpref.getString("UserEmail", "Logout") + "ProfileFragment",
//                            response.body()!!.data
//                        )
//                        profileedit.commit()
//                    } else {
//                        Toast.makeText(context!!.applicationContext, "이미지 업로드 실패\n다시 시도해주세요", Toast.LENGTH_LONG).show()
//                        System.out.println("이미지 오류 : " + response.body()!!.data)
//                    }
//                }
//
//                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
//                    System.out.println("이미지 오류 : $t")
//                    Toast.makeText(context!!.applicationContext, "이미지 업로드 실패\n다시 시도해주세요", Toast.LENGTH_LONG).show()
//                }
//            })
//    }
//
//    private fun getPath(uri: Uri): String {
//        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
//        var c: Cursor = activity!!.contentResolver.query(uri, proj, null, null, null)
//        var index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//        c.moveToFirst()
//        return c.getString(index)
//    }
//
//    private fun Imagechange(Imageview: ImageView, imagpath: String) {
//        System.out.println("이미지 경로 : $imagpath")
//
//        lateinit var bitmap: Bitmap
//
//        runBlocking {
//            launch {
//                withContext(Dispatchers.IO)
//                {
//                    val url: URL = URL(ipadress.ipadress + imagpath)
//
//                    val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
//                    conn.doInput = true
//                    conn.connect()
//                    val ins: InputStream = conn.inputStream
//                    bitmap = BitmapFactory.decodeStream(ins)
//                }
//            }
//        }
//        Imageview.setImageBitmap(bitmap)
//        System.out.println("런성공")
//    }
}