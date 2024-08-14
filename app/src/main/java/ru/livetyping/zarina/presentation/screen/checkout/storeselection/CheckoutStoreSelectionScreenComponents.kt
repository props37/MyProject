package ru.livetyping.zarina.presentation.screen.checkout.storeselection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.checkout.PickupStore
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.presentation.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.presentation.screen.checkout.storeselection.CheckoutStoreSelectionViewModel.State
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.Crossfade

@Suppress("ConstPropertyName")
object CheckoutStoreSelectionScreenComponents {

    @Composable
    fun City(
        city: City?,
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            modifier = modifier.heightIn(min = 48.dp),
        ) {
            Crossfade(
                targetState = city,
                contentKey = { it != null },
            ) { city ->
                val textStyle = UiKitTheme.typography.secondary.bold
                if (city != null) {
                    Text(
                        text = city.name,
                        style = textStyle,
                    )
                } else {
                    ZarinaTextSkeleton(
                        textStyle = textStyle,
                        modifier = Modifier.width(100.dp),
                    )
                }
            }
        }
    }


    @Composable
    fun Stores(
        state: State,
        onStoreClicked: (PickupStore) -> Unit,
        onStoresErrorRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        @Suppress("NAME_SHADOWING")
        Crossfade(
            targetState = state,
            contentKey = {
                when (it) {
                    is State.Stores -> StoresContentKeyStores
                    is State.Error -> it
                    State.Loading -> it
                }
            },
            modifier = modifier,
        ) { state ->
            when (state) {
                is State.Stores -> {
                    StoresImpl(
                        storesState = state,
                        onStoreClicked = onStoreClicked,
                    )
                }

                State.Loading -> {
                    StoresSkeleton()
                }

                is State.Error -> {
                    ZarinaErrorScreen(
                        state = state.state,
                        onButtonClicked = onStoresErrorRefreshClicked,
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding()
                            .padding(16.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun StoresImpl(
        storesState: State.Stores,
        onStoreClicked: (PickupStore) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val navigationBarHeight =
            WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        val contentPadding = PaddingValues(bottom = navigationBarHeight + 20.dp)

        LazyColumn(
            contentPadding = contentPadding,
            modifier = modifier,
        ) {
            itemsIndexed(
                items = storesState.stores,
                key = { _, store -> store.store.id.value },
            ) { index, store ->
                Store(
                    store = store,
                    cartItemCount = storesState.cartItemCount,
                    onClick = onStoreClicked,
                )

                if (index < storesState.stores.lastIndex) {
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
    private fun StoresSkeleton(
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
            val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)
            repeat(StoreSkeletonCount) { index ->
                StoreSkeleton(shimmer = shimmer)

                if (index < StoreSkeletonCount - 1) {
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
    private fun Store(
        store: PickupStore,
        cartItemCount: Int,
        onClick: (PickupStore) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            modifier = modifier
                .clickable { onClick(store) }
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = store.store.name,
                    style = UiKitTheme.typography.secondary.light,
                )

                Spacer(modifier = Modifier.height(6.dp))

                val productAvailabilityText = if (store.availableItemCount == cartItemCount) {
                    stringResource(R.string.all_products_are_available)
                } else {
                    pluralStringResource(
                        id = R.plurals.d_out_of_d_products_are_available,
                        count = cartItemCount,
                        store.availableItemCount,
                        cartItemCount,
                    )
                }
                Text(
                    text = productAvailabilityText,
                    style = UiKitTheme.typography.tertiary.regular,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = store.store.address,
                    style = UiKitTheme.typography.tertiary.light,
                )

                if (store.store.schedule != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = store.store.schedule,
                        style = UiKitTheme.typography.tertiary.light,
                    )
                }
            }

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_small_arrow_up_24),
                contentDescription = stringResource(R.string.select_store),
                tint = UiKitTheme.colors.icon.regular.default,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(16.dp)
                    .rotate(degrees = 90f),
            )
        }
    }

    @Composable
    private fun StoreSkeleton(
        shimmer: Shimmer,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier.padding(16.dp)) {
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.secondary.light,
                shimmer = shimmer,
                modifier = Modifier.width(140.dp),
            )
            Spacer(modifier = Modifier.height(12.dp))
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.tertiary.regular,
                shimmer = shimmer,
                modifier = Modifier.width(170.dp),
            )
            Spacer(modifier = Modifier.height(10.dp))
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.tertiary.light,
                shimmer = shimmer,
                modifier = Modifier.width(190.dp),
            )
            Spacer(modifier = Modifier.height(6.dp))
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.tertiary.light,
                shimmer = shimmer,
                modifier = Modifier.width(150.dp),
            )
        }
    }

    private const val StoresContentKeyStores = "StoresContentKeyStores"

    private const val StoreSkeletonCount = 6
}
