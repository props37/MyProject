package ru.zarina.zarina.ui.common.component.button

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.component.loader.ZarinaCircularLoader
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.animation.AnimatedContentDefaultTransitionSpec

@Composable
fun ZarinaIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    loaderSize: Dp = 24.dp,
    loaderColor: Color = UiKitTheme.colors.icon.regular.default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = rememberRipple(bounded = false, radius = 24.dp),
    content: @Composable () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clickable(
                onClick = onClick,
                enabled = isEnabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = indication,
            )
            .minimumInteractiveComponentSize(),
    ) {
        AnimatedContent(
            targetState = isLoading,
            transitionSpec = {
                AnimatedContentDefaultTransitionSpec().using(SizeTransform(clip = false))
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

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        var isLoading by remember { mutableStateOf(false) }
        ZarinaIconButton(
            onClick = { isLoading = !isLoading },
            isLoading = isLoading,
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_heart_24),
                contentDescription = null,
            )
        }
    }
}
