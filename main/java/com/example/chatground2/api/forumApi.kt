package com.example.chatground2.api

import com.example.chatground2.model.DefaultResponse
import com.example.chatground2.model.loginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface forumApi {
    @Multipart
    @POST("/writeforum")
    fun uploadforum(
        @Part("Email") Email:String,
        @Part("Nickname") Nickname:String,
        @Part("Title") Title:String,
        @Part("Subject") Subject:String,
        @Part("Content") Content:String,
        @Part("ImageNum") ImageNum:Int,
        @Part file1: MultipartBody.Part?,
        @Part file2: MultipartBody.Part?,
        @Part file3: MultipartBody.Part?,
        @Part file4: MultipartBody.Part?,
        @Part file5: MultipartBody.Part?
    ):Call<DefaultResponse>
}