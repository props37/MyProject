package ru.zarina.zarina.ui.screens.webpage

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@Composable
fun WebpageScreenContent(
    headers: ImmutableMap<String, String>?,
    url: String,
) {
    var isWebViewLoading by remember { mutableStateOf(true) }
    val isLoading = headers == null || isWebViewLoading
    ZarinaScaffold(
        isModalLoaderVisible = isLoading,
    ) {
        if (headers != null)
            Webpage(
                headers = headers,
                url = url,
                onLoadingChange = { isWebViewLoading = it },
            )
    }
}

@Composable
private fun Webpage(
    headers: ImmutableMap<String, String>,
    url: String,
    onLoadingChange: (Boolean) -> Unit,
) {
    val state = rememberWebViewState(
        url = url,
        additionalHttpHeaders = headers,
    )
    val loadingState by remember { derivedStateOf { state.loadingState } }
    val lastLoadedUrl by remember { derivedStateOf { state.lastLoadedUrl } }
    LaunchedEffect(loadingState) {
        onLoadingChange(loadingState != LoadingState.Finished || lastLoadedUrl == null)
    }
    WebView(
        state = state,
        onCreated = {
            with(it.settings) {
                @SuppressLint("SetJavaScriptEnabled")
                javaScriptEnabled = true
                domStorageEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.screenBackground)
            .safeDrawingPadding(),
    )
}

@Composable
fun WebpageScreen() {
    val viewModel = hiltViewModel<WebpageViewModel>()

    val headers by viewModel.headers.collectAsStateWithLifecycle()
    val url by viewModel.url.collectAsStateWithLifecycle()

    WebpageScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    WebpageScreenContent(
        headers = headers,
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
            headers = persistentMapOf(),
            url = "https://zarina.ru/help/privacy-policy/",
        )
    }
}
