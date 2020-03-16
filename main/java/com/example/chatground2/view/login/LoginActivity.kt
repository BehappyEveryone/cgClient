package com.example.chatground2.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.chatground2.R
import com.example.chatground2.view.mainActivity.MainActivity
import com.example.chatground2.view.signUp.SignUpActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginContract.ILoginView, View.OnClickListener {

    private var presenter: LoginPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initialize()
    }

    private fun initialize()
    {
        presenter = LoginPresenter(this,this)

        L_signIn.setOnClickListener(this)
        L_signUp.setOnClickListener(this)
    }

    override fun progressVisible(boolean: Boolean) {
        if (boolean) {
            L_progressbar.visibility = View.VISIBLE
        } else {
            L_progressbar.visibility = View.INVISIBLE
        }
    }

    override fun finishActivity() = finish()

    override fun toastMessage(text: String) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()

    override fun getEmailText(): String = L_email.text.toString()

    override fun getPasswordText(): String = L_password.text.toString()

    override fun enterMainActivity() = startActivity(Intent(this, MainActivity::class.java))

    override fun enterSignUpActivity() = startActivity(Intent(this, SignUpActivity::class.java))

    override fun setEmailText(text: String) = L_email.setText(text)

    override fun setPasswordText(text: String) = L_password.setText(text)

    override fun setEmailFocus(){
        L_email.requestFocus()
    }

    override fun setPasswordFocus() {
        L_password.requestFocus()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.L_signIn -> presenter?.signInButtonClick()
            R.id.L_signUp -> presenter?.signUpButtonClick()
        }
    }
}
