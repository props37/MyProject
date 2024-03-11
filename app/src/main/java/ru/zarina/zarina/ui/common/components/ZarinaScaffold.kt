package ru.zarina.zarina.ui.common.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.zarina.zarina.ui.common.base.ErrorState
import ru.zarina.zarina.ui.theme.UiKitTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ZarinaScaffold(
    modifier: Modifier = Modifier,
    errorState: ErrorState? = null,
    onErrorButtonClick: () -> Unit = {},
    isModalLoaderVisible: Boolean = false,
    toolbar: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(UiKitTheme.colorsOld.screenBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            toolbar()
            content()
        }
        AnimatedContent(
            targetState = errorState,
            transitionSpec = { fadeIn() with fadeOut() },
            contentAlignment = Alignment.Center,
            label = "error state",
            modifier = Modifier.fillMaxSize()
        ) { state ->
            if (state != null)
                ModalError(
                    state = state,
                    onButtonClick = { onErrorButtonClick() },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(UiKitTheme.colorsOld.screenBackground)
                        .navigationBarsPadding()
                )
        }
        ModalLoader(
            isVisible = isModalLoaderVisible,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
