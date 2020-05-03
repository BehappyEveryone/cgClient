package com.example.chatground2.api

import com.example.chatground2.model.dto.ForumDto
import com.example.chatground2.model.dto.UserDto
import com.example.chatground2.model.dto.DefaultResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @Multipart
    @POST(IpAddress.Router.modifyProfile)
    fun modifyProfile(
        @PartMap hashMap: HashMap<String, RequestBody>,
        @Part imagePart: MultipartBody.Part?
    ):Call<UserDto?>

    @Multipart
    @POST(IpAddress.Router.modifyForum)
    fun modifyForum(
        @PartMap hashMap: HashMap<String, RequestBody>,
        @Part imagePart: Array<MultipartBody.Part?>
    ):Call<DefaultResponse>

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
}