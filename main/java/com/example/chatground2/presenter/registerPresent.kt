//package com.example.chatground2.presenter
//
//import com.example.chatground2.contract.registerContract
//import com.example.chatground2.Model.registerModel
//
public class registerPresent()
//public class registerPresent : registerContract.Present {
//
//    private var view: registerContract.View
//    private var model: registerModel
//
//    constructor(view : registerContract.View)
//    {
//        this.view = view
//        this.model = registerModel(this)
//    }
//
//    override fun createUser(Email: String, Password: String, Nickname: String, PhoneNum: String) {
//        view.showLoading()
//        model.signup(Email,Password,Nickname,PhoneNum)
//    }
//
//    override fun successUser() {
//        view.hideLoading()
//        view.signuptoast()
//        view.nextpage()
//    }
//
//    override fun failureUser() {
//        view.hideLoading()
//        view.signupErrortoast()
//    }
//
//    override fun existsfailure() {
//        view.hideLoading()
//        view.existsError()
//    }
//}