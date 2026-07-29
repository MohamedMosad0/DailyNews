package com.mohamed.dailynews.data.api

import com.mohamed.dailynews.data.api.model.ArticlesResponse
import com.mohamed.dailynews.data.api.model.SourcesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {
    @GET("/v2/top-headlines/sources")
    suspend fun getSources(
        @Query("apiKey") apiKey: String = ApiManager.API_KEY,
        @Query("category") category: String,
    ): SourcesResponse

    @GET("/v2/everything")
    suspend fun getArticles(
        @Query("apiKey") apiKey: String = ApiManager.API_KEY,
        @Query("sources") source: String,
    ): ArticlesResponse

    @GET("/v2/everything")
    suspend fun searchArticles(
        @Query("apiKey") apiKey: String = ApiManager.API_KEY,
        @Query("q") query: String,
    ): ArticlesResponse
}