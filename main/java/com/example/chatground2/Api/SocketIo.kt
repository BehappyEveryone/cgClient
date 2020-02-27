package com.example.chatground2.Api

import io.socket.client.IO
import io.socket.client.Socket

object SocketIo {
    private val opts = IO.Options()
    val mSocket: Socket = IO.socket(IpAddress.BaseURL + IpAddress.Router.socket, opts)
}