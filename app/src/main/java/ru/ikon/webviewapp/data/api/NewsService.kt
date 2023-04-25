package ru.ikon.webviewapp.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.ikon.webviewapp.models.NewsResponse
import ru.ikon.webviewapp.utils.Constants.Companion.API_KEY

interface NewsService {
    @GET("/v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country") country: String = "ru",
        @Query("category") category: String = "sports",
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY
    ) : Response<NewsResponse>
}