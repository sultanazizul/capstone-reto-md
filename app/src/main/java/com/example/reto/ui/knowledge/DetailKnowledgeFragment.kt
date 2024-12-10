package com.example.reto.ui.knowledge

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.reto.R
import com.example.reto.databinding.FragmentDetailKnowledgeBinding

class DetailKnowledgeFragment : Fragment() {

    private var _binding: FragmentDetailKnowledgeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailKnowledgeBinding.inflate(inflater, container, false)
        val view = binding.root

        val typeWaste = DetailKnowledgeFragmentArgs.fromBundle(arguments as Bundle).typeWaste

        // Mengambil data dari arguments
        val typeWasteImg = typeWaste.typeWasteImage
        val typeWasteName = typeWaste.typeWasteName
        val typeWasteDesc = typeWaste.typeWasteDescription
        val typeWasteExample = typeWaste.typeWasteExample
        val typeWasteImpact = typeWaste.typeWasteImpact
        val typeWasteManage = typeWaste.typeWasteManage

        // Menampilkan data ke UI
        binding.imgTypeWaste.setImageResource(typeWasteImg)
        binding.tvTypeWasteName.text = typeWasteName
        binding.tvTypeWasteDescription.text = typeWasteDesc
        binding.tvTypeWasteExample.text = typeWasteExample
        binding.tvTypeWasteImpact.text = typeWasteImpact
        binding.tvTypeWasteManage.text = typeWasteManage

        // Menampilkan deskripsi detail
        val knowledgeDetailedDescription = getDetailedDescription(typeWasteName)
        binding.tvTypeWasteDescription.text = knowledgeDetailedDescription

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.shareButton.setOnClickListener{
            shareDetails(
                typeWasteName,
                typeWasteDesc,
                typeWasteExample,
                typeWasteImpact,
                typeWasteManage
            )
        }
        return view
    }

    private fun shareDetails(
        typeWasteName: String?,
        typeWasteDesc: String?,
        typeWasteExample: String?,
        typeWasteImpact: String?,
        typeWasteManage: String?) {
        val shareText = """
            Mari Kita Kenali Jenis Sampah!:
            Nama           : $typeWasteName
            Deskripsi      : $typeWasteDesc
            Contoh         : $typeWasteExample
            Dampak         : $typeWasteImpact
            Pengelolaannya : $typeWasteManage
        """.trimIndent()


        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }


        startActivity(Intent.createChooser(shareIntent, "Share this Knowledge details via"))
    }

    private fun getDetailedDescription(tvTypeWasteName: String?): String {
        val dataName = resources.getStringArray(R.array.data_name)
        val detailedDescriptions = resources.getStringArray(R.array.data_description)

        dataName.forEachIndexed { index, name ->
            if (name.equals(tvTypeWasteName, ignoreCase = true)) {
                return detailedDescriptions[index]
            }
        }
        return "No detailed description available."
    }

    companion object {
        const val EXTRA_IMG = "extra_image"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESC = "extra_description"
        const val EXTRA_EXAMPLE = "extra_example"
        const val EXTRA_IMPACT = "extra_impact"
        const val EXTRA_MANAGE = "extra_manage"

        // Fungsi untuk membuat fragment dengan arguments
        fun newInstance(
            img: Int, name: String, desc: String, example: String,
            impact: String, manage: String
        ): DetailKnowledgeFragment {
            val fragment = DetailKnowledgeFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_IMG, img)
            bundle.putString(EXTRA_NAME, name)
            bundle.putString(EXTRA_DESC, desc)
            bundle.putString(EXTRA_EXAMPLE, example)
            bundle.putString(EXTRA_IMPACT, impact)
            bundle.putString(EXTRA_MANAGE, manage)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
