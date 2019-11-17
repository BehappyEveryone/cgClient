package com.example.chatground2.api

import com.example.chatground2.model.DefaultResponse
import com.example.chatground2.model.loginResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface loginApi {
    @FormUrlEncoded
    @POST("/login")
    fun loginuser(
        @Field("Email") email:String,
        @Field("Password") password:String
    ):Call<loginResponse>
}