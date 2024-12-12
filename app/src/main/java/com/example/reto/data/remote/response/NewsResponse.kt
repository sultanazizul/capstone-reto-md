package com.example.reto.data.remote.response

data class NewsApiResponse(
    val success: Boolean,
    val message: String,
    val page: Int,
    val articles: List<NewsResponse>?
)

data class NewsResponse(
    val img: String,
    val title: String,
    val description: String,
    val articleLink: String
)

