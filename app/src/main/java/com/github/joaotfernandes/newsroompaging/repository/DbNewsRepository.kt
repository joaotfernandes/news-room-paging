package com.github.joaotfernandes.newsroompaging.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.joaotfernandes.newsroompaging.db.NewsDb
import com.github.joaotfernandes.newsroompaging.paging.ArticlesBoundaryCallback
import com.github.joaotfernandes.newsroompaging.service.NewsService
import com.github.joaotfernandes.newsroompaging.service.model.Article

private const val PAGE_SIZE = 20

class DbNewsRepository(private val newsDb: NewsDb, private val newsService: NewsService) : NewsRepository {

    private val pagedListConfig = PagedList.Config.Builder()
        .setPageSize(PAGE_SIZE)
        .setEnablePlaceholders(false)
        .build()

    override fun getArticles(keyword: String, language: String): LiveData<PagedList<Article>> {
        return LivePagedListBuilder(newsDb.articles().getArticles(), pagedListConfig)
            .setBoundaryCallback(ArticlesBoundaryCallback(newsDb, newsService, keyword, language, pagedListConfig.pageSize))
            .build()
    }
}
