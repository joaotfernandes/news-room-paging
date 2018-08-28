package com.github.joaotfernandes.newsroompaging

import androidx.lifecycle.ViewModel
import com.github.joaotfernandes.newsroompaging.repository.NewsRepository

class NewsViewModel(newsRepository: NewsRepository, keyword: String, language: String) : ViewModel() {

    val articles = newsRepository.getArticles(keyword, language)
}

