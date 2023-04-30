package sportssisal.matchgame.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import sportssisal.matchgame.models.NewsResponse
import sportssisal.matchgame.utils.Constants.Companion.API_KEY

interface NewsService {
    @GET("/v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country") country: String = "us",
        @Query("category") category: String = "sports",
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY
    ) : Response<NewsResponse>


}