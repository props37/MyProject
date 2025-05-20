package ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.button.ZarinaBackIconButton
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBarDefaults
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.model.SubcategoryListState

@Composable
internal fun TopBar(
    categoryName: String?,
    subcategoryListState: SubcategoryListState,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(vertical = ZarinaTopBarDefaults.LargeVerticalPadding)) {
        Header(
            categoryName = categoryName,
            onBackClicked = onBackClicked,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: [Top] Add subcategory list
    }
}

@Composable
private fun Header(
    categoryName: String?,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        startContent = {
            ZarinaBackIconButton(
                onClick = onBackClicked,
                modifier = Modifier.padding(start = 2.dp),
            )
        },
        centerContent = {
            Crossfade(
                targetState = categoryName,
                label = "TopBar category name",
                modifier = Modifier.fillMaxWidth(),
            ) { categoryName ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    val textStyle = UiKitTheme2.typography.h3

                    if (categoryName != null) {
                        Text(
                            text = categoryName.uppercase(),
                            style = textStyle,
                            color = UiKitTheme2.colors.mainBlack,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    } else {
                        ZarinaTextSkeleton(
                            textStyle = textStyle,
                            modifier = Modifier.fillMaxWidth(0.6f),
                        )
                    }
                }
            }
        },
        contentPadding = PaddingValues(),
        modifier = modifier,
    )
}
