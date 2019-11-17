package com.example.chatground2.api

import com.example.chatground2.model.DefaultResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface registerApi {
    @FormUrlEncoded
    @POST("/signup")
    fun createuser(
        @Field("Email") email:String,
        @Field("Password") password:String,
        @Field("Nickname") nickname:String,
        @Field("PhoneNum") phoneNum:String
    ):Call<DefaultResponse>
}