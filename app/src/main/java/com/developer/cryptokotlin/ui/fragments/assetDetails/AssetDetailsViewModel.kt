package com.developer.cryptokotlin.ui.fragments.assetDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.cryptokotlin.connect.response.TimeSeriesResponse
import com.developer.cryptokotlin.connect.services.ApiService
import kotlinx.coroutines.launch

class AssetDetailsViewModel(
    apiService: ApiService,
    symbol: String,
    end: String,
    beg: String
) : ViewModel() {

    private var _data = MutableLiveData<TimeSeriesResponse>()
    val data: LiveData<TimeSeriesResponse>
        get() = _data

    init {
        viewModelScope.launch {
            _data.value = apiService.getTimeSeries(symbol,beg,end).data
        }
    }
}