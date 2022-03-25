package com.news.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.news.domain.entity.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

    companion object {
        @Volatile
        private var instance: ArticleDatabase? = null                                    //создаем instance дб(синглтон)
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {         // При вызове ArticleDatabase(context) возвращается внутренний объект
                instance ?: createDatabase(context).also {
                    instance = it
                }                                                                       // instance; в случае, если объект null, синхронно создаем его
            }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()
    }
}