package com.example.reto.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.reto.data.repository.AuthRepository
import com.example.reto.di.Injection
import com.example.reto.ui.login.LoginViewModel
import com.example.reto.ui.register.RegisterViewModel

class ViewModelFactory(
    private val authRepository: AuthRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(this.authRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(this.authRepository) as T
            }
            else -> {
                throw IllegalArgumentException("ViewModel Not Found: ${modelClass.name}")
            }
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideAuthRepository(context))
            }.also { instance = it }
    }
}