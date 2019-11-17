package com.example.chatground2.contract

import com.android.volley.RequestQueue

interface registerContract {
    interface View{
        fun showLoading()
        fun hideLoading()
        fun signuptoast()
        fun signupErrortoast()
        fun nextpage()
        fun existsError()
    }

    interface Present{
        fun createUser(Email:String, Password:String,Nickname:String,PhoneNum:String){
            //logic for creating a new user.
        }
        fun successUser()
        fun failureUser()
        fun existsfailure()
    }
}