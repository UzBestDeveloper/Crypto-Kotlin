package com.developer.cryptokotlin.utils

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.developer.cryptokotlin.utils.assetListScrollListener.DataResult
import com.developer.cryptokotlin.utils.assetListScrollListener.ProgressStatus
import com.developer.cryptokotlin.connect.models.AssetObject

interface AssetContract {
    interface ViewModel{
       fun getAssets(): LiveData<PagedList<AssetObject>>
       fun getProgressStatus(): LiveData<ProgressStatus>
    }

    interface Repository{
        suspend fun getAssets(pageNumber:Int): DataResult<List<AssetObject>>
    }
}