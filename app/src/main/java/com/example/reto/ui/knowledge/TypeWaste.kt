package com.example.reto.ui.knowledge

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TypeWaste(
    val typeWasteImage: Int,
    val typeWasteName: String,
    val typeWasteDescription: String,
    val typeWasteExample: String,
    val typeWasteImpact: String,
    val typeWasteManage: String
) : Parcelable

