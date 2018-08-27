package com.github.joaotfernandes.newsroompaging.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.joaotfernandes.newsroompaging.service.model.Article

const val DATABASE_NAME = "news.db"

@Database(entities = [Article::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NewsDb : RoomDatabase() {

    abstract fun articles(): ArticleDao
}
