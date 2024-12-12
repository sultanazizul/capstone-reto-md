package com.example.reto.ui.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import com.example.reto.R

class ResultFragment : Fragment() {

    private lateinit var ivPreview: ImageView
    private lateinit var tvResultName: TextView
    private lateinit var tvResultDescription: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivPreview = view.findViewById(R.id.iv_preview)
        tvResultName = view.findViewById(R.id.tvResultName)
        tvResultDescription = view.findViewById(R.id.tvResultDescription)

        val result = arguments?.getString("prediction") ?: "No result"
        tvResultName.text = result

        // Update deskripsi lebih lanjut jika perlu
        tvResultDescription.text = "Prediksi ini didasarkan pada model deteksi sampah."
    }
}
