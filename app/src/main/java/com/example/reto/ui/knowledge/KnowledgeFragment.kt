package com.example.reto.ui.knowledge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reto.R
import com.example.reto.databinding.FragmentKnowledgeBinding

class KnowledgeFragment : Fragment() {
    private var _binding: FragmentKnowledgeBinding? = null
    private val binding get() = _binding!!

    private val typeWasteList by lazy {
        addDataToList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKnowledgeBinding.inflate(inflater, container, false)
        val view = binding.root

        setListeners()

        return view
    }

    private fun setListeners() {
        binding.apply {
            cvLiqiuidWaste.setOnClickListener {
                navigateToDetail(typeWasteList[0])
            }

            cvSolidWaste.setOnClickListener {
                navigateToDetail(typeWasteList[1])
            }

            cvOrganicWaste.setOnClickListener {
                navigateToDetail(typeWasteList[2])
            }

            cvHazardousWaste.setOnClickListener {
                navigateToDetail(typeWasteList[3])
            }
        }
    }

    private fun addDataToList(): ArrayList<TypeWaste> {
        val dataImg = resources.obtainTypedArray(R.array.data_image)
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataExample = resources.getStringArray(R.array.data_example)
        val dataImpact = resources.getStringArray(R.array.data_impact)
        val dataManage = resources.getStringArray(R.array.data_manage)

        val typeWasteList = ArrayList<TypeWaste>()
        for (i in dataName.indices) {
            val typeWaste = TypeWaste(
                dataImg.getResourceId(i, -1),
                dataName[i],
                dataDescription[i],
                dataExample[i],
                dataImpact[i],
                dataManage[i]
            )
            typeWasteList.add(typeWaste)
        }
        dataImg.recycle()
        return typeWasteList
    }

    private fun navigateToDetail(typeWaste: TypeWaste) {
        val action =
            KnowledgeFragmentDirections.actionNavigationKnowledgeToNavigationDetailKnowledge(
                typeWaste
            )
        findNavController().navigate(action)
    }
}
