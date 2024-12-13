package com.example.reto.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

class ImageClassifierHelper(private val context: Context) {

    private var interpreter: Interpreter? = null;

    init {
        try {
            // Load the TFLite model
            val modelFile = loadModelFile("bestoneyet.tflite");
            interpreter = Interpreter(modelFile);
        } catch (e: Exception) {
            Log.e("ImageClassifierHelper", "Error loading model: ${e.message}");
        }
    }

    // Load model file from assets folder
    private fun loadModelFile(modelFileName: String): ByteBuffer {
        val assetFileDescriptor = context.assets.openFd(modelFileName);
        val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor);
        val fileChannel = fileInputStream.channel;
        val startOffset = assetFileDescriptor.startOffset;
        val declaredLength = assetFileDescriptor.declaredLength;
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    // Tambahkan deskripsi untuk setiap label di ImageClassifierHelper
    fun classifyImage(bitmap: Bitmap): Pair<String, String> {
        // Resize the bitmap to the model input size
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 512, 512, true);

        // Convert the bitmap to ByteBuffer
        val inputBuffer = bitmapToByteBuffer(resizedBitmap);

        // Allocate output buffer
        val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 7), org.tensorflow.lite.DataType.FLOAT32);

        // Run inference
        interpreter?.run(inputBuffer, outputBuffer.buffer);

        // Get the predicted class
        val predictions = outputBuffer.floatArray;
        val predictedIndex = predictions.indices.maxByOrNull { predictions[it] } ?: -1;

        // Label classes
        val labels = listOf("B3", "Kaca", "Kertas", "Plastik", "Residu", "Metal", "Organik");

        val predictedLabel = if (predictedIndex != -1) {
            labels[predictedIndex];
        } else {
            "Unknown";
        }

        // Map specific predictions to "anorganik"
        val anorganikLabels = listOf("Kaca", "Kertas", "Plastik", "Metal");
        val finalLabel = if (predictedLabel in anorganikLabels) "Anorganik" else predictedLabel;

        // Deskripsi hasil prediksi
        val description = getResultDescription(finalLabel)

        return Pair(finalLabel, description)
    }

    // Fungsi untuk memberikan deskripsi berdasarkan label
    private fun getResultDescription(result: String): String {
        return when (result) {
            "Kaca", "Plastik", "Kertas", "Metal" -> {
                "Anorganik mencakup material seperti kaca, plastik, logam, dan kertas. " +
                        "Bahan ini dapat didaur ulang menjadi barang baru, seperti botol kaca, " +
                        "kantong plastik, dan barang logam. Untuk daur ulang, pastikan bahan ini bersih " +
                        "dari kotoran dan tidak tercampur dengan bahan organik atau B3. Recycle untuk " +
                        "mengurangi limbah dan mendukung lingkungan."
            }
            "B3" -> {
                "B3 (Bahan Berbahaya dan Beracun) meliputi bahan kimia, baterai, atau perangkat elektronik " +
                        "yang dapat membahayakan kesehatan manusia dan lingkungan. Pengelolaan yang tepat " +
                        "penting untuk menghindari kontaminasi dan kerusakan lingkungan. Jangan dibuang sembarangan. " +
                        "Serahkan ke fasilitas daur ulang B3 yang terdaftar."
            }
            "Residu" -> {
                "Residu adalah limbah yang tidak bisa didaur ulang atau diproses lebih lanjut. Biasanya, ini " +
                        "terdiri dari bahan yang terkontaminasi atau sudah terlalu rusak untuk diolah. " +
                        "Penting untuk meminimalkan limbah ini dengan mengurangi konsumsi dan memilih produk yang lebih ramah lingkungan."
            }
            "Organik" -> {
                "Sampah organik berasal dari bahan-bahan alami seperti sisa makanan dan dedaunan. Sampah ini " +
                        "dapat diolah menjadi kompos yang berguna untuk pertanian atau kebun. " +
                        "Cobalah untuk membuat kompos di rumah atau serahkan ke tempat daur ulang organik."
            }
            else -> {
                "Prediksi ini didasarkan dari model deteksi sampah."
            }
        }
    }

    // Convert Bitmap to ByteBuffer
    private fun bitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * 512 * 512 * 3);
        byteBuffer.order(ByteOrder.nativeOrder());
        val intValues = IntArray(512 * 512);
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height);

        var pixel = 0;
        for (i in 0 until 512) {
            for (j in 0 until 512) {
                val value = intValues[pixel++];
                byteBuffer.putFloat((value shr 16 and 0xFF).toFloat()); // R
                byteBuffer.putFloat((value shr 8 and 0xFF).toFloat());  // G
                byteBuffer.putFloat((value and 0xFF).toFloat());        // B
            }
        }
        return byteBuffer;
    }

    // Release resources
    fun close() {
        interpreter?.close();
    }
}
