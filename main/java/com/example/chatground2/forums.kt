package com.example.chatground2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.chatground2.api.socket
import com.example.chatground2.model.chatitemdata
import com.example.chatground2.model.forumitemdata
import com.example.chatground2.view.MainActivity
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_forums.*
import kotlinx.android.synthetic.main.fragment_forums.view.*
import kotlinx.android.synthetic.main.fragment_gameready.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.util.ArrayList

class forums : Fragment() {
    private lateinit var mSocket: Socket
    private lateinit var pref: SharedPreferences
    private var items: ArrayList<forumitemdata> = ArrayList()
    private lateinit var mAdapter:forumAdapter
    val WRITEFORUMCODE = 500

    override fun onDestroyView() {
        super.onDestroyView()

        items.clear()
        mSocket.off("ForumResult", ForumResult)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_forums, container, false)

        mAdapter = forumAdapter(this.context!!, items)

        val lm = LinearLayoutManager(this.context)
        view.F_forumRecy.adapter = mAdapter
        view.F_forumRecy.layoutManager = lm

        mSocket = socket.mSocket
        mSocket.on("ForumResult", ForumResult)
        mSocket.emit("ForumRequest", 1)
        pref = activity!!.getSharedPreferences("chatgroundlogin", Context.MODE_PRIVATE)

        view.F_writebutton.setOnClickListener {
            activity!!.startActivityForResult(Intent(context, writeforum::class.java),WRITEFORUMCODE)
        }

        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == WRITEFORUMCODE) {
            if (resultCode == Activity.RESULT_OK) {
                System.out.println("WRITEFORUMCODE성공")
                items.clear()
                mSocket.emit("ForumRequest", 1)
            }
            else {
                System.out.println("WRITEFORUMCODE실패")
                Toast.makeText(context, "취소", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            System.out.println("requestcode : $requestCode")
        }
    }

    private val ForumResult = Emitter.Listener { args ->
        val receivedData = args[0] as JSONObject
        // your code...

        if (receivedData.getBoolean("result")) {
            for (i: Int in 0 until receivedData.getJSONArray("forum").length()) {
                var F_idx: Int = receivedData.getJSONArray("forum").getJSONObject(i).getInt("idx")
                var F_Email: String = receivedData.getJSONArray("forum").getJSONObject(i).getString("Email")
                var F_Nickname: String = receivedData.getJSONArray("forum").getJSONObject(i).getString("Nickname")
                var F_CommentNum: Int =
                    receivedData.getJSONArray("forum").getJSONObject(i).getJSONArray("Comments").length()
                var F_Title: String = receivedData.getJSONArray("forum").getJSONObject(i).getString("Title")
                var F_Subject: String = receivedData.getJSONArray("forum").getJSONObject(i).getString("Subject")
                var F_Recommend: Int = receivedData.getJSONArray("forum").getJSONObject(i).getInt("Recommend")
                var F_ImageNum: Int = receivedData.getJSONArray("forum").getJSONObject(i).getInt("ImageNum")
                var F_Date: String = receivedData.getJSONArray("forum").getJSONObject(i).getString("Date")

                items.add(forumitemdata(F_idx,F_Email, F_Subject, F_Title, F_CommentNum, F_Nickname, F_Recommend, F_Date, F_ImageNum))
            }
            CoroutineScope(Dispatchers.Main).launch {
                mAdapter.notifyDataSetChanged()
            }
        }
    }
}