package com.github.joaotfernandes.newsroompaging.service.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

data class NewsResponse(val status: String, val totalResults: Int, val articles: List<Article>)

@Entity(tableName = "articles")
data class Article(
    val title: String,
    val description: String? = "",
    val publishedAt: Date,
    @PrimaryKey val url: String,
    @Embedded(prefix = "source_") val source: Source
)

data class Source(val name: String)
