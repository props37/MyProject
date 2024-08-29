package ru.livetyping.zarina.presentation.screen.checkout.deliverymethod

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.checkout.DeliveryMethod
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.presentation.common.component.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.presentation.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.presentation.screen.checkout.deliverymethod.CheckoutDeliveryMethodViewModel.State
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.Crossfade

@Suppress("ConstPropertyName")
object CheckoutDeliveryMethodScreenComponents {

    @Composable
    fun DeliveryMethods(
        state: State,
        onMethodClicked: (DeliveryMethod) -> Unit,
        onErrorRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        @Suppress("NAME_SHADOWING")
        Crossfade(
            targetState = state,
            contentKey = {
                when (it) {
                    is State.DeliveryMethods -> DeliveryMethodsContentKey
                    is State.Error -> it
                    State.Loading -> it
                }
            },
            modifier = modifier,
        ) { state ->
            when (state) {
                is State.DeliveryMethods -> {
                    DeliveryMethodsImpl(
                        state = state,
                        onDeliveryMethodClicked = onMethodClicked,
                    )
                }

                State.Loading -> {
                    DeliveryMethodsSkeleton()
                }

                is State.Error -> {
                    ZarinaErrorScreen(
                        state = state.state,
                        onButtonClicked = onErrorRefreshClicked,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .navigationBarsPadding(),
                    )
                }
            }
        }
    }

    @Composable
    private fun DeliveryMethodsImpl(
        state: State.DeliveryMethods,
        onDeliveryMethodClicked: (DeliveryMethod) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
            state.methods.forEachIndexed { index, method ->
                key(method.id.value) {
                    DeliveryMethod(
                        method = method,
                        onClick = onDeliveryMethodClicked,
                    )

                    if (index < state.methods.lastIndex) {
                        ZarinaDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        )
                    }
                }
            }

            val navigationBarHeight =
                WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            Spacer(modifier = Modifier.height(navigationBarHeight + 20.dp))
        }
    }

    @Composable
    private fun DeliveryMethodsSkeleton(
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
            val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)
            repeat(DeliveryMethodSkeletonCount) { index ->
                DeliveryMethodSkeleton(shimmer = shimmer)

                if (index < DeliveryMethodSkeletonCount - 1) {
                    ZarinaDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }

            val navigationBarHeight =
                WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            Spacer(modifier = Modifier.height(navigationBarHeight + 20.dp))
        }
    }

    @Composable
    private fun DeliveryMethod(
        method: DeliveryMethod,
        onClick: (DeliveryMethod) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(
            onClick = { onClick(method) },
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            startContent = {
                Column {
                    Text(
                        text = method.name,
                        style = UiKitTheme.typography.secondary.light,
                    )

                    if (method.description != null) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = method.description,
                            style = UiKitTheme.typography.footnote.light,
                        )
                    }
                }
            },
            endContent = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_small_arrow_up_24),
                    contentDescription = stringResource(R.string.select_delivery_method),
                    tint = UiKitTheme.colors.icon.regular.default,
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(degrees = 90f),
                )
            },
            modifier = modifier,
        )
    }

    @Composable
    private fun DeliveryMethodSkeleton(
        shimmer: Shimmer,
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            startContent = {
                Column {
                    ZarinaTextSkeleton(
                        shimmer = shimmer,
                        textStyle = UiKitTheme.typography.secondary.light,
                        modifier = Modifier.width(200.dp),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    ZarinaTextSkeleton(
                        shimmer = shimmer,
                        textStyle = UiKitTheme.typography.footnote.light,
                        modifier = Modifier.width(260.dp),
                    )
                }
            },
            endContent = {
                ZarinaSkeleton(
                    shimmer = shimmer,
                    modifier = Modifier.size(16.dp),
                )
            },
            modifier = modifier,
        )
    }

    private const val DeliveryMethodsContentKey = "DeliveryMethodsContentKey"

    private const val DeliveryMethodSkeletonCount = 6
}
