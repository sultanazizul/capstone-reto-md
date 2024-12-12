package com.example.reto.helper

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.reto.ml.Bestoneyet
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ImageClassifierHelper(private val context: Context) {

    private var model: Bestoneyet? = null

    init {
        // Load the model
        try {
            model = Bestoneyet.newInstance(context)
        } catch (e: Exception) {
            Log.e("ImageClassifierHelper", "Error loading model: ${e.message}")
        }
    }

    // Method to classify image and return the result
    fun classifyImage(bitmap: Bitmap): String {
        // Resize image to 512x512
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 512, 512, true)

        // Check if resizing was successful
        if (resizedBitmap.width != 512 || resizedBitmap.height != 512) {
            return "Error: Image resizing failed, unexpected dimensions."
        }

        // Convert bitmap to ByteBuffer
        val byteBuffer = bitmapToByteBuffer(resizedBitmap)

        // Prepare the input tensor for the model
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 512, 512, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        // Get the model's output
        val outputs = model?.process(inputFeature0)
        val outputFeature0 = outputs?.outputFeature0AsTensorBuffer

        // Handle null output
        if (outputFeature0 == null) {
            return "Error: Model output is null."
        }

        // Get the predicted class index
        val predictedClassIndex = outputFeature0.floatArray
            ?.withIndex()
            ?.maxByOrNull { it.value }?.index ?: -1

        // Calculate confidence
        val confidence = outputFeature0.floatArray?.maxOrNull() ?: 0.0f

        // Label classes
        val labels = listOf("B3", "Kaca", "Kertas", "Plastik", "Residu", "Metal", "Organik")

        // Return predicted class and confidence
        val predictedLabel = if (predictedClassIndex != -1) {
            labels[predictedClassIndex]
        } else {
            "Unknown"
        }

        return "Predicted: $predictedLabel with confidence: ${"%.2f".format(confidence * 100)}%"
    }

    // Method to convert Bitmap to ByteBuffer
    private fun bitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * 512 * 512 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(512 * 512)

        // Get the pixel data from the bitmap
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        var pixel = 0
        // Iterate through the pixels and put values into the byteBuffer
        for (i in 0 until 512) {
            for (j in 0 until 512) {
                val value = intValues[pixel++]
                byteBuffer.putFloat(((value shr 16 and 0xFF) / 255.0f))  // R
                byteBuffer.putFloat(((value shr 8 and 0xFF) / 255.0f))   // G
                byteBuffer.putFloat(((value and 0xFF) / 255.0f))        // B
            }
        }

        return byteBuffer
    }

    // Close the model to release resources
    fun close() {
        model?.close()
    }
}
