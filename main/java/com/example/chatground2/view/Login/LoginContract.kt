package com.example.chatground2.view.Login

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
    }

    interface Listener
    {
        fun onSuccess(userDto: UserDto)
        fun onFailure()
    }
}