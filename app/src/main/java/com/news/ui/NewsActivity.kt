package com.news.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.news.R
import com.news.data.db.ArticleDatabase
import com.news.presentation.NewsViewModel
import com.news.presentation.NewsViewModelProviderFactory
import com.news.data.repository.NewsRepository
import kotlinx.android.synthetic.main.activity_news.*


class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        setContentView(R.layout.activity_news)




        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }
}
