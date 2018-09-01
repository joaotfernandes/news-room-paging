package com.github.joaotfernandes.newsroompaging.paging

import android.util.Log
import androidx.paging.PagedList
import com.github.joaotfernandes.newsroompaging.service.model.Article
import java.util.concurrent.Executors

import com.github.joaotfernandes.newsroompaging.db.NewsDb

import com.github.joaotfernandes.newsroompaging.service.NewsService
import com.github.joaotfernandes.newsroompaging.service.model.NewsResponse
import retrofit2.Response
import java.util.Date

class ArticlesBoundaryCallback(
    private val newsService: NewsService,
    private val keyword: String,
    private val language: String,
    private val pageSize: Int,
    private val successCallback: (Response<NewsResponse>) -> Unit,
    private val errorCallback: (Throwable) -> Unit
) : PagedList.BoundaryCallback<Article>() {

    private var running = false

    override fun onZeroItemsLoaded() = loadArticles(null)

    override fun onItemAtEndLoaded(itemAtEnd: Article) = loadArticles(itemAtEnd.publishedAt)

    private fun loadArticles(to: Date?) {
        if (!running) {
            running = true
            newsService.getArticles(keyword, language, null, to, pageSize)
                .doOnError {
                    errorCallback(it)
                    running = false
                }
                .doOnSuccess {
                    successCallback(it)
                    running = false
                }.run()
        }
    }
}
