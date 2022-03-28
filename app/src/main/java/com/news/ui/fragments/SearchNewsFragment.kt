package com.news.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.news.R
import com.news.domain.entity.Article
import com.news.presentation.NewsAdapter
import com.news.presentation.state.NewsState
import com.news.ui.BaseFragment
import com.news.ui.NewsActivity
import com.news.util.Constants
import com.news.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.news.util.PaginationScrollListener
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchNewsFragment : BaseFragment(R.layout.fragment_search_news) {

    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        paginationScrollListener = PaginationScrollListener(viewModel::searchNews)
        setupRecyclerView()
        viewModel.searchNewsLiveData.observe(viewLifecycleOwner) { state ->
            renderContent(state)
        }

        var job: Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()

            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.clearResults()
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }
    }

    private fun renderContent(state: NewsState) {
        when (state) {
            is NewsState.Content -> {
                hideProgressBar(paginationProgressBar)
                newsAdapter.submitList(state.data.articles)
                updatePaginationListener(state)
            }

            is NewsState.Error -> {
                hideProgressBar(paginationProgressBar)
                Toast.makeText(context, "Не удалось связаться с сервером", Toast.LENGTH_SHORT)
                    .show()
            }

            is NewsState.Loading -> {
                showProgressBar(paginationProgressBar)
            }
        }
    }

    private fun updatePaginationListener(state: NewsState.Content) {
        val totalPages = state.data.totalResults / Constants.QUERY_PAGE_SIZE + 2
        paginationScrollListener.isLastPage = viewModel.searchNewsPage == totalPages
        if (paginationScrollListener.isLastPage) {
            rvSearchNews.setPadding(0, 0, 0, 0)
        }
    }

    private fun openArticle(article: Article) {
        val bundle = Bundle().apply {
            putSerializable("article", article)
        }

        findNavController().navigate(
            R.id.action_searchNewsFragment_to_articleFragment,
            bundle
        )
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(::openArticle)
        rvSearchNews.adapter = newsAdapter
        rvSearchNews.addOnScrollListener(paginationScrollListener)
    }
}
