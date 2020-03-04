package com.example.chatground2.view.login

import com.example.chatground2.Model.DTO.UserDto

interface LoginContract {
    interface ILoginPresenter{
        fun signUpButtonClick()
        fun signInButtonClick()
    }

    interface ILoginView{
        fun finishActivity()
        fun progressVisible(boolean: Boolean)
        fun toastMessage(text:String)
        fun getEmailText():String
        fun getPasswordText():String
        fun enterMainActivity()
        fun enterSignUpActivity()
        fun setEmailText(text: String)
        fun setPasswordText(text: String)
        fun setEmailFocus()
        fun setPasswordFocus()
    }

    interface Listener
    {
        fun onLoginSuccess(userDto: UserDto)
        fun onLoginFailure()
        fun onFailure()
    }
}