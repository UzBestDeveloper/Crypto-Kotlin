package com.developer.cryptokotlin

import android.app.Application
import com.developer.cryptokotlin.di.components.ApplicationComponent
import com.developer.cryptokotlin.di.components.DaggerApplicationComponent

open class MyApplication : Application() {

    val appComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(applicationContext)
    }
}
