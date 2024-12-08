package com.example.reto.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class RegisterResponse(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("email")
	val email: String
) : Parcelable
