package com.example.chatground2.model.DTO

import java.util.*
import kotlin.collections.ArrayList

data class ChatUserDto(
    var _id: String,
    var nickname: String,
    var profile: String?,
    var introduce: String?,
    var opinion:Boolean?
)