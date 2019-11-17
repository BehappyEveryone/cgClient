package com.example.chatground2.view

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.chatground2.R
import com.example.chatground2.contract.loginContract
import com.example.chatground2.contract.registerContract
import com.example.chatground2.presenter.loginPresent
import com.example.chatground2.presenter.registerPresent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import android.content.SharedPreferences
import com.example.chatground2.menu

class MainActivity : AppCompatActivity(), loginContract.View {

    private lateinit var presenter: loginContract.Present
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pref = this.getSharedPreferences("chatgroundlogin", Context.MODE_PRIVATE)

        //Presenter 초기화
        presenter = loginPresent(this,pref)

        presenter.autoLogin()

        loginbutton.setOnClickListener {
            var email = L_Emailinput.text.toString().trim()
            var pw = L_Passwordinput.text.toString().trim()

            presenter.loginUser(email,pw)
        }

        registerbutton.setOnClickListener {
            signuppage()
        }
    }

    override fun autocheck():Boolean
    {
        return autologinbox.isChecked
    }

    override fun showLoading() {
        loginprogressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loginprogressBar.visibility = View.GONE
    }

    override fun logintoast() {
        Toast.makeText(applicationContext,"로그인 성공", Toast.LENGTH_LONG).show()
    }

    override fun loginErrortoast() {
        Toast.makeText(applicationContext,"로그인 실패\n다시 한번 시도해 주십시오.", Toast.LENGTH_LONG).show()
    }

    override fun loginnextpage() {
        finish()
        startActivity(Intent(this@MainActivity, menu::class.java))
    }

    override fun signuppage() {
        startActivity(Intent(this@MainActivity, register::class.java))
    }

    override fun incorrectError() {
        Toast.makeText(applicationContext,"아이디 또는 비밀번호가 틀렸습니다", Toast.LENGTH_LONG).show()
    }
}
