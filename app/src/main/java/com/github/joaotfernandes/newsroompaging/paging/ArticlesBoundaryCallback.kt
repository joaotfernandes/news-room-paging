package com.github.joaotfernandes.newsroompaging.paging

import android.util.Log
import androidx.paging.PagedList
import com.github.joaotfernandes.newsroompaging.db.NewsDb
import com.github.joaotfernandes.newsroompaging.service.NewsService
import com.github.joaotfernandes.newsroompaging.service.model.Article
import java.util.Date
import java.util.concurrent.Executors

class ArticlesBoundaryCallback(
    private val newsDb: NewsDb,
    private val newsService: NewsService,
    private val keyword: String,
    private val language: String,
    private val pageSize: Int
) : PagedList.BoundaryCallback<Article>() {

    private val ioExecutor = Executors.newSingleThreadExecutor()
    private var running = false

    override fun onZeroItemsLoaded() = loadArticles(null)

    override fun onItemAtEndLoaded(itemAtEnd: Article) = loadArticles(itemAtEnd.publishedAt)

    private fun loadArticles(to: Date?) {
        if (!running) {
            running = true
            newsService.getArticles(keyword, language, null, to, pageSize)
                .doOnError {
                    Log.e("ArticlesPagedDataSource", "Failed to load articles", it)
                    running = false
                }
                .doOnSuccess {
                    if (it.isSuccessful) {
                        it.body()?.let {
                            ioExecutor.submit {
                                newsDb.articles().insertArticles(it.articles)
                                running = false
                            }
                        }
                    }
                }.run()
        }
    }
}
