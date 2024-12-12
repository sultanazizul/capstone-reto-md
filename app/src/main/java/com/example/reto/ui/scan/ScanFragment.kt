package com.example.reto.ui.scan

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.reto.R
import androidx.camera.view.PreviewView
import java.io.File

class ScanFragment : Fragment() {

    private var imageCapture: ImageCapture? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_scan, container, false)

        // Set up camera preview
        val previewView: PreviewView = binding.findViewById(R.id.previewView)
        val captureButton: Button = binding.findViewById(R.id.captureButton)

        // Setup CameraX
        startCamera(previewView)

        // Capture image when the button is clicked
        captureButton.setOnClickListener {
            captureImage()
        }


        // Initialize views
        val uploadButton: ImageButton = binding.findViewById(R.id.uploadButton)

        // Set up click listener for upload button
        uploadButton.setOnClickListener {
            openGallery()
        }

        return binding
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri = data.data!!
            navigateToUploadFragment(imageUri)
        }
    }

    private fun startCamera(previewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(previewView.surfaceProvider)

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, preview, imageCapture)

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun captureImage() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(requireContext().filesDir, "captured_image.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                // Image saved successfully
                val imageUri = outputFileResults.savedUri
                navigateToUploadFragment(imageUri)
            }

            override fun onError(exception: ImageCaptureException) {
                // Handle the error here
            }
        })
    }

    private fun navigateToUploadFragment(imageUri: Uri?) {
        imageUri?.let {
            val bundle = Bundle().apply {
                putString("imageUri", it.toString()) // Convert Uri to String
            }
            findNavController().navigate(R.id.action_scanFragment_to_uploadFragment, bundle)
        } ?: run {
            // Jika imageUri null, bisa memberi pesan error atau penanganan lainnya
            Toast.makeText(requireContext(), "Error: Image capture failed.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 1001
    }

}
