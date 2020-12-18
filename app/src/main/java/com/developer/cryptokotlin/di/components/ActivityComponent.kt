package com.developer.cryptokotlin.di.components

import com.akfa.catalog.di.scopes.ActivityScope
import com.developer.cryptokotlin.ui.activity.MainActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface ActivityComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(): ActivityComponent
    }

    fun inject(mainActivity: MainActivity?)

}