package com.developer.cryptokotlin.ui.fragments.cryptoList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.developer.cryptokotlin.utils.AssetContract

class CryptoListViewModelFactory(private val repository: AssetContract.Repository) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CryptoListViewModel::class.java)) {
            return CryptoListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}