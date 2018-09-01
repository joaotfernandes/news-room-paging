package com.github.joaotfernandes.newsroompaging.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.joaotfernandes.newsroompaging.service.model.Article

@Dao
interface ArticleDao {

    @Query("select * from articles order by publishedAt desc")
    fun getArticles(): DataSource.Factory<Int, Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticles(articles: List<Article>)

    @Query("delete from articles")
    fun deleteAll()
}
