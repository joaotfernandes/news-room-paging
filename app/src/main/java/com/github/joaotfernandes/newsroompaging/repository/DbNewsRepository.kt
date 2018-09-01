package com.github.joaotfernandes.newsroompaging.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.joaotfernandes.newsroompaging.db.NewsDb
import com.github.joaotfernandes.newsroompaging.paging.ArticlesBoundaryCallback
import com.github.joaotfernandes.newsroompaging.service.NewsService
import com.github.joaotfernandes.newsroompaging.service.model.Article
import com.github.joaotfernandes.newsroompaging.service.model.NewsResponse
import retrofit2.Response
import java.util.concurrent.ExecutorService

private const val PAGE_SIZE = 20

class DbNewsRepository(
    private val newsDb: NewsDb,
    private val newsService: NewsService,
    private val ioExecutor: ExecutorService
) : NewsRepository {

    private val refreshState = MutableLiveData<NewsRepository.RefreshState>()

    private val pagedListConfig = PagedList.Config.Builder()
        .setPageSize(PAGE_SIZE)
        .setEnablePlaceholders(false)
        .build()

    override fun getArticles(keyword: String, language: String): LiveData<PagedList<Article>> {
        return LivePagedListBuilder(newsDb.articles().getArticles(), pagedListConfig)
            .setBoundaryCallback(ArticlesBoundaryCallback(
                newsService,
                keyword,
                language,
                PAGE_SIZE,
                this::onArticlesLoaded,
                this::onArticlesLoadFailed)
            ).build()
    }

    override fun onRefreshStateChange() = refreshState

    override fun requestRefresh(keyword: String, language: String) {
        if (refreshState != NewsRepository.RefreshState.REFRESHING) {
            refreshState.value = NewsRepository.RefreshState.REFRESHING

            newsService.getArticles(keyword, language, null, null, pageSize = PAGE_SIZE)
                .doOnError { refreshState.value = NewsRepository.RefreshState.COMPLETE }
                .doOnSuccess {
                    if (it.isSuccessful) {
                        it.body()?.let { newsResponse ->
                            ioExecutor.submit {
                                newsDb.runInTransaction {
                                    val articleDao = newsDb.articles()
                                    articleDao.deleteAll()
                                    articleDao.insertArticles(newsResponse.articles)
                                }
                                refreshState.postValue(NewsRepository.RefreshState.COMPLETE)
                            }
                        }
                    }
                }.run()
        }
    }

    private fun onArticlesLoaded(response: Response<NewsResponse>) {
        if (response.isSuccessful) {
            response.body()?.let {
                ioExecutor.submit {
                    newsDb.articles().insertArticles(it.articles)
                }
            }
        }
    }

    private fun onArticlesLoadFailed(throwable: Throwable) {
        Log.e("DbNewsRepository", "Failed to load articles", throwable)
    }
}
