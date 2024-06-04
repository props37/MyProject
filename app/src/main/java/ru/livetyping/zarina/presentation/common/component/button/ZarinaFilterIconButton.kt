package ru.livetyping.zarina.presentation.common.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.text.unscalable

@Composable
fun ZarinaFilterIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    appliedFilterCount: Int = 0,
    iconSize: Dp = 24.dp,
) {
    Box(modifier = modifier) {
        ZarinaIconButton(
            onClick = onClick,
            indication = ripple(bounded = false, radius = iconSize),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_settings_menu_24),
                contentDescription = stringResource(R.string.filters),
                modifier = Modifier.size(iconSize),
            )
        }

        AppliedFilterCounter(
            appliedFilterCount = appliedFilterCount,
            modifier = Modifier.align(AppliedFilterCounterAlignment),
        )
    }
}

@Composable
private fun AppliedFilterCounter(
    appliedFilterCount: Int,
    modifier: Modifier = Modifier,
) {
    if (appliedFilterCount > 0) {
        Text(
            text = appliedFilterCount.toString(),
            style = UiKitTheme.typography.caption2.bold.unscalable(LocalDensity.current),
            color = UiKitTheme.colors.text.general.inversed.default,
            modifier = modifier
                .background(
                    color = UiKitTheme.colors.background.general.inversed.default,
                    shape = CircleShape,
                )
                .padding(start = 6.dp, top = 1.dp, end = 6.dp),
        )
    }
}

private val AppliedFilterCounterAlignment: Alignment
    get() = BiasAlignment(0.5f, -0.5f)

@Preview
@Composable
private fun PreviewNoAppliedFilters() {
    ZarinaPreview {
        ZarinaFilterIconButton(
            onClick = {},
            modifier = Modifier.background(Color.White),
        )
    }
}

@Preview
@Composable
private fun PreviewOneAppliedFilters() {
    ZarinaPreview {
        ZarinaFilterIconButton(
            onClick = {},
            appliedFilterCount = 1,
            modifier = Modifier.background(Color.White),
        )
    }
}

@Preview
@Composable
private fun PreviewTenAppliedFilters() {
    ZarinaPreview {
        ZarinaFilterIconButton(
            onClick = {},
            appliedFilterCount = 10,
            modifier = Modifier.background(Color.White),
        )
    }
}
