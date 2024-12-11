package com.example.reto.data.remote.api

import com.example.reto.data.remote.model.AuthRequest
import com.example.reto.data.remote.response.LoginResponse
import com.example.reto.data.remote.response.RegisterResponse
import com.example.reto.data.remote.response.ResetPassResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    suspend fun register (
        @Body requestBody: AuthRequest
    ): RegisterResponse

    @POST("login")
    suspend fun login (
        @Body requestBody: AuthRequest
    ): LoginResponse

    @POST("reset-password")
    suspend fun resetPassword (
        @Field("email") email: String
    ): ResetPassResponse

}
