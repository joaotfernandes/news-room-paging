package com.github.joaotfernandes.newsroompaging

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.joaotfernandes.newsroompaging.repository.NewsRepository
import com.github.joaotfernandes.newsroompaging.service.model.Article
import kotlinx.android.synthetic.main.activity_news.articlesRecyclerView
import kotlinx.android.synthetic.main.activity_news.swipeRefreshLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsActivity : AppCompatActivity() {

    private val viewModel: NewsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val adapter = ArticlesAdapter(this::onArticleClicked)
        articlesRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        articlesRecyclerView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener { viewModel.refresh() }

        viewModel.articles.observe(this, Observer { adapter.submitList(it) })
        viewModel.refreshState.observe(this, Observer {
            swipeRefreshLayout.isRefreshing = it == NewsRepository.RefreshState.REFRESHING
        })
    }

    private fun onArticleClicked(article: Article) {
        Intent(Intent.ACTION_VIEW)
            .apply { data = Uri.parse(article.url) }
            .also { startActivity(it) }
    }
}
