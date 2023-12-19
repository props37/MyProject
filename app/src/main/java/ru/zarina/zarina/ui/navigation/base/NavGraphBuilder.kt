package ru.zarina.zarina.ui.navigation.base

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navigation
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet

/**
 * Adds the [Destination] to the [NavGraphBuilder].
 *
 * @param destination destination to add.
 * @param content composable for the destination.
 */
fun NavGraphBuilder.composableDestination(
    destination: Destination<*>,
    enterTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
    exitTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
    popEnterTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = enterTransition,
    popExitTransition: (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = exitTransition,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable(
        route = destination.routeSchema,
        arguments = destination.arguments,
        deepLinks = destination.deepLinks,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        content = content,
    )
}

/**
 * Adds the [Destination] to the [NavGraphBuilder] that will be hosted within a
 * [androidx.compose.ui.window.Dialog].
 *
 * This is suitable only when this dialog represents
 * a separate screen in your app that needs its own lifecycle and saved state, independent
 * of any other destination in your navigation graph. For use cases such as `AlertDialog`,
 * you should use those APIs directly in the [composableDestination] destination that
 * wants to show that dialog.
 *
 * @param destination destination to add.
 * @param dialogProperties properties that should be passed to [androidx.compose.ui.window.Dialog].
 * @param content composable content for the destination that will be hosted within the Dialog.
 */
@Suppress("Unused")
fun NavGraphBuilder.dialogDestination(
    destination: Destination<*>,
    dialogProperties: DialogProperties = DialogProperties(),
    content: @Composable (NavBackStackEntry) -> Unit,
) {
    dialog(
        route = destination.routeSchema,
        arguments = destination.arguments,
        deepLinks = destination.deepLinks,
        dialogProperties = dialogProperties,
        content = content,
    )
}

@OptIn(ExperimentalMaterialNavigationApi::class)
@Suppress("Unused")
fun NavGraphBuilder.bottomSheetDestination(
    destination: Destination<*>,
    content: @Composable ColumnScope.(NavBackStackEntry) -> Unit,
) {
    bottomSheet(
        route = destination.routeSchema,
        arguments = destination.arguments,
        deepLinks = destination.deepLinks,
        content = content,
    )
}

/**
 * Constructors a nested [NavGraph] based on the given [Graph].
 *
 * @param builder the builder used to construct the graph.
 */
@Suppress("Unused")
inline fun NavGraphBuilder.navigationGraph(graph: Graph<*>, builder: NavGraphBuilder.() -> Unit) {
    navigation(
        route = graph.routeSchema,
        startDestination = graph.startDestination.routeSchema,
        builder = builder,
    )
}
