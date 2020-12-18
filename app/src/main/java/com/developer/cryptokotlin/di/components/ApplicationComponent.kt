package com.developer.cryptokotlin.di.components

import com.developer.cryptokotlin.di.modules.NetworkModule
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppSubComponents::class, NetworkModule::class])
interface ApplicationComponent {

    fun activityComponent(): ActivityComponent.Factory

    fun fragmentComponent(): FragmentComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

}