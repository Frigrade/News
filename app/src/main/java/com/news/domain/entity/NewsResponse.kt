package com.news.domain.entity

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)
