package com.example.chatground2.Model.DTO

import java.util.*
import kotlin.collections.ArrayList

data class CommentDto(
    var id: String,
    var userId:Any,
    var forumIdx:Int,
    var content:String,
    var imageUrl:String?,
    var reply:ArrayList<Any>?,
    var date: Date?
)