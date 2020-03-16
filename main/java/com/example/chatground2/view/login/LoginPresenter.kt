package com.example.chatground2.view.login

import android.content.Context
import android.content.SharedPreferences
import com.example.chatground2.model.Constants.SHARED_PREFERENCE
import com.example.chatground2.model.DAO.Model
import com.example.chatground2.model.DTO.UserDto
import com.google.gson.Gson


class LoginPresenter(
    private val context: Context,
    val view: LoginContract.ILoginView
) : LoginContract.ILoginPresenter, LoginContract.Listener {
    private var model: Model = Model(context)

    private val sp: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)
    private val spEdit: SharedPreferences.Editor = sp.edit()
    private val gson = Gson()

    override fun signUpButtonClick() {
        view.enterSignUpActivity()
    }

    override fun signInButtonClick() {
        val email = view.getEmailText()
        val password = view.getPasswordText()

        if (email.isEmpty()) {
            view.toastMessage("이메일을 입력해주세요")
            view.setEmailFocus()
            return
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.toastMessage("이메일 형식에 맞게 입력해주세요")
            view.setEmailText("")
            view.setEmailFocus()
            return
        }

        if (password.isEmpty()) {
            view.toastMessage("패스워드를 입력해주세요")
            view.setPasswordFocus()
            return
        }

        val hashMap = HashMap<String, Any>()
        hashMap["email"] = email
        hashMap["password"] = password

        model.signIn(hashMap, this)
    }

    override fun onLoginSuccess(userDto: UserDto) {//로그인 할 때 유저 저장
        spEdit.remove("User")//기존 유저 데이터 삭제
        spEdit.remove("message")//기존 유저 데이터 삭제
        val userJson = gson.toJson(userDto)
        println("json : $userJson")
        spEdit.putString("User", userJson)
        spEdit.commit()
        view.toastMessage("로그인 성공")
        view.finishActivity()
        view.enterMainActivity()
    }

    override fun onLoginFailure() {
        view.toastMessage("아이디 또는 패스워드가 일치하지 않습니다.")
        view.setEmailText("")
        view.setPasswordText("")
        view.setEmailFocus()
    }

    override fun onFailure() {
        view.toastMessage("로그인 실패")
    }
}