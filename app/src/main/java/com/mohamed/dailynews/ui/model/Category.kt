package com.mohamed.dailynews.ui.model

import androidx.annotation.StringRes
import com.mohamed.dailynews.R

data class Category(
    val id: String,
    val image: Int,
    @StringRes val titleResId: Int,
)

val categories = listOf(
    Category(id = "general", image = R.drawable.general_dark, titleResId = R.string.category_general),
    Category(id = "business", image = R.drawable.business_dark, titleResId = R.string.category_business),
    Category(id = "sports", image = R.drawable.sport_dark, titleResId = R.string.category_sports),
    Category(id = "technology", image = R.drawable.technology_dark, titleResId = R.string.category_technology),
    Category(id = "science", image = R.drawable.science_dark, titleResId = R.string.category_science),
    Category(id = "health", image = R.drawable.health_dark, titleResId = R.string.category_health),
    Category(id = "entertainment", image = R.drawable.entertainment_dark, titleResId = R.string.category_entertainment),
)
