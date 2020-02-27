package com.example.chatground2.Model

import com.example.chatground2.contract.registerContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class registerModel {
//    private var present: registerContract.Present
//
//    constructor(present : registerContract.Present)
//    {
//        this.present = present
//    }
//
//    fun signup(Email: String, Password: String, Nickname: String, PhoneNum: String) {
//        ApiClient.instance.createuser(Email,Password,Nickname,PhoneNum)
//            .enqueue(object: Callback<DefaultResponse> {
//                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
//                    if(response.body()!!.type)
//                    {
//                        present.successUser()
//                    }
//                    else
//                    {
//                        System.out.println("응답 데이터 : " + response.body()!!.data.toString())
//                        if(response.body()!!.data.toString().equals("Email or Nickname already exists"))
//                        {
//                            present.existsfailure()
//                        }
//                        else
//                        {
//                            present.failureUser()
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
//                    present.failureUser()
//                }
//            })
//    }
}