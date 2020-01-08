package com.example.chatground2.model

import android.os.AsyncTask
import android.os.Message
import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject
import java.io.*

data class DefaultResponse2(
    @SerializedName("type")
    val type:Boolean,
    @SerializedName("data")
    val data: Array<forumitemdata>?
)