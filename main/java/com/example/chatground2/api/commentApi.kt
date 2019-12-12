package com.example.chatground2.api

import com.example.chatground2.model.DefaultResponse
import com.example.chatground2.model.loginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface commentApi {
    @Multipart
    @POST("/writecomment")
    fun uploadcomment(
        @Part("ForumIdx") ForumIdx:Int,
        @Part("Email") Email:String,
        @Part("Nickname") Nickname:String,
        @Part("Content") Content:String,
        @Part("IsImage") IsImage:Boolean,
        @Part file: MultipartBody.Part?
    ):Call<DefaultResponse>

    @Multipart
    @POST("/writerecomment")
    fun uploadrecomment(
        @Part("ForumIdx") ForumIdx:Int,
        @Part("" +
                "") CommentUid:String,
        @Part("Email") Email:String,
        @Part("Nickname") Nickname:String,
        @Part("Content") Content:String,
        @Part("IsImage") IsImage:Boolean,
        @Part file: MultipartBody.Part?
    ):Call<DefaultResponse>
}