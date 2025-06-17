package ru.livetyping.zarina.feature.onboarding.ui.impl.impl.ui

import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.rememberAsyncImagePainter
import coil3.imageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.ImageRequest
import coil3.request.error
import coil3.request.fallback
import coil3.size.Size
import okhttp3.OkHttpClient
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.uikit.logo.ZarinaLogo
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.onboarding.ui.impl.R
import java.util.concurrent.TimeUnit

@Composable
internal fun Banner(
    url: Url?,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        val context = LocalContext.current

        var isBannerDisplayed by remember { mutableStateOf(false) }

        val imageLoader = remember(context) {
            getBannerImageLoader(context)
        }
        if (url != null) {
            val imageRequest = remember(context, url) {
                val fallbackBannerResId = R.drawable.onboarding_default_banner
                ImageRequest.Builder(context)
                    .data(url.value)
                    .size(Size.ORIGINAL)
                    .error(fallbackBannerResId)
                    .fallback(fallbackBannerResId)
                    .build()
            }
            val contentScale = ContentScale.Crop
            val painter = rememberAsyncImagePainter(
                model = imageRequest,
                imageLoader = imageLoader,
                contentScale = contentScale,
                onSuccess = { isBannerDisplayed = true },
                onError = { isBannerDisplayed = true },
            )

            Image(
                painter = painter,
                contentDescription = null,
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize(),
            )
        }

        val logoColor by animateColorAsState(
            targetValue = if (isBannerDisplayed) {
                UiKitTheme2.colors.white
            } else {
                UiKitTheme2.colors.mainBlack
            },
            label = "Banner logo color",
        )

        ZarinaLogo(
            color = logoColor,
            animate = !isBannerDisplayed,
            modifier = Modifier
                .align(Alignment.Center)
                .size(SplashScreenLogoSize),
        )
    }
}

private fun getBannerImageLoader(context: Context): ImageLoader {
    return context.imageLoader.newBuilder()
        .components {
            val okHttpFactory = OkHttpNetworkFetcherFactory(
                callFactory = {
                    OkHttpClient.Builder()
                        .connectTimeout(BANNER_IMAGE_LOADER_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                        .readTimeout(BANNER_IMAGE_LOADER_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                        .build()
                }
            )
            add(okHttpFactory)
        }
        .build()
}

// According to https://developer.android.com/develop/ui/views/launch/splash-screen
private val SplashScreenLogoSize: Dp get() = 192.dp

private const val BANNER_IMAGE_LOADER_TIMEOUT_SECONDS = 3L
