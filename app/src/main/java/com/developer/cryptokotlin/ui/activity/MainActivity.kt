package com.developer.cryptokotlin.ui.activity

import android.os.Bundle
import com.developer.cryptokotlin.MyApplication
import com.developer.cryptokotlin.R
import com.developer.cryptokotlin.connect.services.ApiService
import com.developer.cryptokotlin.di.components.FragmentComponent
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var apiService: ApiService;


    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.activityComponent().create().inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}