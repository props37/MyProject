package ru.livetyping.zarina.core.uicomponent.captcha

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptcha
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptchaToken
import ru.livetyping.zarina.core.uicommon.YandexCaptchaEvent
import ru.livetyping.zarina.core.uikit.bottomsheet.ZarinaBottomSheetDefaults
import timber.log.Timber

@SuppressLint("ComposeModifierMissing")
@Suppress("UnusedReceiverParameter")
@Composable
public fun BoxScope.YandexCaptchaDialog(
    captcha: YandexCaptcha,
    onEvent: (YandexCaptchaEvent) -> Unit,
    windowInsetsProvider: @Composable () -> WindowInsets = { WindowInsets.safeDrawing },
) {
    val captchaUrl = captcha.url.value

    var isUserActionRequired by remember { mutableStateOf(false) }
    var isPageLoaded by remember(captcha) { mutableStateOf(false) }

    val isWebViewVisible by remember(captcha) {
        derivedStateOf { isPageLoaded && isUserActionRequired }
    }

    val backgroundClickableModifier = if (isWebViewVisible) {
        Modifier.clickable(
            interactionSource = null,
            indication = null,
            onClick = { onEvent(YandexCaptchaEvent.DismissRequested) },
        )
    } else {
        Modifier
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(keyboardController) {
        snapshotFlow { isWebViewVisible }.collect {
            keyboardController?.hide()
        }
    }

    BackHandler(
        enabled = isWebViewVisible,
        onBack = { onEvent(YandexCaptchaEvent.DismissRequested) },
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(
                    color = ZarinaBottomSheetDefaults.ScrimColor,
                    alpha = if (isWebViewVisible) 1f else 0f,
                )
            }
            .windowInsetsPadding(windowInsetsProvider())
            .then(backgroundClickableModifier),
    ) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    )
                    isVisible = isWebViewVisible

                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            Timber.tag(TAG).v("onPageFinished: $url")
                            isPageLoaded = true
                        }
                    }

                    settings.javaScriptEnabled = true
                    val jsInterface = object : YandexCaptchaJsInterface {
                        @JavascriptInterface
                        override fun onGetToken(token: String) {
                            Timber.tag(TAG).v("onGetToken: $token")
                            val yandexCaptchaToken = YandexCaptchaToken(token)
                            onEvent(YandexCaptchaEvent.TokenReceived(yandexCaptchaToken))
                        }

                        @JavascriptInterface
                        override fun onChallengeVisible() {
                            Timber.tag(TAG).v("onChallengeVisible")
                            isUserActionRequired = true
                        }

                        @JavascriptInterface
                        override fun onChallengeHidden() {
                            Timber.tag(TAG).v("onChallengeHidden")
                            isUserActionRequired = false
                        }
                    }
                    addJavascriptInterface(jsInterface, JS_INTERFACE_NAME)

                    loadUrl(captchaUrl)
                }
            },
            update = { webView ->
                webView.isVisible = isWebViewVisible

                if (webView.url != captchaUrl) {
                    webView.loadUrl(captchaUrl)
                }
            },
            onRelease = { webView ->
                webView.settings.javaScriptEnabled = false
                webView.removeJavascriptInterface(JS_INTERFACE_NAME)
                webView.destroy()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.5f),
        )
    }
}

private interface YandexCaptchaJsInterface {
    @JavascriptInterface
    fun onGetToken(token: String)

    @JavascriptInterface
    fun onChallengeVisible()

    @JavascriptInterface
    fun onChallengeHidden()
}

private const val JS_INTERFACE_NAME = "NativeClient"

private const val TAG = "YandexCaptchaDialog"
