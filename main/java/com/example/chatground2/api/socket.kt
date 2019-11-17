package com.example.chatground2.api

import com.example.chatground2.ipadress
import io.socket.client.IO
import io.socket.client.Socket

object socket {
    val opts = IO.Options()
    val mSocket: Socket = IO.socket(ipadress.ipadress, opts)
}