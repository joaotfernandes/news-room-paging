package com.github.joaotfernandes.newsroompaging.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.joaotfernandes.newsroompaging.service.model.Article

@Dao
interface ArticleDao {

    @Query("select * from articles order by publishedAt desc")
    fun getArticles(): List<Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticles(articles: List<Article>)

    @Query("delete from articles")
    fun deleteAll()
}
