package com.developer.cryptokotlin.utils.assetListScrollListener

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.developer.cryptokotlin.connect.models.AssetObject
import com.developer.cryptokotlin.utils.AssetContract
import kotlinx.coroutines.CoroutineScope

class AssetDataSourceFactory(
    private val repository: AssetContract.Repository,
    private val scope: CoroutineScope
) : DataSource.Factory<Int, AssetObject>() {

    val liveData = MutableLiveData<AssetsDataSource>()
    lateinit var issuesDataSource: AssetsDataSource

    override fun create(): DataSource<Int, AssetObject> {
        issuesDataSource = AssetsDataSource(repository, scope)
        liveData.postValue(issuesDataSource)
        return issuesDataSource
    }
}