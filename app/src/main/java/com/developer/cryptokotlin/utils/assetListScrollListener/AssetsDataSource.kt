package com.developer.cryptokotlin.utils.assetListScrollListener

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.developer.cryptokotlin.connect.models.AssetObject
import com.developer.cryptokotlin.utils.AssetContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AssetsDataSource(
    private val repository: AssetContract.Repository,
    private val scope: CoroutineScope, ) : PageKeyedDataSource<Int, AssetObject>() {
    private val progressLiveStatus = MutableLiveData<ProgressStatus>()

    fun getProgressLiveStatus() = progressLiveStatus

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, AssetObject>) {
        scope.launch {
            progressLiveStatus.postValue(ProgressStatus.Loading)
            when (val dataResult = repository.getAssets(1)) {
                is DataResult.DataSuccess -> {
                    progressLiveStatus.postValue(ProgressStatus.Success)
                    callback.onResult(dataResult.data, null, 2)
                }
                is DataResult.DataError -> progressLiveStatus.postValue(ProgressStatus.Error)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, AssetObject>) {
        scope.launch {
            progressLiveStatus.postValue(ProgressStatus.Loading)
            when (val dataResult = repository.getAssets(params.key)) {
                is DataResult.DataSuccess -> {
                    progressLiveStatus.postValue(ProgressStatus.Success)
                    callback.onResult(dataResult.data, params.key + 1)
                }
                is DataResult.DataError -> progressLiveStatus.postValue(ProgressStatus.Error)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, AssetObject>) {

    }
}