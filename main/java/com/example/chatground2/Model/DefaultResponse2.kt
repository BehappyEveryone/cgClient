package com.example.chatground2.Model

import com.google.gson.annotations.SerializedName

data class DefaultResponse2(
    @SerializedName("type")
    val type:Boolean,
    @SerializedName("data")
    val data: Array<forumitemdata>?
)