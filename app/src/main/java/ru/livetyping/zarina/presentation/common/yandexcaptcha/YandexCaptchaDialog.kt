package ru.livetyping.zarina.presentation.common.yandexcaptcha

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawingPadding
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
import ru.livetyping.zarina.domain.captcha.YandexCaptchaToken
import ru.livetyping.zarina.presentation.common.component.bottomsheet.ZarinaBottomSheetDefaults
import timber.log.Timber

@Suppress("UnusedReceiverParameter")
@SuppressLint("ComposeModifierMissing")
@Composable
fun BoxScope.YandexCaptchaDialog(
    state: YandexCaptchaDialogState,
    onDismissRequest: () -> Unit,
    onTokenReceived: (YandexCaptchaToken) -> Unit,
) {
    if (state is YandexCaptchaDialogState.Visible) {
        val captcha = state.captcha
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
                onClick = onDismissRequest,
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
            onBack = onDismissRequest,
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
                .safeDrawingPadding()
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
                                if (url == captchaUrl) isPageLoaded = true
                            }
                        }

                        settings.javaScriptEnabled = true
                        val jsInterface = object : YandexCaptchaJsInterface {
                            @JavascriptInterface
                            override fun onGetToken(token: String) {
                                Timber.tag(TAG).v("onGetToken: $token")
                                onTokenReceived(YandexCaptchaToken(token))
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
                        addJavascriptInterface(jsInterface, "NativeClient")

                        loadUrl(captchaUrl)
                    }
                },
                update = { webView ->
                    webView.isVisible = isWebViewVisible

                    if (webView.url != captchaUrl) {
                        webView.loadUrl(captchaUrl)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.5f),
            )
        }
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

private const val TAG = "YandexCaptchaDialog"
