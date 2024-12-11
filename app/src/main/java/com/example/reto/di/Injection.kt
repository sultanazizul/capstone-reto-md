package com.example.reto.di

import android.content.Context
import com.example.reto.data.repository.AuthRepository
import com.example.reto.data.repository.AuthRepositoryImpl

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        return AuthRepositoryImpl(context)
    }
}