package com.example.reto.data.repository

import com.example.reto.data.local.datastore.UserPreferences
import com.example.reto.data.local.model.UserModel

interface AuthRepository {
//    val userPreferences: UserPreferences

    suspend fun login(email: String, password: String): UserModel
    suspend fun register(email: String, password: String)
    suspend fun saveUser(user: UserModel)
//    suspend fun logout() = userPreferences.logout()
}