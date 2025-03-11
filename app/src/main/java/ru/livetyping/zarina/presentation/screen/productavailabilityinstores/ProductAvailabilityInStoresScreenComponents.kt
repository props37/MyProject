package ru.livetyping.zarina.presentation.screen.productavailabilityinstores

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.product.ProductAvailabilityInStore
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.presentation.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.presentation.common.component.tag.ZarinaTag
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.common.util.nameResId
import ru.livetyping.zarina.presentation.screen.productavailabilityinstores.ProductAvailabilityInStoresViewModel.AvailabilityState
import ru.livetyping.zarina.presentation.screen.productavailabilityinstores.ProductAvailabilityInStoresViewModel.OfferItem
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.Crossfade
import ru.livetyping.zarina.util.compose.plus

object ProductAvailabilityInStoresScreenComponents {

    @Composable
    fun TopBar(
        onBackClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            startContent = {
                ZarinaBackIconButton(
                    onClick = onBackClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            centerContent = {
                Text(
                    text = stringResource(R.string.availability_in_stores)
                )
            },
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = modifier,
        )
    }

    @Composable
    fun Offers(
        offers: ImmutableList<OfferItem>,
        onOfferClicked: (OfferItem) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = modifier,
        ) {
            items(
                items = offers,
                key = { it.offer.barcode.value },
            ) { item ->
                ZarinaTag(
                    onClick = { onOfferClicked(item) },
                    isEnabled = item.offer.isAvailableInStores,
                    isSelected = item.isSelected,
                ) {
                    val offer = item.offer
                    val heightText = offer.height?.let { stringResource(R.string.height_cm, it) }
                    val text = remember(offer, item.isHeightVisible, heightText) {
                        buildString {
                            append(offer.size)
                            if (offer.sizeRu != null) {
                                append(" ${offer.sizeRu}")
                            }
                            if (item.isHeightVisible && heightText != null) {
                                append(" — $heightText")
                            }
                        }
                    }
                    Text(text = text)
                }
            }
        }
    }

    @Suppress("NAME_SHADOWING")
    @Composable
    fun Availability(
        state: AvailabilityState,
        city: City?,
        onErrorRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Crossfade(
            targetState = state,
            contentKey = { state ->
                when (state) {
                    is AvailabilityState.Success -> AvailabilityContentKey.Success
                    is AvailabilityState.Error -> state
                    AvailabilityState.Loading -> state
                    AvailabilityState.NotAvailable -> state
                }
            },
            modifier = modifier,
        ) { state ->
            when (state) {
                is AvailabilityState.Success -> {
                    AvailabilitySuccess(
                        state = state,
                        city = city,
                    )
                }

                AvailabilityState.NotAvailable -> {
                    AvailabilityNotAvailable(modifier = Modifier.safeContentPadding())
                }

                AvailabilityState.Loading -> {
                    AvailabilityLoading()
                }

                is AvailabilityState.Error -> {
                    ZarinaErrorScreen(
                        state = state.errorState,
                        onButtonClicked = onErrorRefreshClicked,
                        modifier = Modifier
                            .fillMaxSize()
                            .safeDrawingPadding()
                            .padding(16.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun AvailabilitySuccess(
        state: AvailabilityState.Success,
        city: City?,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            if (city != null) {
                ZarinaItem {
                    Text(
                        text = city.name,
                        style = UiKitTheme.typography.secondary.bold,
                    )
                }
            }

            val safeDrawingBottomPadding = WindowInsets.safeDrawing
                .only(WindowInsetsSides.Bottom)
                .asPaddingValues()

            LazyColumn(
                contentPadding = safeDrawingBottomPadding.plus(PaddingValues(bottom = 20.dp)),
            ) {
                itemsIndexed(
                    items = state.availability,
                    key = { _, item -> item.store.id.value },
                ) { index, availability ->
                    AvailabilityItem(availability)

                    if (index < state.availability.lastIndex) {
                        ZarinaDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun AvailabilityNotAvailable(
        modifier: Modifier = Modifier,
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            Text(
                text = stringResource(R.string.product_is_not_available),
                style = UiKitTheme.typography.primary.regular,
                color = UiKitTheme.colors.text.general.regular.default,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp),
            )
        }
    }

    @Composable
    private fun AvailabilityLoading(
        modifier: Modifier = Modifier,
    ) {
        val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)

        val safeDrawingBottomPadding = WindowInsets.safeDrawing
            .only(WindowInsetsSides.Bottom)
            .asPaddingValues()

        LazyColumn(
            contentPadding = safeDrawingBottomPadding.plus(PaddingValues(bottom = 20.dp)),
            modifier = modifier,
        ) {
            items(AvailabilityItemSkeletonCount) { index ->
                ZarinaItem(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)) {
                    Column {
                        ZarinaTextSkeleton(
                            textStyle = UiKitTheme.typography.secondary.light,
                            shimmer = shimmer,
                            modifier = Modifier.fillMaxWidth(0.45f),
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        ZarinaTextSkeleton(
                            textStyle = UiKitTheme.typography.tertiary.light,
                            shimmer = shimmer,
                            modifier = Modifier.fillMaxWidth(0.5f),
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        ZarinaTextSkeleton(
                            textStyle = UiKitTheme.typography.tertiary.light,
                            shimmer = shimmer,
                            modifier = Modifier.fillMaxWidth(0.35f),
                        )
                    }
                }

                if (index < AvailabilityItemSkeletonCount - 1) {
                    ZarinaDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun AvailabilityItem(
        availability: ProductAvailabilityInStore,
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            modifier = modifier,
        ) {
            Column {
                Text(
                    text = availability.store.name,
                    style = UiKitTheme.typography.secondary.light,
                )
                Spacer(modifier = Modifier.height(4.dp))

                if (
                    availability.amount == ProductAvailabilityInStore.Amount.LAST_CHANCE
                    || availability.amount == ProductAvailabilityInStore.Amount.LITTLE
                ) {
                    Text(
                        text = stringResource(availability.amount.nameResId),
                        style = UiKitTheme.typography.tertiary.regular,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }

                Text(
                    text = availability.store.address,
                    style = UiKitTheme.typography.tertiary.light,
                    color = UiKitTheme.colors.text.general.regular.muted,
                )

                if (availability.store.schedule != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = availability.store.schedule,
                        style = UiKitTheme.typography.tertiary.light,
                        color = UiKitTheme.colors.text.general.regular.muted,
                    )
                }
            }
        }
    }

    private enum class AvailabilityContentKey { Success }

    private const val AvailabilityItemSkeletonCount = 10
}
