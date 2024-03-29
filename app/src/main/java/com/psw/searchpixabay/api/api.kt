package com.psw.searchpixabay.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object api {
    val BASE = "https://pixabay.com/"
    val builder = OkHttpClient.Builder()
        .addInterceptor( HttpLoggingInterceptor().apply {
            // Debug시에 모든 패킷을 덤프
            setLevel(HttpLoggingInterceptor.Level.BODY)
        } )

    val pix: PixabayReq
        get() {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build()
            return retrofit.create<PixabayReq>(PixabayReq::class.java!!)
        }
}