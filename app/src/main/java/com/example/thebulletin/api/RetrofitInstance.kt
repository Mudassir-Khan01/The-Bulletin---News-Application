package com.example.thebulletin.api

import com.example.thebulletin.models.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object{
        private val retrofit by lazy {
            val logging=HttpLoggingInterceptor() //used for debugging
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client=OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        val api by lazy {  //actual api object that we will be able to use it from everywhere for actual network requests
            retrofit.create(NewsAPI::class.java)
        }
    }
}