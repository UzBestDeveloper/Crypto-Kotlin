package com.developer.cryptokotlin.ui.fragments.cryptoList

import androidx.lifecycle.*
import androidx.paging.*
import com.developer.cryptokotlin.utils.assetListScrollListener.AssetDataSourceFactory
import com.developer.cryptokotlin.utils.assetListScrollListener.AssetsDataSource
import com.developer.cryptokotlin.utils.assetListScrollListener.ProgressStatus
import com.developer.cryptokotlin.connect.models.AssetObject
import com.developer.cryptokotlin.utils.AssetContract

class CryptoListViewModel(repository: AssetContract.Repository) : ViewModel(),AssetContract.ViewModel {

    private val assetDataSourceFactory: AssetDataSourceFactory = AssetDataSourceFactory(
        repository,
        viewModelScope
    )
    private val progressLoadStatus: LiveData<ProgressStatus>
    private val assets: LiveData<PagedList<AssetObject>>

    init {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(20)
            .setPageSize(20)
            .build()

        assets = LivePagedListBuilder(assetDataSourceFactory, pagedListConfig)
            .build()

        progressLoadStatus = Transformations.switchMap(assetDataSourceFactory.liveData, AssetsDataSource::getProgressLiveStatus)
    }

    override fun getAssets() =  assets

    override fun getProgressStatus() = progressLoadStatus
}