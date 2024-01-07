package ru.zarina.zarina.ui.common.tooling.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import ru.zarina.zarina.ui.common.behavior.base.NoopBehaviorController
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.BottomNavBarBehavior
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.LocalBottomNavBarBehaviorController
import ru.zarina.zarina.ui.theme.rework.ZarinaTheme

@Composable
fun ZarinaPreview(
    content: @Composable () -> Unit,
) {
    val bottomNavBarBehaviorController = remember {
        val defaultBehavior = BottomNavBarBehavior.Hidden(isAnimated = false)
        NoopBehaviorController<BottomNavBarBehavior>(
            defaultBehavior = defaultBehavior,
            throwExceptions = false,
        )
    }

    CompositionLocalProvider(
        LocalBottomNavBarBehaviorController provides bottomNavBarBehaviorController,
    ) {
        ZarinaTheme(content = content)
    }
}
