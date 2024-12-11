package com.example.reto.ui.login

import com.example.reto.data.local.model.UserModel
import com.example.reto.data.remote.response.LoginResponse
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reto.data.repository.AuthRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    private val _registerResponse = MutableLiveData<LoginResponse>()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val userModel = authRepository.login(email, password)
                saveUser(userModel)
                _isLoading.postValue(false)
                Log.d(TAG, "onSuccess: $userModel")
            } catch (e: HttpException) {
                _isLoading.postValue(false)
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                val errorMessage = errorBody.message
                _isLoading.postValue(false)
                _registerResponse.postValue(errorBody)
                Log.d(TAG, "onError: $errorMessage")
            }
        }
    }

    private fun saveUser(user: UserModel) {
        viewModelScope.launch {
            authRepository.saveUser(user)
        }
    }
}