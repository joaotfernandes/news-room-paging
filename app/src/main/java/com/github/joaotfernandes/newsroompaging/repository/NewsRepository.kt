package com.github.joaotfernandes.newsroompaging.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.github.joaotfernandes.newsroompaging.service.model.Article

interface NewsRepository {

    fun getArticles(keyword: String, language: String): LiveData<PagedList<Article>>
}
