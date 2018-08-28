package com.github.joaotfernandes.newsroompaging

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.joaotfernandes.newsroompaging.db.NewsDb
import com.github.joaotfernandes.newsroompaging.paging.ArticlesBoundaryCallback
import com.github.joaotfernandes.newsroompaging.service.NewsService
import com.github.joaotfernandes.newsroompaging.service.model.Article

private const val PAGE_SIZE = 20

class NewsViewModel(newsDb: NewsDb, newsService: NewsService, keyword: String, language: String) : ViewModel() {

    val articles: LiveData<PagedList<Article>>

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()

        articles = LivePagedListBuilder(newsDb.articles().getArticles(keyword), config)
            .setBoundaryCallback(ArticlesBoundaryCallback(newsDb, newsService, keyword, language, PAGE_SIZE))
            .build()
    }
}

