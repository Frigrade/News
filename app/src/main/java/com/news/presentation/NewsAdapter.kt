package com.news.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.news.R
import com.news.domain.entity.Article
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter(
    private val openArticle: (Article) -> Unit
) : ListAdapter<Article, NewsAdapter.ArticleViewHolder>(DiffUtilCallback()) {

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article_preview,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = currentList[position]

        holder.itemView.apply {
            Glide.with(this)
                .load(article.urlToImage)
                .into(ivArticleImage)

            tvTitle.text = article.title
            tvDescription.text = article.description
            tvPublishedAt.text = article.publishedAt

            setOnClickListener {
                openArticle(article)
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

}

class DiffUtilCallback: DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}
