//package com.example.chatground2.view
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity;
//import android.view.View
//import android.widget.Toast
//import com.example.chatground2.contract.registerContract
//import com.example.chatground2.presenter.registerPresent
//import kotlinx.android.synthetic.main.activity_register.*
//import com.example.chatground2.R
//import com.example.chatground2.view.mainActivity.MainActivity
//
//
//public class register : AppCompatActivity(), registerContract.View {
//
//    private lateinit var presenter: registerContract.Present
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_register)
//
//        //Presenter 초기화
//        presenter = registerPresent(this)
//
//        registerOK.setOnClickListener {
//            var email = Emailinput.text.toString().trim()
//            var pw = passwordinput.text.toString().trim()
//            var nickname = nicknameinput.text.toString().trim()
//            var phoneNum = phoneinput.text.toString().trim()
//
//            presenter.createUser(email,pw,nickname,phoneNum)
//        }
//    }
//
//    override fun showLoading() {
//        registerprogressBar.visibility = View.VISIBLE
//    }
//
//    override fun hideLoading() {
//        registerprogressBar.visibility = View.GONE
//    }
//
//    override fun signuptoast() {
//        Toast.makeText(applicationContext,"회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show()
//    }
//
//    override fun signupErrortoast() {
//        Toast.makeText(applicationContext,"회원가입 실패\n다시 한번 시도해 주십시오.", Toast.LENGTH_LONG).show()
//    }
//
//    override fun nextpage() {
//        startActivity(Intent(this@register, MainActivity::class.java))
//    }
//
//    override fun existsError() {
//        Toast.makeText(applicationContext,"이메일 또는 닉네임이 이미 존재합니다.", Toast.LENGTH_LONG).show()
//    }
//}
public class register