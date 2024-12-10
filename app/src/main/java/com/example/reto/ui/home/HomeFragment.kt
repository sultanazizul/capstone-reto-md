package com.example.reto.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.reto.R
import com.example.reto.ui.knowledge.TypeWaste
import com.example.reto.ui.knowledge.TypeWasteAdapter

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var typeWasteList: ArrayList<TypeWaste>
    private lateinit var typeWasteAdapter: TypeWasteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)

        // klik see all untuk masuk ke page knowledge
        val tvSeeAllKnowledge: TextView = view.findViewById(R.id.tvSeeAllKnowledge)
        tvSeeAllKnowledge.setOnClickListener {
            val action = HomeFragmentDirections
                .actionNavigationHomeToNavigationKnowledge()
            findNavController().navigate(action)
        }

        // klik see all untuk masuk ke page news
        val tvSeeAllNews: TextView = view.findViewById(R.id.tvSeeAllNews)
        tvSeeAllNews.setOnClickListener {
            val action = HomeFragmentDirections
                .actionNavigationHomeToNavigationNews()
            findNavController().navigate(action)
        }

    }

    private fun init(view: View){
        recyclerView = view.findViewById(R.id.rvTypeWaste)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL,
            false)

        val snapHelper : SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        typeWasteList = ArrayList()

        typeWasteList.addAll(addDataToList())
        typeWasteAdapter = TypeWasteAdapter(typeWasteList)
        recyclerView.adapter = typeWasteAdapter

        typeWasteAdapter.setOnItemClickCallback(object : TypeWasteAdapter.OnItemClickCallback{
            override fun onItemClicked(data: TypeWaste) {
                showSelected(data)
            }
        })
    }

    private fun addDataToList():ArrayList<TypeWaste>{
        val dataImg = resources.obtainTypedArray(R.array.data_image)
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataExample = resources.getStringArray(R.array.data_example)
        val dataImpact = resources.getStringArray(R.array.data_impact)
        val dataManage = resources.getStringArray(R.array.data_manage)

        val typeWasteList = ArrayList<TypeWaste>()
        for (i in dataName.indices){
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

    private fun showSelected(typeWaste: TypeWaste) {
        val action = HomeFragmentDirections
            .actionNavigationHomeToNavigationDetailKnowledge(typeWaste)
        findNavController().navigate(action)
    }


}