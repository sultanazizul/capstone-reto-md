package com.example.reto.data.remote.api

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
            .connectTimeout(30, TimeUnit.SECONDS)  // Set timeout 30 detik untuk koneksi
            .readTimeout(30, TimeUnit.SECONDS)     // Set timeout 30 detik untuk membaca response
            .writeTimeout(30, TimeUnit.SECONDS)    // Set timeout 30 detik untuk menulis request
            .build()

        // Retrofit setup
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)  // Base URL API
            .addConverterFactory(GsonConverterFactory.create())  // Converter untuk JSON -> Object
            .client(client)  // Menambahkan OkHttpClient
            .build()

        // Mengembalikan ApiService
        return retrofit.create(ApiService::class.java)
    }
}
