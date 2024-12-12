package com.example.reto.data.remote.local

data class ClassificationResult(
    val probabilities: FloatArray? // This holds the classification probabilities from the TFLite model
)
