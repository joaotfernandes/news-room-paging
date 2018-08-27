package com.github.joaotfernandes.newsroompaging.service

import com.github.joaotfernandes.newsroompaging.service.model.NewsResponse
import java.util.Date

private const val DEFAULT_HEADLINES_PER_PAGE = 20

interface NewsService {

    /**
     * Provides articles that have a match the given [keywords].
     *
     * Articles are sorted by the earliest date published first.
     *
     * @param keywords keywords to search for
     * @param language the 2-letter ISO-639-1 code of the language the articles will be returned for
     * @param from a date for the oldest article returned
     * @param to a date for the newest article returned
     * @param pageSize the number of results to return. 20 is the default value, 100 is the maximum
     */
    fun getArticles(
        keywords: String,
        language: String,
        from: Date?,
        to: Date?,
        pageSize: Int = DEFAULT_HEADLINES_PER_PAGE
    ): SimpleCall<NewsResponse>
}
