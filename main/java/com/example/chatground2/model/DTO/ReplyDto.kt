package com.example.chatground2.model.DTO

data class ReplyDto(
    var id: String,
    var userId:String,
    var forumId:String,
    var commentId:String,
    var content:String,
    var imageUrl:String?
)