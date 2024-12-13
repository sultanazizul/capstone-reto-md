package com.example.reto.data.remote.api

import com.example.reto.data.remote.response.NewsApiResponse
import retrofit2.http.GET

interface ApiService {
    @GET("organik")
    suspend fun getOrganikNews(): NewsApiResponse

    @GET("non-organik")
    suspend fun getNonOrganikNews(): NewsApiResponse
}
