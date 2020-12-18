package com.developer.cryptokotlin.di.components

import com.akfa.catalog.di.scopes.ActivityScope
import com.developer.cryptokotlin.ui.fragments.assetDetails.AssetDetailsFragment
import com.developer.cryptokotlin.ui.fragments.cryptoList.CryptoListFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface FragmentComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(): FragmentComponent
    }


    fun inject(cyptoListFragment: CryptoListFragment?)

    fun inject(assetDetailsFragment: AssetDetailsFragment?)

}