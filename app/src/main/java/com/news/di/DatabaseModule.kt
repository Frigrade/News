package com.news.di

import android.content.Context
import androidx.room.Room
import com.news.data.db.ArticleDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun getNewsRepository(context: Context): ArticleDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            ArticleDatabase::class.java,
            "article_db.db"
        ).build()
}