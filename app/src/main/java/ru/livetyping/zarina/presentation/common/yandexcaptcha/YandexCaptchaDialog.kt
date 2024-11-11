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
import kotlinx.coroutines.delay
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.domain.captcha.YandexCaptchaMode
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
        var currentCaptchaMode by remember(state) {
            mutableStateOf(YandexCaptchaMode.getMain())
        }
        val currentCaptchaModeKey by remember {
            derivedStateOf {
                when (currentCaptchaMode) {
                    YandexCaptchaMode.SLIDER -> BuildConfig.YANDEX_CAPTCHA_SLIDER_KEY
                    YandexCaptchaMode.CHECKBOX -> BuildConfig.YANDEX_CAPTCHA_CHECKBOX_KEY
                }
            }
        }
        val currentCaptchaUrl by remember(captcha) {
            derivedStateOf {
                getCaptchaUrl(captcha.url.value, currentCaptchaModeKey, captcha.isInvisible)
            }
        }
        var isUserActionRequired by remember { mutableStateOf(false) }

        var isPageLoaded by remember(state, currentCaptchaMode) { mutableStateOf(false) }
        val isWebViewVisible by remember(captcha.isInvisible) {
            derivedStateOf {
                isPageLoaded && (!captcha.isInvisible || (captcha.isInvisible && isUserActionRequired))
            }
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

        LaunchedEffect(currentCaptchaMode, isWebViewVisible) {
            val nextMode = currentCaptchaMode.getNext()
            if (isWebViewVisible && nextMode != null) {
                delay(YandexCaptchaMode.MODE_MAX_DURATION_MILLIS)
                currentCaptchaMode = nextMode
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
                .then(backgroundClickableModifier),
        ) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                        )
                        settings.useWideViewPort = true
                        settings.loadWithOverviewMode = true
                        isVisible = isWebViewVisible

                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                Timber.tag(TAG).v("onPageFinished: $url")
                                if (url == currentCaptchaUrl) isPageLoaded = true
                            }
                        }

                        settings.javaScriptEnabled = true
                        val jsInterface = object : YandexCaptchaJsInterface {
                            @JavascriptInterface
                            override fun onGetToken(token: String) {
                                Timber.tag(TAG).v("onGetToken: $token")
                                val captchaToken = YandexCaptchaToken(
                                    token = token,
                                    mode = currentCaptchaMode,
                                )
                                onTokenReceived(captchaToken)
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

                        loadUrl(currentCaptchaUrl)
                    }
                },
                update = { webView ->
                    webView.isVisible = isWebViewVisible

                    if (webView.url != currentCaptchaUrl) {
                        webView.loadUrl(currentCaptchaUrl)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.5f)
                    .safeDrawingPadding(),
            )
        }
    }
}

private fun getCaptchaUrl(baseUrl: String, key: String, isInvisible: Boolean): String {
    return buildString {
        append(baseUrl)
        append("?sitekey=$key")
        if (isInvisible) {
            append("&invisible=true")
        }
    }
}

private const val TAG = "YandexCaptchaDialog"

private interface YandexCaptchaJsInterface {
    @JavascriptInterface
    fun onGetToken(token: String)

    @JavascriptInterface
    fun onChallengeVisible()

    @JavascriptInterface
    fun onChallengeHidden()
}
