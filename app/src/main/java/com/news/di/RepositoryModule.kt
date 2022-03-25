package com.news.di

import com.news.data.api.NewsAPI
import com.news.data.db.ArticleDatabase
import com.news.data.repository.NewsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun getNewsRepository(db: ArticleDatabase, newsAPI: NewsAPI): NewsRepository =
        NewsRepository(db, newsAPI)
}