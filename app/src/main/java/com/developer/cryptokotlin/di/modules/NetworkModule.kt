package com.developer.cryptokotlin.di.modules

import com.developer.cryptokotlin.BuildConfig
import com.developer.cryptokotlin.connect.services.ApiService
import com.developer.cryptokotlin.utils.Constants.Companion.BASE_URL
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return interceptor
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL).build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClientDefault(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val okBuilder = OkHttpClient.Builder()
        okBuilder.addInterceptor(httpLoggingInterceptor)
        okBuilder.addInterceptor { chain ->
            val request = chain.request()
            val builder = request.newBuilder().addHeader("x-messari-api-key","501fa3fb-9e2b-49d4-90c7-a2e1f5e5065e")
            chain.proceed(builder.build())
        }

        okBuilder.connectTimeout(20, TimeUnit.SECONDS)
        okBuilder.readTimeout(20, TimeUnit.SECONDS)
        okBuilder.writeTimeout(20, TimeUnit.SECONDS)

        return okBuilder.build()

    }
}