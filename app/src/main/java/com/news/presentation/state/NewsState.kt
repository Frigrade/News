package com.news.presentation.state

import com.news.domain.entity.NewsResponse

sealed class NewsState {
    object Loading : NewsState()

    data class Content(val data: NewsResponse) : NewsState()
    data class Error(val message: String) : NewsState()
}
