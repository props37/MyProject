package ru.livetyping.zarina.ui.common.component.pullrefresh

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.theme.UiKitTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ZarinaPullRefreshIndicator(
    refreshing: Boolean,
    state: PullRefreshState,
    modifier: Modifier = Modifier,
    backgroundColor: Color = UiKitTheme.colors.background.general.regular.default,
    contentColor: Color = UiKitTheme.colors.icon.regular.default,
    scale: Boolean = false,
) {
    PullRefreshIndicator(
        refreshing = refreshing,
        state = state,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        scale = scale,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        val state = rememberPullRefreshState(refreshing = true, onRefresh = {})
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color.White),
        ) {
            ZarinaPullRefreshIndicator(
                refreshing = true,
                state = state,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}
