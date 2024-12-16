package ru.livetyping.zarina.core.uikit.bottomnavbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.LocalBottomNavBarBehaviorController
import ru.livetyping.zarina.core.uikit.bottomnavbar.sizetracker.LocalBottomNavBarSizeTracker
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaBottomNavBar(
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = ZarinaBottomNavBarDefaults.DefaultWindowInsets,
    content: @Composable RowScope.() -> Unit,
) {
    val behaviorController = LocalBottomNavBarBehaviorController.current
    val behavior by behaviorController.currentBehavior.collectAsStateWithLifecycle()
    val isBottomNavBarVisible = behavior is BottomNavBarBehavior.Visible
    val isBottomNavBarAnimated = behavior.isAnimated

    val sizeTracker = LocalBottomNavBarSizeTracker.current

    val visibleState = remember { MutableTransitionState(isBottomNavBarVisible) }
    DisposableEffect(isBottomNavBarVisible) {
        visibleState.targetState = isBottomNavBarVisible
        onDispose {}
    }

    LaunchedEffect(visibleState, sizeTracker) {
        snapshotFlow { visibleState.currentState }.collect { isVisible ->
            if (!isVisible) {
                sizeTracker.onSizeChanged(IntSize.Zero)
            }
        }
    }

    AnimatedVisibility(
        visibleState = visibleState,
        enter = remember(isBottomNavBarAnimated) {
            if (isBottomNavBarAnimated) {
                expandVertically(ZarinaBottomNavBarDefaults.BottomNavBarAnimationSpec)
            } else {
                EnterTransition.None
            }
        },
        exit = remember(isBottomNavBarAnimated) {
            if (isBottomNavBarAnimated) {
                shrinkVertically(ZarinaBottomNavBarDefaults.BottomNavBarAnimationSpec)
            } else {
                ExitTransition.None
            }
        },
        modifier = modifier.onSizeChanged { size ->
            sizeTracker.onSizeChanged(size)
        },
    ) {
        val backgroundColor = UiKitTheme.colors.background.general.regular.default
        val topBorderColor = UiKitTheme.colors.border.general.default

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .animateEnterExit(
                    enter = remember(isBottomNavBarAnimated) {
                        if (isBottomNavBarAnimated) {
                            slideInVertically(ZarinaBottomNavBarDefaults.BottomNavBarContentAnimationSpec) { it }
                        } else {
                            EnterTransition.None
                        }
                    },
                    exit = remember(isBottomNavBarAnimated) {
                        if (isBottomNavBarAnimated) {
                            slideOutVertically(ZarinaBottomNavBarDefaults.BottomNavBarContentAnimationSpec) { it }
                        } else {
                            ExitTransition.None
                        }
                    },
                )
                .drawBehind {
                    drawRect(backgroundColor)
                    drawLine(
                        color = topBorderColor,
                        start = Offset.Zero,
                        end = Offset(size.width, 0f),
                        strokeWidth = 1.dp.toPx(),
                    )
                }
                .selectableGroup()
                .windowInsetsPadding(windowInsets)
                .clipToBounds()
                .padding(top = 6.dp, bottom = 4.dp),
            content = content,
        )
    }
}

public object ZarinaBottomNavBarDefaults {
    public val DefaultWindowInsets: WindowInsets
        @Composable
        get() = WindowInsets.navigationBars
            .union(WindowInsets.displayCutout)
            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)

    private const val BottomNavBarAnimationSpringStiffness = Spring.StiffnessMedium

    @Stable
    internal val BottomNavBarAnimationSpec: SpringSpec<IntSize> = spring(
        stiffness = BottomNavBarAnimationSpringStiffness,
        visibilityThreshold = IntSize.VisibilityThreshold,
    )

    @Stable
    internal val BottomNavBarContentAnimationSpec: SpringSpec<IntOffset> = spring(
        stiffness = BottomNavBarAnimationSpringStiffness,
        visibilityThreshold = IntOffset.VisibilityThreshold,
    )
}
