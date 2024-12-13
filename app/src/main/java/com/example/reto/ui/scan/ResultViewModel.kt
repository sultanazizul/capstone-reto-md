package com.example.reto.ui.scan

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResultViewModel : ViewModel() {

    // LiveData to hold the image and the result
    val imageLiveData = MutableLiveData<Bitmap>()
    val resultLiveData = MutableLiveData<String>()

    // Method to set the image
    fun setImage(bitmap: Bitmap) {
        imageLiveData.value = bitmap
    }

    // Method to set the result
    fun setResult(result: String) {
        resultLiveData.value = result
    }
}
