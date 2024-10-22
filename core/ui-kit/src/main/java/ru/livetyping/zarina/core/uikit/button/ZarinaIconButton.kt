package ru.livetyping.zarina.core.uikit.button

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalContentColor
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.core.uikit.loader.ZarinaCircularLoader
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    loaderSize: Dp = ZarinaIconButtonDefaults.LoaderSize,
    loaderColor: Color = ZarinaIconButtonDefaults.LoaderColor,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = ripple(
        bounded = false,
        radius = ZarinaIconButtonDefaults.IndicationRadius,
    ),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalContentColor provides UiKitTheme.colors.icon.regular.default,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .minimumInteractiveComponentSize()
                .clickable(
                    onClick = onClick,
                    enabled = isEnabled,
                    role = Role.Button,
                    interactionSource = interactionSource,
                    indication = indication,
                )
                // Workaround as minimumInteractiveComponentSize() that placed before clickable()
                // doesn't always works as intended
                .minimumInteractiveComponentSize(),
        ) {
            AnimatedContent(
                targetState = isLoading,
                transitionSpec = {
                    AnimatedContentDefaultTransitionSpec.using(SizeTransform(clip = false))
                },
                contentAlignment = Alignment.Center,
                label = "ZarinaIconButton content",
            ) { isLoading ->
                if (!isLoading) {
                    content()
                } else {
                    ZarinaCircularLoader(
                        color = loaderColor,
                        modifier = Modifier.size(loaderSize),
                    )
                }
            }
        }
    }
}

public object ZarinaIconButtonDefaults {
    public val LoaderSize: Dp get() = DefaultIconSize

    public val LoaderColor: Color
        @Composable
        get() = UiKitTheme.colors.icon.regular.default

    public val IndicationRadius: Dp get() = DefaultIconSize

    private val DefaultIconSize: Dp get() = 24.dp
}
