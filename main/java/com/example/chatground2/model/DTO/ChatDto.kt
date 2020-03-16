package com.example.chatground2.model.DTO

import java.util.*

data class ChatDto(
    var type: String,
    var content: String,
    var image: String?,
    var date: Date,
    var user: ChatUserDto?
)