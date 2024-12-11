package com.example.reto.data.repository

import android.content.Context
import android.util.Log
import com.example.reto.data.local.datastore.UserPreferences
import com.example.reto.data.local.model.UserModel
import com.example.reto.data.remote.api.ApiConfig
import com.example.reto.data.remote.model.AuthRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    context: Context,
): AuthRepository {
    private val apiService = ApiConfig.getApiService()
    private val userPreferences = UserPreferences(context)

    override suspend fun login(email: String, password: String): UserModel {
        return withContext(Dispatchers.IO) {
            val request = AuthRequest(
                email = email,
                password = password
            )
            val response = apiService.login(request)
            UserModel(
                response.userCredential.user.uid,
                response.userCredential.user.providerData[0].displayName ?: "Guest",
                email,
                response.userCredential.tokenResponse?.idToken ?: "",
                false
            )
        }
    }

    override suspend fun register(email: String, password: String) {
        return withContext(Dispatchers.IO) {
            Log.d("TAG", "Registering user email: $email password: $password")
            val request = AuthRequest(
                email = email,
                password = password
            )
            apiService.register(request)
        }
    }

    override suspend fun saveUser(user: UserModel) {
        return withContext(Dispatchers.IO) {
            userPreferences.saveUser(user)
        }
    }

}