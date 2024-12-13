package com.example.reto.ui.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reto.R
import com.example.reto.databinding.FragmentNewsBinding
import com.example.reto.data.remote.api.ApiConfig
import com.example.reto.data.remote.response.NewsApiResponse
import com.example.reto.data.remote.response.NewsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupListeners()
        fetchAllNews() // Load all news by default

        return binding.root
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(
            mutableListOf(),
            onItemClick = { article ->
                val bundle = bundleOf("url" to article.articleLink)
                findNavController().navigate(
                    R.id.navigation_webview,  // Make sure this ID matches your nav graph
                    bundle
                )
            }
        )
        binding.rvNews.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNews.adapter = newsAdapter
    }

    private fun setupListeners() {
        binding.btnAll.setOnClickListener {
            fetchAllNews()
        }

        binding.btnOrganic.setOnClickListener {
            fetchNews("organik")
        }

        binding.btnNonOrganic.setOnClickListener {
            fetchNews("non-organik")
        }
    }

    private fun fetchAllNews() {
        showLoading(true)
        newsAdapter.updateData(mutableListOf())

        val apiService = ApiConfig.getApiService()

        lifecycleScope.launch {
            try {
                val organikDeferred = async(Dispatchers.IO) { apiService.getOrganikNews() }
                val nonOrganikDeferred = async(Dispatchers.IO) { apiService.getNonOrganikNews() }

                val organikResponse = organikDeferred.await()
                val nonOrganikResponse = nonOrganikDeferred.await()

                val articles = mutableListOf<NewsResponse>().apply {
                    organikResponse.articles?.let { addAll(it) }
                    nonOrganikResponse.articles?.let { addAll(it) }
                }

                newsAdapter.updateData(articles)

            } catch (e: Exception) {
                Log.e("NewsFragment", "Error: ${e.message}")
                Toast.makeText(requireContext(), "Gagal memuat berita: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                showLoading(false)
            }
        }
    }

    private fun fetchNews(type: String) {
        showLoading(true)
        newsAdapter.updateData(mutableListOf())

        val apiService = ApiConfig.getApiService()

        lifecycleScope.launch {
            try {
                val response = when (type) {
                    "organik" -> apiService.getOrganikNews()
                    "non-organik" -> apiService.getNonOrganikNews()
                    else -> throw IllegalArgumentException("Unknown news type")
                }

                response.articles?.let {
                    newsAdapter.updateData(it)
                } ?: run {
                    Toast.makeText(requireContext(), "Tidak ada artikel tersedia", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                if (e is UnknownHostException) {
                    Log.e("NewsFragment", "Unable to resolve host: ${e.message}")
                    Toast.makeText(requireContext(), "Tidak dapat terhubung ke server. Periksa koneksi internet Anda.", Toast.LENGTH_LONG).show()
                } else {
                    Log.e("NewsFragment", "Error: ${e.message}")
                    Toast.makeText(requireContext(), "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
