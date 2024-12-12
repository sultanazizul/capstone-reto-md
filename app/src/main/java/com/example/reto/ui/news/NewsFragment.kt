package com.example.reto.ui.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reto.databinding.FragmentNewsBinding
import com.example.reto.data.remote.api.ApiConfig
import com.example.reto.data.remote.response.NewsApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        setupRecyclerView()
        setupListeners()

        return binding.root
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(mutableListOf())  // Initialize with an empty mutable list
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
        // Show progress bar
        showLoading(true)

        // Clear the old data from adapter
        newsAdapter.updateData(mutableListOf())

        val apiService = ApiConfig.getApiService()

        val callOrganic = apiService.getOrganikNews()
        val callNonOrganic = apiService.getNonOrganikNews()

        callOrganic.enqueue(object : Callback<NewsApiResponse> {
            override fun onResponse(call: Call<NewsApiResponse>, response: Response<NewsApiResponse>) {
                if (response.isSuccessful) {
                    val newsApiResponse = response.body()
                    if (newsApiResponse != null && newsApiResponse.success) {
                        newsApiResponse.articles?.let {
                            Log.d("NewsFragment", "Received organic articles size: ${it.size}")
                            newsAdapter.updateData(it)  // Update the RecyclerView with new data
                        } ?: run {
                            Log.e("NewsFragment", "No articles available in response")
                        }
                    } else {
                        Log.e("NewsFragment", "Failed to load organic articles")
                    }
                } else {
                    Log.e("NewsFragment", "Failed to load organic news: ${response.message()}")
                }
                // After receiving data, hide the progress bar
                showLoading(false)
            }

            override fun onFailure(call: Call<NewsApiResponse>, t: Throwable) {
                Log.e("NewsFragment", "Error: ${t.message}")
                showLoading(false)
            }
        })

        callNonOrganic.enqueue(object : Callback<NewsApiResponse> {
            override fun onResponse(call: Call<NewsApiResponse>, response: Response<NewsApiResponse>) {
                if (response.isSuccessful) {
                    val newsApiResponse = response.body()
                    if (newsApiResponse != null && newsApiResponse.success) {
                        newsApiResponse.articles?.let {
                            Log.d("NewsFragment", "Received non-organic articles size: ${it.size}")
                            newsAdapter.updateData(it)  // Update the RecyclerView with new data
                        } ?: run {
                            Log.e("NewsFragment", "No articles available in response")
                        }
                    } else {
                        Log.e("NewsFragment", "Failed to load non-organic articles")
                    }
                } else {
                    Log.e("NewsFragment", "Failed to load non-organic news: ${response.message()}")
                }
                // After receiving data, hide the progress bar
                showLoading(false)
            }

            override fun onFailure(call: Call<NewsApiResponse>, t: Throwable) {
                Log.e("NewsFragment", "Error: ${t.message}")
                showLoading(false)
            }
        })
    }

    private fun fetchNews(type: String) {
        // Show progress bar
        showLoading(true)

        // Clear the old data from adapter
        newsAdapter.updateData(mutableListOf())

        val apiService = ApiConfig.getApiService()

        val call = if (type == "organik") {
            apiService.getOrganikNews()
        } else {
            apiService.getNonOrganikNews()
        }

        call.enqueue(object : Callback<NewsApiResponse> {
            override fun onResponse(call: Call<NewsApiResponse>, response: Response<NewsApiResponse>) {
                if (response.isSuccessful) {
                    val newsApiResponse = response.body()
                    if (newsApiResponse != null && newsApiResponse.success) {
                        newsApiResponse.articles?.let {
                            Log.d("NewsFragment", "Received articles size: ${it.size}")
                            newsAdapter.updateData(it)  // Update the RecyclerView with new data
                        } ?: run {
                            Log.e("NewsFragment", "No articles available in response")
                        }
                    } else {
                        Log.e("NewsFragment", "Failed to load articles: ${newsApiResponse?.message}")
                        Toast.makeText(requireContext(), "Gagal memuat artikel", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("NewsFragment", "Failed to load news: ${response.message()}")
                    Toast.makeText(requireContext(), "Gagal memuat berita", Toast.LENGTH_SHORT).show()
                }
                // After receiving data, hide the progress bar
                showLoading(false)
            }

            override fun onFailure(call: Call<NewsApiResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                showLoading(false)
            }
        })
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}


