package com.developer.cryptokotlin.ui.fragments.assetDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.developer.cryptokotlin.connect.services.ApiService

class AssetDetailsViewModelFactory(
    private val apiService: ApiService,
    private val symbol: String,
    private val end: String,
    private val beg: String
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AssetDetailsViewModel::class.java)) {
            return AssetDetailsViewModel(apiService,symbol,end,beg) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}