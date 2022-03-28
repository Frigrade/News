package com.news.ui

import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.news.presentation.NewsViewModel
import com.news.util.PaginationScrollListener

open class BaseFragment(resId: Int) : Fragment(resId) {
    lateinit var viewModel: NewsViewModel
    lateinit var paginationScrollListener: PaginationScrollListener

    fun hideProgressBar(paginationProgressBar: ProgressBar) {
        paginationProgressBar.visibility = View.INVISIBLE
        paginationScrollListener.isLoading = false
    }

    fun showProgressBar(paginationProgressBar: ProgressBar) {
        paginationProgressBar.visibility = View.VISIBLE
        paginationScrollListener.isLoading = true
    }
}