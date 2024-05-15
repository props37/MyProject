package ru.livetyping.zarina.presentation.common.tooling.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import ru.livetyping.zarina.base.behavior.NoOpBehaviorController
import ru.livetyping.zarina.presentation.bottomnavbar.LocalBottomNavBarSizeTracker
import ru.livetyping.zarina.presentation.bottomnavbar.NoOpBottomNavBarSizeTracker
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.BottomNavBarBehavior
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.LocalBottomNavBarBehaviorController
import ru.livetyping.zarina.presentation.theme.ZarinaTheme

@Composable
fun ZarinaPreview(
    content: @Composable () -> Unit,
) {
    val bottomNavBarBehaviorController = remember {
        val defaultBehavior = BottomNavBarBehavior.Hidden(isAnimated = false)
        NoOpBehaviorController<BottomNavBarBehavior>(
            defaultBehavior = defaultBehavior,
            throwExceptions = false,
        )
    }

    val bottomNavBarSizeTracker = remember {
        NoOpBottomNavBarSizeTracker(throwExceptions = false)
    }

    CompositionLocalProvider(
        LocalBottomNavBarBehaviorController provides bottomNavBarBehaviorController,
        LocalBottomNavBarSizeTracker provides bottomNavBarSizeTracker,
    ) {
        ZarinaTheme(content = content)
    }
}
