//package com.example.chatground2.presenter
//
//import android.content.SharedPreferences
//import com.example.chatground2.contract.loginContract
//import com.example.chatground2.Model.loginModel
//
public class loginPresent()
//public class loginPresent : loginContract.Present {
//
//    private var view: loginContract.View
//    private var model: loginModel
//    private var prefe: SharedPreferences
//    private var editor: SharedPreferences.Editor
//
//    constructor(view : loginContract.View, preferences : SharedPreferences)
//    {
//        this.view = view
//        this.model = loginModel(this)
//        this.prefe = preferences
//        this.editor = preferences.edit()
//    }
//
//    override fun shareduserdata(Token: String,Email: String,Nickname: String) {
//        editor.clear()
//        editor.putString("UserToken",Token)
//        editor.putString("UserEmail",Email)
//        editor.putString("UserNickname",Nickname)
//        if(view.autocheck())
//        {
//            editor.putBoolean("autoLogin",true)
//        }
//        else
//        {
//            editor.putBoolean("autoLogin",false)
//        }
//        editor.commit()
//    }
//
//    override fun autoLogin()
//    {
//        if(prefe.getBoolean("autoLogin",false))
//        {
//            view.showLoading()
//            if(prefe.getString("UserToken","Logout") != "Logout")
//            {
//                successUser()
//            }
//            else
//            {
//                failureUser()
//            }
//        }
//    }
//
//    override fun loginUser(Email: String, Password: String) {
//        view.showLoading()
//        model.login(Email,Password)
//    }
//
//    override fun successUser() {
//        view.hideLoading()
//        view.logintoast()
//        view.loginnextpage()
//    }
//
//    override fun failureUser() {
//        view.hideLoading()
//        view.loginErrortoast()
//    }
//
//    override fun incorrectfailure() {
//        view.hideLoading()
//        view.incorrectError()
//    }
//}