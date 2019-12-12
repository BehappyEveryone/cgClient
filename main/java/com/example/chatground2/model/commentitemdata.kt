package com.example.chatground2.model

data class commentitemdata(
    val type: String,
    var recommentstate: Boolean,
    val uid: String,
    val Email: String,
    val nickname: String,
    val content: String,
    val date: String,
    val IsImage: Boolean,
    val Imagepath: String?
)