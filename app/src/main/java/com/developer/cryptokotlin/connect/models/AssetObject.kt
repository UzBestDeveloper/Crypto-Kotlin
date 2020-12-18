package com.developer.cryptokotlin.connect.models

import com.developer.cryptokotlin.connect.models.metrics.Metrics
import com.developer.cryptokotlin.connect.models.profile.Profile
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AssetObject(val id: String?,val symbol: String?,val name: String?,val slug: String?,var profile: Profile?,var metrics: Metrics?) : Serializable