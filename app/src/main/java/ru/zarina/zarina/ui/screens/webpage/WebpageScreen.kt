package ru.zarina.zarina.ui.screens.webpage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.ui.common.base.ErrorStateOld
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarState
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.toolbar.CloseButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbarDefaults
import ru.zarina.zarina.ui.theme.old.ZarinaTheme
import ru.zarina.zarina.utils.android.tryStartActivity
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebpageScreenContent(
    headers: ImmutableMap<String, String>?,
    url: String,
    onReloadClick: () -> Unit,
    onCloseClick: () -> Unit,
    errorType: WebpageViewModel.ErrorType?,
) {
    var isWebViewLoading by remember { mutableStateOf(true) }
    val isLoading = headers == null || isWebViewLoading
    val errorState = when (errorType) {
        WebpageViewModel.ErrorType.GENERIC -> ErrorStateOld.GENERIC
        else -> null
    }
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = "",
                colors = ScreenToolbarDefaults.colors(
                    containerColor = Color.Transparent,
                ),
                endIcon = {
                    CloseButton(
                        onClick = onCloseClick,
                    )
                }
            )
        },
        isModalLoaderVisible = isLoading,
        errorState = errorState,
        onErrorButtonClick = { onReloadClick() },
    ) {
        if (headers != null)
            Webpage(
                headers = headers,
                url = url,
                onLoadingChange = { isWebViewLoading = it },
                modifier = Modifier.navigationBarsPadding(),
            )
    }
}

@Composable
private fun Webpage(
    headers: ImmutableMap<String, String>,
    url: String,
    onLoadingChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
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
        onCreated = { webView ->
            with(webView.settings) {
                @SuppressLint("SetJavaScriptEnabled")
                javaScriptEnabled = true
                domStorageEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
            }
            webView.enableDefaultResourceHandling()
        },
        client = remember {
            object : AccompanistWebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?,
                ): Boolean {
                    val uri = request?.url ?: return false
                    return handleMailto(context, uri)
                }
            }
        },
        modifier = modifier,
    )
}

private fun WebView.enableDefaultResourceHandling() {
    setDownloadListener { url, _, _, _, _ ->
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            context.tryStartActivity(intent)
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }
}

private fun handleMailto(context: Context, uri: Uri): Boolean {
    val isOverridden = uri.scheme == "mailto"
    return if (isOverridden) {
        context.tryStartActivity(Intent(Intent.ACTION_VIEW, uri))
    } else {
        false
    }
}

@Composable
fun WebpageScreen(
    goBack: () -> Unit,
) {
    val viewModel = koinViewModel<WebpageViewModel>()

    val headers by viewModel.headers.collectAsStateWithLifecycle()
    val url by viewModel.url.collectAsStateWithLifecycle()
    val errorType by viewModel.errorType.collectAsStateWithLifecycle()

    WebpageScreenBehavior(
        sideEffects = viewModel.sideEffects,
        goBack = goBack,
    )

    WebpageScreenContent(
        headers = headers,
        url = url,
        onReloadClick = viewModel::onReloadClick,
        onCloseClick = viewModel::onCloseClick,
        errorType = errorType,
    )
}

@Composable
fun WebpageScreenBehavior(
    sideEffects: Flow<WebpageViewModel.SideEffect>,
    goBack: () -> Unit,
) {
    NavigationBarState(isVisible = false, isAnimated = false)
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                WebpageViewModel.SideEffect.GoBack -> goBack()
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
            onReloadClick = {},
            onCloseClick = {},
        )
    }
}
