package com.example.reto.data.remote.api

import com.example.reto.data.remote.response.LoginResponse
import com.example.reto.data.remote.response.RegisterResponse
import com.example.reto.data.remote.response.ResetPassResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register (
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login (
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("reset-password")
    suspend fun resetPassword (
        @Field("email") email: String
    ): ResetPassResponse

}
