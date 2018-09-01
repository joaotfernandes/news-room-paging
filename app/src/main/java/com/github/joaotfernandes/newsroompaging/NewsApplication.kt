package com.github.joaotfernandes.newsroompaging

import android.app.Application
import androidx.room.Room
import com.github.joaotfernandes.newsroompaging.db.DATABASE_NAME
import com.github.joaotfernandes.newsroompaging.db.NewsDb
import com.github.joaotfernandes.newsroompaging.repository.DbNewsRepository
import com.github.joaotfernandes.newsroompaging.repository.NewsRepository
import com.github.joaotfernandes.newsroompaging.service.NewsService
import com.github.joaotfernandes.newsroompaging.service.RetrofitNewsService
import org.koin.android.ext.android.startKoin
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import java.util.Locale
import java.util.concurrent.Executors

class NewsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val module = module {
            single { RetrofitNewsService(BuildConfig.NEWS_API_KEY) as NewsService }

            single { DbNewsRepository(get(), get(), Executors.newSingleThreadExecutor()) as NewsRepository }

            single {
                Room.databaseBuilder(this@NewsApplication, NewsDb::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
            }

            viewModel { NewsViewModel(get(), getProperty("article_keywords"), Locale.getDefault().language) }
        }

        startKoin(this, listOf(module))
    }
}
