package com.example.reto.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reto.databinding.ItemNewsBinding
import com.example.reto.data.remote.response.NewsResponse

class NewsAdapter(
    private var articles: MutableList<NewsResponse>,
    private val onItemClick: (NewsResponse) -> Unit  // Ubah parameter menjadi NewsResponse
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    fun updateData(newArticles: List<NewsResponse>) {
        articles.clear()
        articles.addAll(newArticles)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = articles[position]  // Gunakan articles bukan newsList
        holder.bind(news)
    }

    override fun getItemCount(): Int = articles.size  // Gunakan articles bukan newsList

    inner class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: NewsResponse) {
            binding.tvItemName.text = news.title
            binding.tvItemDescription.text = news.description
            Glide.with(binding.imgItemPhoto.context)
                .load(news.img)
                .into(binding.imgItemPhoto)

            // Set klik listener
            binding.root.setOnClickListener {
                onItemClick(news)  // Pass seluruh objek NewsResponse
            }
        }
    }
}