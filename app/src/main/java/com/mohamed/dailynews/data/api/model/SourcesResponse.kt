package com.mohamed.dailynews.data.api.model

import com.google.gson.annotations.SerializedName

data class SourcesResponse(

    @field:SerializedName("sources")
    val sources: List<SourceDto>? = null,

    @field:SerializedName("status")
    val status: String? = null
)