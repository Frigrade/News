package com.news.data.repository

import com.news.data.api.RetrofitInstance
import com.news.data.db.ArticleDatabase
import com.news.domain.entity.Article

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuary: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuary, pageNumber)

    suspend fun insert(article: Article) = db.getArticleDao().insert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

}