package com.example.chatground2.view.Login

import android.content.Context
import com.example.chatground2.Model.DAO.Model
import com.example.chatground2.Model.DTO.UserDto

class LoginPresenter(
    private val context: Context,
    val view: LoginContract.ILoginView
) : LoginContract.ILoginPresenter, LoginContract.Listener {
    private var model:Model = Model(context)

    override fun signUpButtonClick() {
        val email = view.getEmailText()
        val password = view.getPasswordText()

        val hashMap = HashMap<String,Any>()
        hashMap["email"] = email
        hashMap["password"] = password

        model.signIn(hashMap,this)
    }

    override fun signInButtonClick() {
        view.enterSignUpActivity()
    }

    override fun onSuccess(userDto: UserDto) {
        view.toastMessage("로그인 성공")
        view.finishActivity()
        view.enterMainActivity()
    }

    override fun onFailure() {
        view.toastMessage("로그인 실패")
    }
}