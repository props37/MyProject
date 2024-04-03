package ru.livetyping.zarina.ui.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.ui.base.text.Text
import ru.livetyping.zarina.ui.base.text.textString
import ru.livetyping.zarina.ui.theme.UiKitTheme

@Composable
fun StateSnackbar(
    isVisible: Boolean,
    text: Text,
    modifier: Modifier = Modifier,
    enter: EnterTransition = StateSnackbarDefaults.enterTransition,
    exit: ExitTransition = StateSnackbarDefaults.exitTransition,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = enter,
        exit = exit,
        modifier = modifier
    ) {
        Snackbar(
            containerColor = UiKitTheme.colorsOld.snackbarBackground,
            contentColor = UiKitTheme.colorsOld.snackbarForeground,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = textString(text),
                style = UiKitTheme.typographyOld.circle1420,
            )
        }
    }
}


object StateSnackbarDefaults {

    val slideAnimationSpec: SpringSpec<IntOffset>
        get() = spring(
            stiffness = Spring.StiffnessLow,
            visibilityThreshold = IntOffset.VisibilityThreshold
        )

    val enterTransition
        get() = slideInVertically(slideAnimationSpec) { it * 2 }

    val exitTransition
        get() = slideOutVertically(slideAnimationSpec) { it * 2 }
}
