package com.github.joaotfernandes.newsroompaging.service

import com.github.joaotfernandes.newsroompaging.service.model.NewsResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private const val NEWS_API_BASE_URL = "https://newsapi.org/v2/"
private const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.ss"
private const val DATE_TIME_ZONE = "GMT"

class RetrofitNewsService(private val apiKey: String) : NewsService {

    private val api: NewsApi
    private val dateFormatter: DateFormat

    init {
        api = Retrofit.Builder()
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
                        .add(KotlinJsonAdapterFactory())
                        .build()
                )
            )
            .baseUrl(NEWS_API_BASE_URL)
            .build()
            .create(NewsApi::class.java)

        dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.US)
        dateFormatter.timeZone = TimeZone.getTimeZone(DATE_TIME_ZONE)
    }

    override fun getArticles(keywords: String, language: String, from: Date?, to: Date?, pageSize: Int) =
        api.getArticles(
            keywords,
            language,
            from?.let { dateFormatter.format(from) },
            to?.let { dateFormatter.format(to) },
            pageSize,
            apiKey
        ).toSimpleCall()

    interface NewsApi {

        @GET("everything?sortBy=publishedAt")
        fun getArticles(
            @Query("q") keyword: String,
            @Query("language") language: String,
            @Query("from") from: String?,
            @Query("to") to: String?,
            @Query("pageSize") pageSize: Int,
            @Header("X-Api-Key") apiKey: String
        ): Call<NewsResponse>
    }
}
