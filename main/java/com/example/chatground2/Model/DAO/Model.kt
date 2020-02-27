package com.example.chatground2.Model.DAO

import android.content.Context
import com.example.chatground2.Api.ServiceGenerator
import com.example.chatground2.Model.DTO.UserDto
import com.example.chatground2.Model.DefaultResponse
import com.example.chatground2.view.Login.LoginContract
import com.example.chatground2.view.SignUp.SignUpContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class Model(context: Context) {
    val serviceGenerator: ServiceGenerator = ServiceGenerator(context)

    fun signIn(
        hashMap: HashMap<String, Any>,
        listener: LoginContract.Listener
    ) {
        serviceGenerator.instance.signIn(hashMap).enqueue(object : Callback<UserDto> {
            override fun onFailure(call: Call<UserDto>, t: Throwable) {
                listener.onFailure()
            }

            override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                response.body()?.let { listener.onSuccess(it) }
            }
        })
    }

    fun signUp(
        hashMap: HashMap<String, Any>,
        listener: SignUpContract.Listener
    ) {
        serviceGenerator.instance.signUp(hashMap).enqueue(object : Callback<UserDto> {
            override fun onFailure(call: Call<UserDto>, t: Throwable) {
                listener.onFail(t.toString())
            }

            override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                try {
                    response.body()?.let { listener.onSignUpSuccess(it) }
                }catch (e:Exception)
                {
                    println("가입실패 : $e")
                    listener.onSignUpFailure()
                }
            }
        })
    }

    fun emailOverlap(
        hashMap: HashMap<String, Any>,
        listener: SignUpContract.Listener
    ) {
        serviceGenerator.instance.emailOverlap(hashMap).enqueue(object : Callback<DefaultResponse> {
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                listener.onFail(t.toString())
            }

            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if(response.body()?.type!!)
                {
                    listener.onEmailOverlapSuccess()
                }
                else
                {
                    listener.onEmailOverlapFailure()
                    println("이메일중복 : " + response.body()?.data)
                }
            }
        })
    }

    fun nicknameOverlap(
        hashMap: HashMap<String, Any>,
        listener: SignUpContract.Listener
    ) {
        serviceGenerator.instance.nicknameOverlap(hashMap).enqueue(object : Callback<DefaultResponse> {
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                listener.onFail(t.toString())
            }

            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if(response.body()?.type!!)
                {
                    listener.onNicknameOverlapSuccess()
                }
                else
                {
                    listener.onNicknameOverlapFailure()
                    println("닉네임중복 : " + response.body()?.data)
                }
            }
        })
    }
}