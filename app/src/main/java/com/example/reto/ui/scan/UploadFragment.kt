package com.example.reto.ui.scan

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reto.R
import com.google.android.material.button.MaterialButton
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.util.UUID
import com.example.reto.ml.Bestoneyet
import org.tensorflow.lite.support.image.TensorImage
import com.example.reto.helper.ImageClassifierHelper

class UploadFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var btnCrop: ImageButton
    private lateinit var btnDelete: ImageButton
    private lateinit var btnAnalyze: MaterialButton  // Tombol Analisis
    private lateinit var classifierHelper: ImageClassifierHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_upload, container, false)

        imageView = binding.findViewById(R.id.iv_preview)
        btnCrop = binding.findViewById(R.id.btnCrop)
        btnDelete = binding.findViewById(R.id.btnDelete)
        btnAnalyze = binding.findViewById(R.id.analyzeButton)  // Inisialisasi tombol Analisis

        classifierHelper = ImageClassifierHelper(requireContext())  // Inisialisasi ImageClassifierHelper

        // Get the image URI passed from ScanFragment
        val imageUriString = arguments?.getString("imageUri")
        imageUriString?.let {
            val imageUri = Uri.parse(it)
            showCapturedImage(imageUri)

            // Set up crop button functionality
            btnCrop.setOnClickListener {
                startCropImage(imageUri)
            }
        } ?: run {
            Toast.makeText(requireContext(), "Failed to load image.", Toast.LENGTH_SHORT).show()
        }

        // Set up delete button functionality
        btnDelete.setOnClickListener {
            deleteImage() // Hapus gambar
        }

        // Set up analyze button functionality to run AI model
        btnAnalyze.setOnClickListener {
            analyzeImage() // Menjalankan model AI untuk analisis gambar
        }

        return binding
    }

    private fun showCapturedImage(imageUri: Uri) {
        try {
            val bitmap = BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(imageUri))
            imageView.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startCropImage(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "${UUID.randomUUID()}.jpg"))
        UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1080, 1080)
            .start(requireContext(), this)
    }

    private fun deleteImage() {
        imageView.setImageDrawable(null)
        Toast.makeText(requireContext(), "Image deleted", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    private fun analyzeImage() {
        // Cek apakah gambar ada di ImageView
        val bitmap = (imageView.drawable as? BitmapDrawable)?.bitmap
        bitmap?.let {
            val result = runModelAI(it)
            // Setelah mendapatkan hasil, navigasi ke ResultFragment dan kirimkan hasilnya
            val action = UploadFragmentDirections.actionUploadFragmentToResultFragment(result)
            findNavController().navigate(action)
        } ?: run {
            Toast.makeText(requireContext(), "No image to analyze", Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi untuk menjalankan model AI dan mendapatkan hasil prediksi
    private fun runModelAI(bitmap: Bitmap): String {
        return try {
            // Memanggil fungsi classifyImage dari ImageClassifierHelper
            val result = classifierHelper.classifyImage(bitmap)
            result
        } catch (e: Exception) {
            e.printStackTrace()
            "Error analyzing image"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let {
                showCapturedImage(it)
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            cropError?.printStackTrace()
            Toast.makeText(requireContext(), "Crop error: ${cropError?.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        classifierHelper.close()  // Tutup model ketika fragment dihancurkan
    }
}
