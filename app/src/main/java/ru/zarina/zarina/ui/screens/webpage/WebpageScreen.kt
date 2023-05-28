package ru.zarina.zarina.ui.screens.webpage

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun WebpageScreenContent(
    url: String,
) {
    val state = rememberWebViewState(url = url)
    WebView(
        state = state,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun WebpageScreen() {
    val viewModel = hiltViewModel<WebpageViewModel>()

    val url by viewModel.url.collectAsStateWithLifecycle()

    WebpageScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    WebpageScreenContent(
        url = url,
    )
}

@Composable
fun WebpageScreenBehavior(
    sideEffects: Flow<WebpageViewModel.SideEffect>,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                else -> TODO()
            }
        }
    }
}

@Preview
@Composable
fun WebpageScreenContentPreview() {
    ZarinaTheme {
        WebpageScreenContent(
            url = "https://zarina.ru/help/privacy-policy/"
        )
    }
}
