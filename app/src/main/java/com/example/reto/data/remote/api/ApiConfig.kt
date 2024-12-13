package com.example.reto.data.remote.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {

    private const val BASE_URL = "https://reto.my.id/api/" // Ganti ke HTTPS jika API sudah mendukung

    // Fungsi untuk mendapatkan ApiService
    fun getApiService(): ApiService {
        // Interceptor untuk logging request dan response
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        // OkHttpClient dengan interceptor dan timeout
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Logging interceptor
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        // Retrofit setup
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory()) // Menambahkan coroutine adapter
            .client(client)
            .build()


        // Mengembalikan ApiService
        return retrofit.create(ApiService::class.java)
    }
}
