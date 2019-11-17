package com.example.chatground2.api

import com.example.chatground2.model.DefaultResponse
import com.example.chatground2.model.loginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface imageApi {
    @Multipart
    @POST("/uploadprofile")
    fun uploadprofile(
        @Part("Email") Email:String,
        @Part file: MultipartBody.Part
    ):Call<DefaultResponse>
}