package com.example.chatground2.model

import android.os.AsyncTask
import android.os.Message
import org.json.JSONObject
import java.io.*

data class loginResponse(val type: Boolean, val data:String, val token:String, val userEmail:String,val userNickname:String)