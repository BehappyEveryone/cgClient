package com.example.chatground2.Model.DTO

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

data class ForumDto(
    var _id:String,
    var idx: Int,
    var user:UserDto,
    var subject:String,
    var title:String,
    var content:String,
    var imageUrl:ArrayList<String>?,
    var comments:ArrayList<Any>?,
    var recommend:Int,
    var birth:Date
)