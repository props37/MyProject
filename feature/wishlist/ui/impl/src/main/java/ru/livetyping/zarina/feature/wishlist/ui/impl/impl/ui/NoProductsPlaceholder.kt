package ru.livetyping.zarina.feature.wishlist.ui.impl.impl.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.uicompose.text.rememberAnnotatedStringWithLinks
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.wishlist.ui.impl.R

@Composable
internal fun NoProductsPlaceholder(
    onCategoryShortcutClicked: (Category.Id) -> Unit,
    onSearchClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 32.dp),
    ) {
        Text(
            text = stringResource(R.string.wishlist_is_empty).uppercase(),
            style = UiKitTheme2.typography.h2Regular,
            color = UiKitTheme2.colors.mainBlack,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(12.dp))

        val news = stringResource(R.string.wishlist_take_a_look_at_our_products_news)
        val search = stringResource(R.string.wishlist_take_a_look_at_our_products_search)
        val linkStyle = UiKitTheme2.typography.body.copy(
            color = UiKitTheme2.colors.hoverText,
            textDecoration = TextDecoration.Underline,
        )
        val substringToLink = remember {
            mapOf(
                news to LinkAnnotation.Clickable(
                    tag = news,
                    styles = TextLinkStyles(linkStyle.toSpanStyle()),
                    linkInteractionListener = { onCategoryShortcutClicked(Category.Id.WOMEN_NEW) }
                ),
                search to LinkAnnotation.Clickable(
                    tag = search,
                    styles = TextLinkStyles(linkStyle.toSpanStyle()),
                    linkInteractionListener = { onSearchClicked() }
                ),
            )
        }
        val text = rememberAnnotatedStringWithLinks(
            baseString = stringResource(R.string.wishlist_take_a_look_at_our_products),
            substringToLink = substringToLink,
        )

        Text(
            text = text.toUpperCase(),
            style = UiKitTheme2.typography.body,
            color = UiKitTheme2.colors.mainBlack,
            textAlign = TextAlign.Center,
        )
    }
}
