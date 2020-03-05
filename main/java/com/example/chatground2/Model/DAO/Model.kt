package com.example.chatground2.Model.DAO

import android.content.Context
import com.example.chatground2.Api.ServiceGenerator
import com.example.chatground2.Model.DTO.ForumDto
import com.example.chatground2.Model.DTO.UserDto
import com.example.chatground2.Model.DefaultResponse
import com.example.chatground2.view.login.LoginContract
import com.example.chatground2.view.SignUp.SignUpContract
import com.example.chatground2.view.detailForum.DetailForumContract
import com.example.chatground2.view.forums.ForumsContract
import com.example.chatground2.view.writeForum.WriteForumContract
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class Model(context: Context) {
    private val serviceGenerator: ServiceGenerator = ServiceGenerator(context)

    fun writeComment(
        hashMap: HashMap<String, RequestBody>,
        imagePart: MultipartBody.Part?,
        listener: DetailForumContract.Listener
    ) {
        serviceGenerator.instance.writeComment(hashMap, imagePart).enqueue(object :
            Callback<DefaultResponse> {
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                println("writeComment 통신 에러 : $t")
                listener.onFailure()
            }

            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.code() == 500) {
                    println("writeComment 에러" + response.body()?.data)
                    listener.onWriteCommentFailure()
                } else {
                    listener.onWriteCommentSuccess()
                }
            }
        })
    }

    fun deleteForum(
        hashMap: HashMap<String, Any>,
        listener: DetailForumContract.Listener
    ) {
        serviceGenerator.instance.deleteForum(hashMap)
            .enqueue(object : Callback<DefaultResponse?> {
                override fun onFailure(call: Call<DefaultResponse?>, t: Throwable) {
                    listener.onFailure()
                }

                override fun onResponse(
                    call: Call<DefaultResponse?>,
                    response: Response<DefaultResponse?>
                ) {
                    if (response.code() == 401 || response.code() == 500) {
                        println(response.body())
                        listener.onDeleteForumFailure()
                    } else {
                        listener.onDeleteForumSuccess()
                    }
                }
            })
    }

    fun recommendForum(
        hashMap: HashMap<String, Any>,
        listener: DetailForumContract.Listener
    ) {
        serviceGenerator.instance.recommendForum(hashMap)
            .enqueue(object : Callback<DefaultResponse?> {
                override fun onFailure(call: Call<DefaultResponse?>, t: Throwable) {
                    listener.onFailure()
                }

                override fun onResponse(
                    call: Call<DefaultResponse?>,
                    response: Response<DefaultResponse?>
                ) {
                    if (response.code() == 401 || response.code() == 500) {
                        println(response.body())
                        listener.onRecommendForumFailure()
                    } else {
                        listener.onRecommendForumSuccess()
                    }
                }
            })
    }

    fun detailForum(
        hashMap: HashMap<String, Any>,
        listener: DetailForumContract.Listener
    ) {
        serviceGenerator.instance.detailForum(hashMap)
            .enqueue(object : Callback<ForumDto?> {
                override fun onFailure(call: Call<ForumDto?>, t: Throwable) {
                    listener.onFailure()
                }

                override fun onResponse(
                    call: Call<ForumDto?>,
                    response: Response<ForumDto?>
                ) {
                    if (response.code() == 401 || response.code() == 500) {
                        println(response.body())
                        listener.onDetailForumFailure()
                    } else {
                        listener.onDetailForumSuccess(response.body())
                    }
                }
            })
    }

    fun writeForum(
        hashMap: HashMap<String, RequestBody>,
        imagePart: Array<MultipartBody.Part?>,
        listener: WriteForumContract.Listener
    ) {
        serviceGenerator.instance.writeForum(hashMap, imagePart).enqueue(object :
            Callback<DefaultResponse> {
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                println("writeForum 통신 에러 : $t")
                listener.onFailure()
            }

            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.code() == 500) {
                    println("writeForum 에러" + response.body()?.data)
                    listener.onFailure()
                } else {
                    listener.onSuccess()
                }
            }
        })
    }

    fun callForums(
        hashMap: HashMap<String, Any>,
        listener: ForumsContract.Listener
    ) {
        serviceGenerator.instance.callForums(hashMap)
            .enqueue(object : Callback<ArrayList<ForumDto>?> {
                override fun onFailure(call: Call<ArrayList<ForumDto>?>, t: Throwable) {
                    println("callForums 통신 에러 : $t")
                    listener.onFailure()
                }

                override fun onResponse(
                    call: Call<ArrayList<ForumDto>?>,
                    response: Response<ArrayList<ForumDto>?>
                ) {
                    if (response.code() == 401 || response.code() == 500) {
                        println("callForums 에러")
                        listener.onCallForumsFailure()
                    } else {
                        listener.onCallForumsSuccess(response.body())
                    }
                }
            })
    }

    fun signIn(
        hashMap: HashMap<String, Any>,
        listener: LoginContract.Listener
    ) {
        serviceGenerator.instance.signIn(hashMap).enqueue(object : Callback<UserDto> {
            override fun onFailure(call: Call<UserDto>, t: Throwable) {
                println("signIn 에러 : $t")
                listener.onFailure()
            }

            override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                if (response.code() == 401) {
                    listener.onLoginFailure()
                } else {
                    response.body()?.let { listener.onLoginSuccess(it) }
                }
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
                } catch (e: Exception) {
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

            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.body()?.type!!) {
                    listener.onEmailOverlapSuccess()
                } else {
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
        serviceGenerator.instance.nicknameOverlap(hashMap)
            .enqueue(object : Callback<DefaultResponse> {
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    listener.onFail(t.toString())
                }

                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if (response.body()?.type!!) {
                        listener.onNicknameOverlapSuccess()
                    } else {
                        listener.onNicknameOverlapFailure()
                        println("닉네임중복 : " + response.body()?.data)
                    }
                }
            })
    }
}