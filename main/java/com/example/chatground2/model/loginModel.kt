package com.example.chatground2.model

import com.example.chatground2.api.loginClient
import com.example.chatground2.api.registerClient
import com.example.chatground2.contract.loginContract
import com.example.chatground2.contract.registerContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class loginModel {
    private var present: loginContract.Present

    constructor(present : loginContract.Present)
    {
        this.present = present
    }

    fun login(Email: String, Password: String) {
        loginClient.instance.loginuser(Email,Password)
            .enqueue(object: Callback<loginResponse> {
                override fun onResponse(call: Call<loginResponse>, response: Response<loginResponse>) {
                    System.out.println("로그인 응답 : " + response.body())
                    if(response.body()!!.type)
                    {
                        var token = response.body()!!.token.trim()
                        var email = response.body()!!.userEmail.trim()
                        var nickanme = response.body()!!.userNickname.trim()

                        present.shareduserdata(token,email,nickanme)

                        present.successUser()
                    }
                    else
                    {
                        if(response.body()!!.data.toString().equals("Incorrect email/password"))
                        {
                            present.incorrectfailure()
                        }
                        else
                        {
                            present.failureUser()
                            System.out.println("에러 : " + response.body()!!.data)
                        }
                    }
                }

                override fun onFailure(call: Call<loginResponse>, t: Throwable) {
                    present.failureUser()
                    System.out.println("에러 : $t")
                }
            })
    }
}