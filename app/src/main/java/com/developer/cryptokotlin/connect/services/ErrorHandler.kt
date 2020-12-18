package com.developer.cryptokotlin.connect.services

import retrofit2.Response

class ErrorHandler {
    companion object {
        fun getError(response: Response<out Any>): String {
            val errorBody = response.errorBody()?.string()
            return errorBody ?: "something went wrong"
        }
    }
}