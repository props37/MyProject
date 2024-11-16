package ru.livetyping.zarina.feature.wishlist.ui.impl

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.wishlist.ui.WishlistFeatureEntry
import ru.livetyping.zarina.feature.wishlist.ui.WishlistNavActions
import ru.livetyping.zarina.feature.wishlist.ui.WishlistNavEntry
import ru.livetyping.zarina.feature.wishlist.ui.impl.impl.WishlistScreen
import kotlin.reflect.KClass

public class WishlistFeatureEntryImpl : WishlistFeatureEntry {
    override fun NavGraphBuilder.composable(
        actions: WishlistNavActions,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
        sizeTransform: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)?
    ) {
        composable<WishlistNavEntry> {
            WishlistScreen(navActions = actions)
        }
    }

    override fun getNavEntry(params: Unit): WishlistNavEntry = WishlistNavEntry

    override fun getNavEntryClass(): KClass<WishlistNavEntry> = WishlistNavEntry::class
}
