package com.mohamed.dailynews.ui.screens.home.composables.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamed.dailynews.R
import com.mohamed.dailynews.ui.model.Category
import com.mohamed.dailynews.ui.model.categories
import com.mohamed.dailynews.ui.theme.Black
import com.mohamed.dailynews.ui.theme.DailyNewsShapes
import com.mohamed.dailynews.ui.theme.White

@Composable
fun CategoriesTab(onCategoryClick: (Category) -> Unit) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        item {
            Text(
                text = stringResource(id = R.string.greeting_headline),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 24.sp,
                    lineHeight = 30.sp
                ),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
            )
        }
        itemsIndexed(categories, key = { _, category -> category.id }) { index, category ->
            CategoryItem(category = category, index = index) {
                onCategoryClick(category)
            }
        }
    }
}

@Composable
fun CategoryItem(category: Category, index: Int, onClick: () -> Unit) {
    val isEven = index % 2 == 0
    val iconModifier = Modifier
        .clip(CircleShape)
        .size(36.dp)
        .background(White)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(180.dp)
            .clip(DailyNewsShapes.large)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(category.image),
            contentDescription = stringResource(id = category.titleResId),
            contentScale = ContentScale.Crop,
            alignment = if (isEven) Alignment.CenterStart else Alignment.CenterEnd,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = if (isEven) AbsoluteAlignment.Right else AbsoluteAlignment.Left
        ) {
            Text(
                text = stringResource(id = category.titleResId),
                style = MaterialTheme.typography.titleMedium,
                color = White
            )

            Card(
                shape = DailyNewsShapes.extraLarge,
                colors = CardDefaults.cardColors(
                    containerColor = White.copy(alpha = 0.55f),
                    contentColor = Black
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.view_all),
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                        color = Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                        contentDescription = stringResource(id = R.string.view_all),
                        tint = Black,
                        modifier = iconModifier
                    )
                }
            }
        }
    }
}
