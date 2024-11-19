package ru.livetyping.zarina.presentation.navigation.util

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination

val AnimatedContentTransitionScope<NavBackStackEntry>.initialDestination: NavDestination
    get() = initialState.destination

val AnimatedContentTransitionScope<NavBackStackEntry>.targetDestination: NavDestination
    get() = targetState.destination
