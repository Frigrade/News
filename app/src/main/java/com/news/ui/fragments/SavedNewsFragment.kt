package com.news.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.news.R
import com.google.android.material.snackbar.Snackbar
import com.news.domain.entity.Article
import com.news.presentation.NewsAdapter
import com.news.ui.BaseFragment
import com.news.ui.NewsActivity
import kotlinx.android.synthetic.main.fragment_saved_news.*


class SavedNewsFragment : BaseFragment(R.layout.fragment_saved_news){

    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view, "Статья успешно удалена", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchCallback).apply {
            attachToRecyclerView(rvSavedNews)
        }

        viewModel.savedNews().observe(viewLifecycleOwner) { articles ->
            newsAdapter.submitList(articles)
        }
    }

    private fun openArticle(article: Article) {
        val bundle = Bundle().apply {
            putSerializable("article", article)
        }

        findNavController().navigate(
            R.id.action_savedNewsFragment_to_articleFragment,
            bundle
        )
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(::openArticle)
        rvSavedNews.adapter = newsAdapter
    }
}

