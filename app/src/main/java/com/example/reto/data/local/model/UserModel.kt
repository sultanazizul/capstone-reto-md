package com.example.reto.data.local.model

data class UserModel(
    val id: String,
    val name: String,
    val email: String,
    val token: String,
    val emailVerified: Boolean
)