package com.news.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.news.data.repository.NewsRepository
import com.news.domain.entity.Article
import com.news.domain.entity.NewsResponse
import com.news.presentation.state.NewsState
import kotlinx.coroutines.launch
import retrofit2.Response


class NewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {
    val breakingNewsLiveData: MutableLiveData<NewsState> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNewsLiveData: MutableLiveData<NewsState> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNewsLiveData.postValue(NewsState.Loading)
        val rawResponse = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
        breakingNewsLiveData.postValue(handleBreakingNewsResponse(rawResponse))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): NewsState {
        if (response.isSuccessful) {
            response.body()?.let {
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = it
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = it.articles
                    oldArticles?.addAll(newArticles)
                }
                return NewsState.Content(breakingNewsResponse ?: it)
            }
        }
        return NewsState.Error(response.message())
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNewsLiveData.postValue(NewsState.Loading)
        val rawResponse = newsRepository.searchNews(searchQuery, searchNewsPage)
        searchNewsLiveData.postValue(handleSearchNewsResponse(rawResponse))
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): NewsState {
        if (response.isSuccessful) {
            response.body()?.let {
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = it
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = it.articles
                    oldArticles?.addAll(newArticles)
                }
                return NewsState.Content(searchNewsResponse ?: it)
            }
        }
        return NewsState.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.insert(article)
    }

    fun savedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

}