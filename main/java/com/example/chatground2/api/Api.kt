package com.example.chatground2.api

import com.example.chatground2.model.DefaultResponse
import com.example.chatground2.model.DefaultResponse2
import com.example.chatground2.model.loginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface Api {
    @FormUrlEncoded
    @POST("/signup")
    fun createuser(
        @Field("Email") email:String,
        @Field("Password") password:String,
        @Field("Nickname") nickname:String,
        @Field("PhoneNum") phoneNum:String
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("/login")
    fun loginuser(
        @Field("Email") email:String,
        @Field("Password") password:String
    ):Call<loginResponse>

    @Multipart
    @POST("/uploadprofile")
    fun uploadprofile(
        @Part("Email") Email:String,
        @Part file: MultipartBody.Part
    ):Call<DefaultResponse>

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

    @Multipart
    @POST("/modifyforum")
    fun modifyforum(
        @Part("idx") idx:Int,
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
        @Part("CommentUid") CommentUid:String,
        @Part("Email") Email:String,
        @Part("Nickname") Nickname:String,
        @Part("Content") Content:String,
        @Part("IsImage") IsImage:Boolean,
        @Part file: MultipartBody.Part?
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("/requestforums")
    fun requestforums(
        @Field("CurrentPage") CurrentPage:Int,
        @Field("Bestforums") Bestforums:Boolean
    ):Call<DefaultResponse2>
}