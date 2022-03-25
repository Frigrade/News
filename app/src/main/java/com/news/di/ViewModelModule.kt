package com.news.di

import com.news.data.repository.NewsRepository
import com.news.presentation.NewsViewModelProviderFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Singleton
    @Provides
    fun provideViewModelFactory(newsRepository: NewsRepository): NewsViewModelProviderFactory =
        NewsViewModelProviderFactory(newsRepository)
}