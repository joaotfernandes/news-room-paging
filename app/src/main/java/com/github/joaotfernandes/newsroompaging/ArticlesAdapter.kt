package com.github.joaotfernandes.newsroompaging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.joaotfernandes.newsroompaging.databinding.ItemArticleBinding
import com.github.joaotfernandes.newsroompaging.service.model.Article

class ArticlesAdapter(private val articleClickListener: (Article) -> Unit)
    : PagedListAdapter<Article, ArticlesAdapter.ArticleViewHolder>(ArticleDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ArticleViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ArticleViewHolder(private val binder: ItemArticleBinding) : RecyclerView.ViewHolder(binder.root) {

        fun bind(article: Article) {
            binder.article = article
            binder.root.setOnClickListener { articleClickListener(article) }
            binder.executePendingBindings()
        }
    }

    private object ArticleDiff : DiffUtil.ItemCallback<Article>() {

        override fun areItemsTheSame(oldItem: Article, newItem: Article) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            // In this case, if items are the same then content will always be the same
            return true
        }

    }
}