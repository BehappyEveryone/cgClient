package com.example.chatground2.contract

import com.android.volley.RequestQueue

interface loginContract {
    interface View{
        fun showLoading()
        fun hideLoading()
        fun logintoast()
        fun loginErrortoast()
        fun incorrectError()
        fun loginnextpage()
        fun signuppage()
        fun autocheck():Boolean
    }

    interface Present{
        fun autoLogin()
        fun loginUser(Email:String, Password:String)
        fun successUser()
        fun failureUser()
        fun incorrectfailure()
        fun shareduserdata(Token: String,Email: String,Nickname: String)
    }
}