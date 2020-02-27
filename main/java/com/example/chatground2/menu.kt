//package com.example.chatground2
//
//import android.content.Context
//import android.content.Intent
//import android.content.SharedPreferences
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import com.google.android.material.bottomnavigation.BottomNavigationView
//import androidx.fragment.app.FragmentTransaction
//import androidx.appcompat.app.AlertDialog
//import android.view.MenuItem
//import com.example.chatground2.Api.socket
//import com.example.chatground2.view.Forums.ForumsFragment
//import com.example.chatground2.view.mainActivity.MainActivity
//import com.example.chatground2.view.Profile.ProfileFragment
//import com.example.chatground2.view.chat
//import io.socket.client.IO
//import io.socket.client.Socket
//import io.socket.emitter.Emitter
//import kotlinx.android.synthetic.main.activity_menu.*
//import kotlinx.coroutines.*
//import org.json.JSONObject
//import java.net.URISyntaxException
//
//
//class menu : AppCompatActivity() {
//
//    private lateinit var pref: SharedPreferences
//    private lateinit var editor: SharedPreferences.Editor
//
//    private lateinit var CurrentStatepref: SharedPreferences
//    private lateinit var CurrentStateeditor: SharedPreferences.Editor
//
//    private var mSocket: Socket = socket.mSocket
//    private val opts:IO.Options = socket.opts
//
//    var forumsFragment = ForumsFragment()
//    var gamereadyFragment = gameready()
//    var profileFragment = ProfileFragment()
//
//    override fun onStop() {
//        super.onStop()
//
//        mSocket.emit("findroomcancel","findroomcancel")
//        CurrentStateeditor.clear()
//        CurrentStateeditor.commit()
//        System.out.println("스탑")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//
//        mSocket.off(Socket.EVENT_CONNECT, onConnect)
//        mSocket.off("verifyResponse", ontokenReceived)
//        mSocket.off("makeroom", makeroom)
//
//        mSocket.disconnect()
//        System.out.println("디스트로이")
//    }
//
//    override fun onPause() {
//        super.onPause()
//
//        var saveID: Int = menu_navigationView.selectedItemId
//        CurrentStateeditor.putInt("lastfragment", saveID)
//        CurrentStateeditor.commit()
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//        val currentID: Int = CurrentStatepref.getInt("lastfragment", R.id.gameItem)
//
//        var transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
//
//        when (currentID) {
//            R.id.gameItem -> transaction.replace(R.id.menu_frameLayout, gamereadyFragment).addToBackStack(null).commit()
//            R.id.forumsItem -> transaction.replace(R.id.menu_frameLayout, forumsFragment).addToBackStack(null).commit()
//            R.id.profileItem -> transaction.replace(
//                R.id.menu_frameLayout,
//                profileFragment
//            ).addToBackStack(null).commit()
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_menu)
//
//        System.out.println("메뉴 크리에이트")
//        try {
//                mSocket.on(Socket.EVENT_CONNECT, onConnect)
//                mSocket.on("verifyResponse", ontokenReceived)
//                mSocket.on("makeroom", makeroom)
//                if(!mSocket.connected())
//                {
//                    System.out.println("소켓 연결 안된 상태임 연결한다.")
//                    mSocket.connect()
//                }
//        } catch (e: URISyntaxException) {
//            e.printStackTrace()
//        }
//
//        pref = getSharedPreferences("chatgroundlogin", Context.MODE_PRIVATE)
//        editor = pref.edit()
//
//        CurrentStatepref = getSharedPreferences("chatgroundstate", Context.MODE_PRIVATE)
//        CurrentStateeditor = CurrentStatepref.edit()
//
//        var transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
//
//        transaction.replace(R.id.menu_frameLayout, gamereadyFragment).addToBackStack(null).commit()
//        menu_navigationView.setOnNavigationItemSelectedListener(ItemSelectedListener())
//    }
//
//    inner class ItemSelectedListener : BottomNavigationView.OnNavigationItemSelectedListener {
//        override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
//            var transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
//
//            when (menuItem.itemId) {
//                R.id.gameItem -> {
//                    transaction.replace(R.id.menu_frameLayout, gamereadyFragment).addToBackStack(null).commit()
//                    return true
//                }
//                R.id.forumsItem -> {
//                    transaction.replace(R.id.menu_frameLayout, forumsFragment).addToBackStack(null).commit()
//                    return true
//                }
//                R.id.profileItem -> {
//                    transaction.replace(R.id.menu_frameLayout, profileFragment).addToBackStack(null).commit()
//                    return true
//                }
//            }
//            return false
//        }
//    }
//
//    // Socket서버에 connect 된 후, 서버로부터 전달받은 'Socket.EVENT_CONNECT' Event 처리.
//    //jwt 토큰 보내기
//    private val onConnect:Emitter.Listener = Emitter.Listener {
//        // your code...
//        mSocket.emit("tokenverify", pref.getString("UserToken", "Logout"))
//    }
//
//    private val ontokenReceived:Emitter.Listener = Emitter.Listener { args ->
//        // 전달받은 데이터는 아래와 같이 추출할 수 있습니다.
//        val receivedData = args[0] as JSONObject
//        // your code...
//
//        System.out.println("토큰 리스폰 : $receivedData")
//
//        if (receivedData.get("token") == "Logout" || receivedData.get("token") == "Failed") {
//            editor.clear()
//            editor.commit()
//            CurrentStateeditor.clear()
//            CurrentStateeditor.commit()
//            mSocket.disconnect()
//            finish()
//            startActivity(Intent(this, MainActivity::class.java))
//        }
//    }
//
//    private val makeroom:Emitter.Listener = Emitter.Listener { args ->
//        // 전달받은 데이터는 아래와 같이 추출할 수 있습니다.
//        // your code...
//        val receivedData = args[0] as JSONObject
//
//        GlobalScope.launch(Dispatchers.IO) {
//            mSocket.emit("joinroom", receivedData.get("room"))
//        }
//
//        System.out.println("메뉴 메잌룸 채팅방 만들어라 신호")
//        startActivityForResult(Intent(this, chat::class.java),100)
//    }
//
//    override fun onBackPressed() {
//        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
//        builder.setMessage("종료하시겠습니까?")
//            .setPositiveButton("예") { _, which ->
//                mSocket.emit("findroomcancel","findroomcancel")
//
//                CurrentStateeditor.clear()
//                CurrentStateeditor.commit()
//                finish()
//                mSocket.disconnect()
//            }
//            .setNegativeButton("아니오") { _, which ->
//                return@setNegativeButton
//            }
//            .create()
//            .show()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        gamereadyFragment.onActivityResult(requestCode,resultCode,data)
//        profileFragment.onActivityResult(requestCode,resultCode,data)
//        forumsFragment.onActivityResult(requestCode,resultCode,data)
//    }
//}
class menu
