package com.example.chatground2.Model

data class commentitemdata(
    val type: String,
    var recommentstate: Boolean,
    val uid: String,
    val recommentuid:String?,
    val Email: String,
    val nickname: String,
    val content: String,
    val date: String,
    val IsImage: Boolean,
    val Imagepath: String?
)