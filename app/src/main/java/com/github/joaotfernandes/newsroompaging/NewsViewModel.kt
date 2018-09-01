package com.github.joaotfernandes.newsroompaging

import androidx.lifecycle.ViewModel
import com.github.joaotfernandes.newsroompaging.repository.NewsRepository

class NewsViewModel(private val newsRepository: NewsRepository, private val keyword: String, private val language: String) : ViewModel() {

    val articles = newsRepository.getArticles(keyword, language)

    val refreshState = newsRepository.onRefreshStateChange()

    fun refresh() = newsRepository.requestRefresh(keyword, language)
}

