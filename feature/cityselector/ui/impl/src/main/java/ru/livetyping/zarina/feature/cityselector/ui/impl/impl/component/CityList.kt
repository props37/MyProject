package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.component

import android.os.Parcelable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uicompose.plus
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cityselector.ui.impl.R
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CityListItem
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CityListState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun CityList(
    cityListState: CityListState,
    onCityClicked: (City) -> Unit,
    onChangeCityClicked: () -> Unit,
    onCityListErrorRefreshClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = cityListState,
        contentKey = {
            when (it) {
                is CityListState.Success, CityListState.Empty -> CityListContentKeySuccess
                CityListState.Loading -> it
                is CityListState.Error -> it
            }
        },
        modifier = modifier,
    ) { state ->
        when (state) {
            is CityListState.Success -> {
                CityListSuccess(
                    state = state,
                    onCityClicked = onCityClicked,
                    onChangeCityClicked = onChangeCityClicked,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            CityListState.Empty -> {
                CityListEmpty(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding()
                        .padding(horizontal = 24.dp),
                )
            }

            CityListState.Loading -> {
                CityListLoading()
            }

            is CityListState.Error -> {
                ZarinaErrorScreen(
                    state = state.state,
                    onButtonClicked = onCityListErrorRefreshClicked,
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
private fun CityListSuccess(
    state: CityListState.Success,
    onCityClicked: (City) -> Unit,
    onChangeCityClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        CityListSuccessList(
            state = state,
            onCityClicked = onCityClicked,
            modifier = Modifier.fillMaxSize(),
        )

        CityListSuccessChangeCityButton(
            isVisible = state.isChangeCityButtonVisible,
            onClick = onChangeCityClicked,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun CityListSuccessList(
    state: CityListState.Success,
    onCityClicked: (City) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isChangeCityButtonVisible = state.isChangeCityButtonVisible
    val baseContentPadding = remember(isChangeCityButtonVisible) {
        val bottomBase = ZarinaScrollableDefaults.ScrollableBottomPadding
        val bottom = if (isChangeCityButtonVisible) {
            val buttonHeight = ZarinaButtonDefaults.SizeLarge
            buttonHeight + ChangeCityButtonBottomPadding + 16.dp
        } else {
            0.dp
        }
        PaddingValues(top = 8.dp, bottom = bottom + bottomBase)
    }
    val contentPadding = WindowInsets.safeDrawing
        .only(WindowInsetsSides.Bottom)
        .asPaddingValues()
        .plus(baseContentPadding, LocalLayoutDirection.current)

    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        itemsIndexed(
            items = state.items,
            key = { _, item -> getCityListSuccessKey(item) },
            contentType = { _, item -> getCityListSuccessContentType(item) },
        ) { index, item ->
            when (item) {
                is CityListItem.CityItem -> {
                    Column(modifier = Modifier.animateZarinaItem(this)) {
                        CityItem(
                            item = item,
                            isCitySelected = item.city.id == state.selectedCity?.id,
                            onCityClicked = onCityClicked,
                        )

                        if (index < state.items.lastIndex) {
                            ZarinaDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                            )
                        }
                    }
                }

                is CityListItem.CityFirstLetterHeaderItem -> {
                    CityFirstLetterHeaderItem(
                        item = item,
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .animateZarinaItem(this),
                    )
                }
            }
        }
    }
}

@Composable
private fun CityListSuccessChangeCityButton(
    isVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets.navigationBars.union(WindowInsets.ime),
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = remember { slideInVertically { it } },
        exit = remember { slideOutVertically { it } },
        modifier = modifier,
    ) {
        ZarinaButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets)
                .padding(horizontal = 16.dp)
                .padding(bottom = ChangeCityButtonBottomPadding),
        ) {
            Text(text = stringResource(RCommon.string.change).uppercase())
        }
    }
}

@Composable
private fun CityListEmpty(
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.city_not_found),
            style = UiKitTheme.typography.primary.bold,
            color = UiKitTheme.colors.text.general.regular.default,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.try_to_change_city_name),
            style = UiKitTheme.typography.secondary.regular,
            color = UiKitTheme.colors.text.general.regular.default,
            textAlign = TextAlign.Center,
        )
    }
}

@Suppress("MagicNumber")
@Composable
private fun CityListLoading(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        repeat(CityListLoadingSkeletonCount) { index ->
            val skeletonWidthFraction = when (index % 4) {
                0 -> 0.8f
                1 -> 0.75f
                2 -> 0.7f
                else -> 0.65f
            }

            CityItemSkeleton(skeletonWidthFraction = skeletonWidthFraction)

            if (index < CityListLoadingSkeletonCount - 1) {
                ZarinaDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
            }
        }

        val safeDrawingBottomPadding =
            WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
        val scrollableBottomPadding = ZarinaScrollableDefaults.ScrollableBottomPadding
        Spacer(modifier = Modifier.height(safeDrawingBottomPadding + scrollableBottomPadding))
    }
}

private fun getCityListSuccessKey(item: CityListItem): CityListSuccessKey {
    return when (item) {
        is CityListItem.CityItem -> CityListSuccessKey.CityItem(item.city.id.value)
        is CityListItem.CityFirstLetterHeaderItem -> {
            CityListSuccessKey.CityFirstLetterHeaderItem(item.letter)
        }
    }
}

private fun getCityListSuccessContentType(item: CityListItem): CityListSuccessContentType {
    return when (item) {
        is CityListItem.CityItem -> CityListSuccessContentType.CityItem
        is CityListItem.CityFirstLetterHeaderItem -> {
            CityListSuccessContentType.CityFirstLetterHeaderItem
        }
    }
}

private val ChangeCityButtonBottomPadding: Dp get() = 16.dp

private const val CityListLoadingSkeletonCount = 20

private data object CityListContentKeySuccess

private sealed class CityListSuccessKey : Parcelable {
    @Parcelize
    data class CityItem(val id: String) : CityListSuccessKey()

    @Parcelize
    data class CityFirstLetterHeaderItem(val char: Char) : CityListSuccessKey()
}

private enum class CityListSuccessContentType {
    CityItem,
    CityFirstLetterHeaderItem,
}
