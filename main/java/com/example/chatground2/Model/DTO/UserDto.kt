package com.example.chatground2.Model.DTO

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

data class UserDto(
    var _id: String,
    var email: String,
    var password: String?,
    var nickname: String,
    var birth: Date,
    var profile: String?,
    var introduce: String?,
    var forums: ArrayList<Any>?,
    var comments: ArrayList<Any>?
)