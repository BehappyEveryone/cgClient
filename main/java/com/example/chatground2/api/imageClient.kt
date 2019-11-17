package com.example.chatground2.api

import com.example.chatground2.ipadress
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object imageClient {

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

    val instance: imageApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

        retrofit.create(imageApi::class.java)
    }
}
