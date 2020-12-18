package com.developer.cryptokotlin.utils.assetListScrollListener

sealed class ProgressStatus {
    object Loading : ProgressStatus()

    object Success : ProgressStatus()

    object Error : ProgressStatus()
}

sealed class DataResult<T> {
    class DataSuccess<T>(val data: T) : DataResult<T>()
    class DataError<T> : DataResult<T>()
}