package com.searchinrecyclerviewexample.network

import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import com.searchinrecyclerviewexample.utils.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {

        private val retrofit by lazy {

            val client = OkHttpClient.Builder()
                .addInterceptor(OkHttpProfilerInterceptor())
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api: APIEndPoints by lazy {
            retrofit.create(APIEndPoints::class.java)
        }
    }
}