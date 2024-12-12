package com.example.reto.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthRequest(
    @field:SerializedName("email")
    val email: String,
    @field:SerializedName("password")
    val password: String,
) : Parcelable