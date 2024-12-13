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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.reto.R
import com.google.android.material.button.MaterialButton
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.UUID
import com.example.reto.helper.ImageClassifierHelper

class UploadFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var btnCrop: ImageButton
    private lateinit var btnDelete: ImageButton
    private lateinit var btnAnalyze: MaterialButton
    private lateinit var classifierHelper: ImageClassifierHelper

    // Using shared ViewModel
    private val resultViewModel: ResultViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_upload, container, false)

        imageView = binding.findViewById(R.id.iv_preview)
        btnCrop = binding.findViewById(R.id.btnCrop)
        btnDelete = binding.findViewById(R.id.btnDelete)
        btnAnalyze = binding.findViewById(R.id.analyzeButton)

        classifierHelper = ImageClassifierHelper(requireContext())

        val imageUriString = arguments?.getString("imageUri")
        imageUriString?.let {
            val imageUri = Uri.parse(it)
            showCapturedImage(imageUri)

            btnCrop.setOnClickListener {
                startCropImage(imageUri)
            }
        } ?: run {
            Toast.makeText(requireContext(), "Failed to load image.", Toast.LENGTH_SHORT).show()
        }

        btnDelete.setOnClickListener {
            deleteImage()
        }

        btnAnalyze.setOnClickListener {
            analyzeImage()
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
        val bitmap = (imageView.drawable as? BitmapDrawable)?.bitmap
        bitmap?.let {
            val result = runModelAI(it)

            // Set image and result in the ViewModel
            resultViewModel.setImage(it)
            resultViewModel.setResult(result.first) // Set label in ViewModel

            // Navigate to ResultFragment with result and description
            val action = UploadFragmentDirections.actionUploadFragmentToResultFragment(
                result = result.first,
                imageUri = result.second,
                description = result.second, // Ensure this is provided
            )
            findNavController().navigate(action)
        } ?: run {
            Toast.makeText(requireContext(), "No image to analyze", Toast.LENGTH_SHORT).show()
        }
    }


    private fun runModelAI(bitmap: Bitmap): Pair<String, String> {
        return try {
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
            classifierHelper.classifyImage(resizedBitmap) // Return Pair directly
        } catch (e: Exception) {
            e.printStackTrace()
            Pair("Error", "Error analyzing image") // Default error case
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let { showCapturedImage(it) }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            cropError?.printStackTrace()
            Toast.makeText(requireContext(), "Crop error: ${cropError?.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        classifierHelper.close()
    }
}
