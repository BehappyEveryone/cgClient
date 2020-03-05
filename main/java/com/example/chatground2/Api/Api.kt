package com.example.chatground2.Api

import com.example.chatground2.Model.DTO.ForumDto
import com.example.chatground2.Model.DTO.UserDto
import com.example.chatground2.Model.DefaultResponse
import com.example.chatground2.Model.DefaultResponse2
import com.example.chatground2.Model.loginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST(IpAddress.Router.recommendForum)
    fun recommendForum(
        @FieldMap hashMap: HashMap<String,Any>
    ): Call<DefaultResponse?>

    @FormUrlEncoded
    @POST(IpAddress.Router.deleteForum)
    fun deleteForum(
        @FieldMap hashMap: HashMap<String,Any>
    ): Call<DefaultResponse?>

    @Multipart
    @POST(IpAddress.Router.writeComment)
    fun writeComment(
        @PartMap hashMap: HashMap<String, RequestBody>,
        @Part imagePart: MultipartBody.Part?
    ):Call<DefaultResponse>

    @Multipart
    @POST(IpAddress.Router.writeForum)
    fun writeForum(
        @PartMap hashMap: HashMap<String, RequestBody>,
        @Part imagePart: Array<MultipartBody.Part?>
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST(IpAddress.Router.callForums)
    fun callForums(
        @FieldMap hashMap: HashMap<String,Any>
    ): Call<ArrayList<ForumDto>?>

    @FormUrlEncoded
    @POST(IpAddress.Router.detailForum)
    fun detailForum(
        @FieldMap hashMap: HashMap<String,Any>
    ): Call<ForumDto?>

    @FormUrlEncoded
    @POST(IpAddress.Router.signIn)
    fun signIn(
        @FieldMap hashMap: HashMap<String,Any>
    ): Call<UserDto>

    @FormUrlEncoded
    @POST(IpAddress.Router.signUp)
    fun signUp(
        @FieldMap hashMap: HashMap<String,Any>
    ): Call<UserDto>

    @FormUrlEncoded
    @POST(IpAddress.Router.emailOverlap)
    fun emailOverlap(
        @FieldMap hashMap: HashMap<String,Any>
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST(IpAddress.Router.nicknameOverlap)
    fun nicknameOverlap(
        @FieldMap hashMap: HashMap<String,Any>
    ): Call<DefaultResponse>

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