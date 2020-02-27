package com.example.chatground2.Model

import com.google.gson.annotations.SerializedName

data class forumitemdata(
    @SerializedName("idx")
    val idx: Int,
    @SerializedName("Email")
    val Email: String,
    @SerializedName("Subject")
    val subject: String,
    @SerializedName("Title")
    val title: String,
    @SerializedName("Content")
    val content: String,
    @SerializedName("CommentNum")
    val commentNum: Int,
    @SerializedName("Nickname")
    val nickname: String,
    @SerializedName("RecommendNum")
    val recommend: Int,
    @SerializedName("Date")
    val date: String,
    @SerializedName("ImageNum")
    val ImageNum: Int
)