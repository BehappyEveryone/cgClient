package com.example.chatground2.view.chatGround

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatground2.R
import com.example.chatground2.adapter.ChatAdapter
import com.example.chatground2.adapter.ChatUserAdapter
import kotlinx.android.synthetic.main.activity_chat_ground.*
import kotlinx.android.synthetic.main.menu_chat_drawer.*

class ChatGroundActivity : AppCompatActivity(), ChatGroundContract.IChatGroundView,
    View.OnClickListener {

    private var presenter: ChatGroundPresenter? = null
    private var chatAdapter: ChatAdapter? = null
    private var chatUserAdapter: ChatUserAdapter? = null
    private var chatLm: LinearLayoutManager? = null
    private var userLm: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_ground)

        initialize()
        presenter?.getIntent(intent)
        presenter?.bindService()
    }

    private fun initialize() {
        chatAdapter = ChatAdapter(this)
        chatUserAdapter = ChatUserAdapter(this)
        chatLm = LinearLayoutManager(this)
        userLm = LinearLayoutManager(this)

        presenter = ChatGroundPresenter(this, this).apply {
            adapterChatModel = chatAdapter
            adapterChatView = chatAdapter
            adapterChatUserModel = chatUserAdapter
            adapterChatUserView = chatUserAdapter
        }

        CG_chatRecycle.run {
            layoutManager = chatLm
            adapter = chatAdapter
            setHasFixedSize(true)
        }

        CD_menuRecycle.run {
            layoutManager = userLm
            adapter = chatUserAdapter
        }

        CG_drawerButton.setOnClickListener(this)
        CG_agree.setOnClickListener(this)
        CG_oppose.setOnClickListener(this)
        CG_sendButton.setOnClickListener(this)
        CG_plus.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.CG_drawerButton -> presenter?.drawerClick()
            R.id.CG_sendButton -> presenter?.sendMessage(CG_message.text.toString())
            R.id.CG_agree -> presenter?.setOpinion(true)
            R.id.CG_oppose -> presenter?.setOpinion(false)
        }
    }

    override fun toastMessage(text: String) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()

    override fun setMessageClear() {
        CG_message.setText("")
    }

    override fun onResume() {
        super.onResume()

        presenter?.getMessages()
        presenter?.setBroadCastReceiver()
    }

    override fun onPause() {
        super.onPause()

        presenter?.removeBroadCastReceiver()
        presenter?.removeMessages()
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter?.unbindService()
    }

    override fun openDrawer() {
        CG_drawer.openDrawer(GravityCompat.END)
    }

    override fun setChatScrollPosition(position: Int) {
        CG_chatRecycle.scrollToPosition(position)
    }

    override fun setEnable(boolean: Boolean) {
        CG_message.isEnabled = boolean
        CG_plus.isEnabled = boolean
        CG_sendButton.isEnabled = boolean
        CD_exit.isEnabled = boolean
        CD_exitText.isEnabled = boolean
    }

    override fun setSubjectText(text: String) {
        CG_subject.text = text
    }

    override fun setAgreeButtonPressed(boolean: Boolean) {
        CG_agree.isPressed = boolean
    }

    override fun getAgreeButtonPressed():Boolean = CG_agree.isPressed

    override fun setOpposeButtonPressed(boolean: Boolean) {
        CG_oppose.isPressed = boolean
    }
    override fun getOpposeButtonPressed():Boolean = CG_agree.isPressed

    override fun setTimeText(text: String) {
        CG_time.text = text
    }

    override fun setOpinionVisible(boolean: Boolean) {
        if (boolean) {
            CG_opinion.visibility = View.VISIBLE
        } else {
            CG_opinion.visibility = View.GONE
        }
    }
}