package com.developer.cryptokotlin.connect.services

import androidx.lifecycle.LiveData
import com.developer.cryptokotlin.connect.models.AssetObject
import com.developer.cryptokotlin.connect.response.ResponseBody
import com.developer.cryptokotlin.connect.response.TimeSeriesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("v2/assets")
    suspend fun getAssets(@Query("page") page: Int): ResponseBody<List<AssetObject>>

    @GET("v1/assets/{assetKey}/metrics/price/time-series")
    suspend fun getTimeSeries(
        @Path("assetKey") assetKey: String?,
        @Query("beg") beg: String?,
        @Query("end") end: String?
    ): ResponseBody<TimeSeriesResponse>

}