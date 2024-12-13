package com.example.reto.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.reto.R
import com.example.reto.ui.knowledge.TypeWaste
import com.example.reto.ui.knowledge.TypeWasteAdapter
import com.example.reto.ui.news.NewsAdapter
import com.example.reto.data.remote.api.ApiConfig
import com.example.reto.data.remote.response.NewsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var typeWasteList: ArrayList<TypeWaste>
    private lateinit var typeWasteAdapter: TypeWasteAdapter
    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        loadNews()

        view.findViewById<TextView>(R.id.tvSeeAllKnowledge).setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_knowledge)
        }

        view.findViewById<TextView>(R.id.tvSeeAllNews).setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_news)
        }
    }

    private fun init(view: View) {
        recyclerView = view.findViewById(R.id.rvTypeWaste)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL,
            false
        )

        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        typeWasteList = ArrayList()
        typeWasteList.addAll(addDataToList())
        typeWasteAdapter = TypeWasteAdapter(typeWasteList)
        recyclerView.adapter = typeWasteAdapter

        typeWasteAdapter.setOnItemClickCallback(object : TypeWasteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: TypeWaste) {
                showSelected(data)
            }
        })

        newsRecyclerView = view.findViewById(R.id.rvNewsHome)
        newsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        newsAdapter = NewsAdapter(mutableListOf()) { article ->
            Toast.makeText(requireContext(), article.title, Toast.LENGTH_SHORT).show()
        }
        newsRecyclerView.adapter = newsAdapter
    }

    private fun loadNews() {
        val apiService = ApiConfig.getApiService()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getOrganikNews() // Ambil berita organik sebagai contoh
                val articles = response.articles?.take(5) ?: emptyList() // Ambil hanya 5 data
                lifecycleScope.launch(Dispatchers.Main) {
                    newsAdapter.updateData(articles)
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "Error: ${e.message}")
                lifecycleScope.launch(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Gagal memuat berita: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
                typeWasteImage = dataImg.getResourceId(i, -1),
                typeWasteName = dataName[i],
                typeWasteDescription = dataDescription[i],
                typeWasteExample = dataExample[i],
                typeWasteImpact = dataImpact[i],
                typeWasteManage = dataManage[i]
            )
            typeWasteList.add(typeWaste)
        }
        dataImg.recycle()
        return typeWasteList
    }

    private fun showSelected(typeWaste: TypeWaste) {
        val bundle = Bundle().apply {
            putParcelable("typeWaste", typeWaste)
        }
        findNavController().navigate(
            R.id.action_navigation_home_to_navigation_detail_knowledge,
            bundle
        )
    }
}
