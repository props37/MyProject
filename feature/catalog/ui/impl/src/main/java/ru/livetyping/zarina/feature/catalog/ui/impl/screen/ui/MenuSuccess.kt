package ru.livetyping.zarina.feature.catalog.ui.impl.screen.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.feedback.FeedbackWidget
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.feature.catalog.ui.impl.screen.model.CatalogEvent
import ru.livetyping.zarina.feature.catalog.ui.impl.screen.model.MenuItem
import ru.livetyping.zarina.feature.catalog.ui.impl.screen.model.MenuState

@Composable
internal fun MenuSuccess(
    state: MenuState.Success,
    onCatalogEvent: (CatalogEvent) -> Unit,
    bottomPaddingProvider: @Composable () -> Dp,
    modifier: Modifier = Modifier,
) {
    val bottomPadding = bottomPaddingProvider() + ZarinaScrollableDefaults.ScrollableBottomPadding

    LazyColumn(
        contentPadding = PaddingValues(bottom = bottomPadding),
        modifier = modifier,
    ) {
        items(
            items = state.items,
            key = { it.id },
            contentType = {
                when (it) {
                    is MenuItem.Basic -> ItemContentType.BasicItem
                    MenuItem.FeedbackWidget -> ItemContentType.FeedbackWidget
                    is MenuItem.City -> ItemContentType.City
                    MenuItem.SupportPhoneNumber -> ItemContentType.SupportPhoneNumber
                    MenuItem.SupportEmailAddress -> ItemContentType.SupportEmailAddress
                    is MenuItem.Spacer -> ItemContentType.Spacer
                }
            },
        ) { item ->
            val animateItemModifier = Modifier.animateZarinaItem(this)

            when (item) {
                is MenuItem.Basic -> {
                    MenuItemBasic(
                        item = item,
                        onClick = { onCatalogEvent(CatalogEvent.MenuItemClicked(it)) },
                        modifier = animateItemModifier,
                    )
                }

                MenuItem.FeedbackWidget -> {
                    FeedbackWidget(
                        onClick = { onCatalogEvent(CatalogEvent.LeaveFeedbackClicked) },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .then(animateItemModifier),
                    )
                }

                is MenuItem.City -> {
                    MenuItemCity(
                        item = item,
                        onChangeClicked = { onCatalogEvent(CatalogEvent.ChangeCityClicked) },
                        modifier = animateItemModifier,
                    )
                }

                MenuItem.SupportPhoneNumber -> {
                    MenuItemSupportPhoneNumber(
                        modifier = animateItemModifier,
                    )
                }

                MenuItem.SupportEmailAddress -> {
                    MenuItemSupportEmailAddress(
                        modifier = animateItemModifier,
                    )
                }

                is MenuItem.Spacer -> {
                    val height = when (item.size) {
                        MenuItem.Spacer.Size.SMALL -> MenuSpacerHeightSmall
                        MenuItem.Spacer.Size.MEDIUM -> MenuSpacerHeightMedium
                    }

                    Spacer(
                        modifier = Modifier
                            .height(height)
                            .then(animateItemModifier),
                    )
                }
            }
        }
    }
}

private enum class ItemContentType {
    BasicItem,
    FeedbackWidget,
    City,
    SupportPhoneNumber,
    SupportEmailAddress,
    Spacer
}

internal val MenuSpacerHeightMedium: Dp get() = 20.dp
internal val MenuSpacerHeightSmall: Dp get() = 8.dp
