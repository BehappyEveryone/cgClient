package com.example.chatground2.view.mainActivity

import android.content.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.chatground2.Api.SocketIo
import com.example.chatground2.model.Constants
import com.example.chatground2.model.DTO.UserDto
import com.example.chatground2.view.chatGround.ChatGroundActivity
import com.google.gson.Gson
import org.json.JSONObject

class MainPresenter(
    private val context: Context,
    val view: MainContract.IMainView
) : MainContract.IMainPresenter {
    private val sp: SharedPreferences =
        context.getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE)
    private val gson = Gson()

    private fun getUser(): UserDto {
        val json = sp.getString("User", "")
        return gson.fromJson(json, UserDto::class.java)
    }
}