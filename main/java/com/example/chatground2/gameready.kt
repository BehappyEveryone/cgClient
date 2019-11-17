package com.example.chatground2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatground2.api.socket
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.fragment_gameready.*
import kotlinx.android.synthetic.main.fragment_gameready.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.Toast
import com.example.chatground2.view.MainActivity
import android.R.attr.data
import android.app.Activity.RESULT_OK


class gameready : Fragment() {

    private var state:Boolean? = null

    private lateinit var mSocket: Socket
    private lateinit var pref: SharedPreferences

    private lateinit var CurrentStatepref: SharedPreferences
    private lateinit var CurrentStateeditor: SharedPreferences.Editor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mSocket= socket.mSocket

        mSocket.on("makeroom", makeroom)

        pref = activity!!.getSharedPreferences("chatgroundlogin", Context.MODE_PRIVATE)
        CurrentStatepref = context!!.getSharedPreferences("chatgroundstate", Context.MODE_PRIVATE)
        CurrentStateeditor = CurrentStatepref.edit()

        state = CurrentStatepref.getBoolean(pref.getString("UserEmail","Logout") + "gameready",false)

        var view:View = inflater.inflate(R.layout.fragment_gameready, container, false)

        if(state == true)
        {
            view.FG_startbutton.background = ContextCompat.getDrawable(activity!!, R.drawable.button4)
            view.FG_startbutton.text = "게임 검색 중"
        }
        else
        {
            view.FG_startbutton.background = ContextCompat.getDrawable(activity!!, R.drawable.button3)
            view.FG_startbutton.text = "게임 시작"
        }

        view.FG_startbutton.setOnClickListener {
            //버튼을 눌렀을 떄 상태가 false면
            if(state == false)
            {
                state = true
                CurrentStateeditor.putBoolean(pref.getString("UserEmail","Logout") + "gameready",true).commit()
                mSocket.emit("findroom")

                FG_startbutton.background = ContextCompat.getDrawable(activity!!, R.drawable.button4)
                FG_startbutton.text = "게임 검색 중"
            }
            else
            {
                state = false
                CurrentStateeditor.putBoolean(pref.getString("UserEmail","Logout") + "gameready",false).commit()
                System.out.println("대기취소버튼")
                mSocket.emit("findroomcancel","findroomcancel")

                FG_startbutton.background = ContextCompat.getDrawable(activity!!, R.drawable.button3)
                FG_startbutton.text = "게임 시작"
            }
        }

        return view
    }

    private val makeroom = Emitter.Listener { args ->
        // 전달받은 데이터는 아래와 같이 추출할 수 있습니다.
        // your code...
        state = false
        CurrentStateeditor.putBoolean(pref.getString("UserEmail","Logout") + "gameready",false).commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                FG_startbutton.background = ContextCompat.getDrawable(activity!!, R.drawable.button3)
                FG_startbutton.text = "게임 시작"
            }
        }
        else
        {
            System.out.println("안돌아옴")
        }
    }

    override fun onDetach() {
        super.onDetach()

        CurrentStateeditor.putBoolean(pref.getString("UserEmail","Logout") + "gameready",false).commit()
        mSocket.emit("findroomcancel","findroomcancel")
    }
}