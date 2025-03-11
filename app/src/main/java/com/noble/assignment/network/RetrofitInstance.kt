package com.noble.assignment.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.noble.assignment.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private var retrofit: Retrofit? = null
    private var okHttpClient: OkHttpClient? = null
    private var apiInterface: ApiInterface? = null

    fun initRetrofit(context: Context) {
        if (retrofit == null) {
            retrofit = getRetrofitInstance(context)
            apiInterface = getApiInterface()
        }
    }

    private fun getClient(context: Context): OkHttpClient {
        return if (okHttpClient == null) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val builder = OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)

            if (BuildConfig.DEBUG) {
                builder.addInterceptor(ChuckerInterceptor(context))
                builder.addInterceptor(logging)
            }



            okHttpClient = builder.build()
            return okHttpClient!!
        } else okHttpClient!!
    }

    fun getApiInterface(): ApiInterface {
        return if (apiInterface == null) {
            apiInterface = retrofit?.create(ApiInterface::class.java)!!
            return apiInterface!!
        } else apiInterface!!
    }

    private fun getRetrofitInstance(context: Context): Retrofit {

        return Retrofit
            .Builder()
            .baseUrl(BuildConfig.BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getClient(context))
            .build()
    }
}