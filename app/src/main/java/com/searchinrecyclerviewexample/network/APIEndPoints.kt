package com.searchinrecyclerviewexample.network

import com.searchinrecyclerviewexample.model.SearchImageRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIEndPoints {

    @GET("api/")
    suspend fun getData(
        @Query("key") apiKey: String,
        @Query("q") searchedTerm: String? = null,
        @Query("page") nextPageNo: Int? = 1
    ): Response<SearchImageRes>
}