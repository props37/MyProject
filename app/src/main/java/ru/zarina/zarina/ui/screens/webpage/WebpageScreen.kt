package ru.zarina.zarina.ui.screens.webpage

import android.annotation.SuppressLint
import android.content.Intent
import android.webkit.WebResourceRequest
import android.webkit.WebView
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.base.ErrorState
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme
import ru.zarina.zarina.utils.android.tryStartActivity

@Composable
fun WebpageScreenContent(
    headers: ImmutableMap<String, String>?,
    url: String,
    onReloadClick: () -> Unit,
    errorType: WebpageViewModel.ErrorType?,
) {
    var isWebViewLoading by remember { mutableStateOf(true) }
    val isLoading = headers == null || isWebViewLoading
    val errorState = when (errorType) {
        WebpageViewModel.ErrorType.GENERIC -> ErrorState.GENERIC
        else -> null
    }
    ZarinaScaffold(
        isModalLoaderVisible = isLoading,
        errorState = errorState,
        onErrorButtonClick = { onReloadClick() },
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
    val context = LocalContext.current
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
        client = remember {
            object : AccompanistWebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?,
                ): Boolean {
                    val uri = request?.url ?: return false
                    val isOverridden = uri.scheme == "mailto"
                    if (isOverridden) {
                        context.tryStartActivity(Intent(Intent.ACTION_VIEW, uri))
                    }
                    return isOverridden
                }
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
    val errorType by viewModel.errorType.collectAsStateWithLifecycle()

    WebpageScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    WebpageScreenContent(
        headers = headers,
        url = url,
        onReloadClick = viewModel::onReloadClick,
        errorType = errorType,
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
            errorType = null,
            onReloadClick = {}
        )
    }
}
