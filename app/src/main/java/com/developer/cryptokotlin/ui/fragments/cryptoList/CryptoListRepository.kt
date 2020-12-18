package com.developer.cryptokotlin.ui.fragments.cryptoList

import com.developer.cryptokotlin.utils.assetListScrollListener.DataResult
import com.developer.cryptokotlin.connect.models.AssetObject
import com.developer.cryptokotlin.connect.services.ApiService
import com.developer.cryptokotlin.utils.AssetContract

class CryptoListRepository (private val apiService: ApiService) : AssetContract.Repository {

    override suspend fun getAssets( pageNumber: Int): DataResult<List<AssetObject>> {
        val response = apiService.getAssets(pageNumber)
        return if (null != response.data) {
            DataResult.DataSuccess(response.data!!)
        } else {
            DataResult.DataError()
        }
    }
}