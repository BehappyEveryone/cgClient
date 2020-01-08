
package com.example.chatground2.api

import com.example.chatground2.ipadress
import com.example.chatground2.model.DefaultResponse
import com.example.chatground2.model.DefaultResponse2
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.ResponseBody



object ApiClient {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()

            val requestBuilder = original.newBuilder()
                .header("ex-hader","sample")
                .method(original.method(),original.body())

            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()

    var gson = GsonBuilder()
        .setLenient()
        .create()

    private const val BASE_URL = ipadress.ipadress

    val instance: Api by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

        retrofit.create(Api::class.java)
    }

    suspend fun requestforumsresult(page:Int, bestforum:Boolean):DefaultResponse2? {
        var result: DefaultResponse2? = null

        withContext(Dispatchers.IO)
        {
            launch {
                var call:Call<DefaultResponse2> = instance.requestforums(page,bestforum)
                result = call.execute().body()
            }
        }

        return result
    }
}
