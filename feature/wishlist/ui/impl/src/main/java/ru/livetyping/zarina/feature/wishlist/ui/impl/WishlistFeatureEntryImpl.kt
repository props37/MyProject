package ru.livetyping.zarina.feature.wishlist.ui.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.wishlist.ui.WishlistFeatureEntry
import ru.livetyping.zarina.feature.wishlist.ui.WishlistNavActions
import ru.livetyping.zarina.feature.wishlist.ui.WishlistNavEntry
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.WishlistScreen

public class WishlistFeatureEntryImpl : WishlistFeatureEntry {
    override fun NavGraphBuilder.composable(actions: WishlistNavActions) {
        composable<WishlistNavEntry> {
            WishlistScreen(navActions = actions)
        }
    }
}
