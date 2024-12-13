package com.example.reto.data.remote.response

import com.google.gson.annotations.SerializedName

data class NewsApiResponse(
    val success: Boolean,
    val message: String,
    val page: Int,
    val articles: List<NewsResponse>?
)


data class NewsResponse(
    @SerializedName("img")
    val img: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("articleLink")
    val articleLink: String
)
