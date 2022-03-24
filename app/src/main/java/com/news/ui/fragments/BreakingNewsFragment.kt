package com.news.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.news.R
import com.news.domain.entity.Article
import com.news.presentation.NewsAdapter
import com.news.presentation.state.NewsState
import com.news.ui.BaseFragment
import com.news.ui.NewsActivity
import com.news.util.Constants.Companion.QUERY_PAGE_SIZE
import com.news.util.PaginationScrollListener
import kotlinx.android.synthetic.main.fragment_breaking_news.*


class BreakingNewsFragment : BaseFragment(R.layout.fragment_breaking_news) {

    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        viewModel.breakingNewsLiveData.observe(viewLifecycleOwner) { state ->
            renderContent(state)
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

    private fun openArticle(article: Article) {
        val bundle = Bundle().apply {
            putSerializable("article", article)
        }

        findNavController().navigate(
            R.id.action_breakingNewsFragment_to_articleFragment,
            bundle
        )
    }

    private fun updatePaginationListener(state: NewsState.Content) {
        val totalPages = state.data.totalResults / QUERY_PAGE_SIZE + 2
        paginationScrollListener.isLastPage = viewModel.breakingNewsPage == totalPages
        if (paginationScrollListener.isLastPage) {
           rvBreakingNews.setPadding(0, 0, 0, 0)
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(::openArticle)
        rvBreakingNews.adapter = newsAdapter
        rvBreakingNews.addOnScrollListener(PaginationScrollListener(viewModel::getBreakingNews))
    }
}
