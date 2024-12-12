package com.example.reto.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reto.databinding.ItemNewsBinding
import com.example.reto.data.remote.response.NewsResponse

class NewsAdapter(private val newsList: MutableList<NewsResponse>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    // Method untuk update data di RecyclerView
    fun updateData(newData: List<NewsResponse>) {
        newsList.clear() // Clear data lama
        newsList.addAll(newData) // Tambahkan data baru
        notifyDataSetChanged() // Memberitahu adapter untuk meng-update RecyclerView
    }

    // Membuat ViewHolder untuk item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    // Mengikat data ke tampilan di setiap item
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.bind(news)
    }

    // Mengembalikan jumlah item dalam list
    override fun getItemCount(): Int = newsList.size

    // ViewHolder untuk mengikat data ke tampilan
    inner class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: NewsResponse) {
            binding.tvItemName.text = news.title
            binding.tvItemDescription.text = news.description
            Glide.with(binding.imgItemPhoto.context)
                .load(news.img) // Memuat gambar menggunakan Glide
                .into(binding.imgItemPhoto)
        }
    }
}
