package ru.zarina.zarina.ui.screen.cityselector

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.component.ZarinaBottomSheet
import ru.zarina.zarina.ui.common.component.ZarinaCircularLoader
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorScreenComponents.City
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorScreenComponents.CityFirstLetterHeader
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorViewModel.CityListItem
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorViewModel.CityListState
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorViewModel.SideEffect
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun CitySelectorBottomSheetScreen(
    navigateBackward: (CitySelectorScreenResult) -> Unit,
    viewModel: CitySelectorViewModel = hiltViewModel(),
) {
    val cityListState by viewModel.cityListState.collectAsStateWithLifecycle()

    ScreenContent(
        cityListState = cityListState,
        sideEffects = viewModel.sideEffects,
        navigateBackward = navigateBackward,
        onCloseClicked = viewModel::onCloseClicked,
    )
}

@Composable
private fun ScreenContent(
    cityListState: CityListState,
    sideEffects: Flow<SideEffect>,
    navigateBackward: (CitySelectorScreenResult) -> Unit,
    onCloseClicked: () -> Unit,
) {
    CitySelectorScreenBehavior(
        sideEffects = sideEffects,
        navigateBackward = navigateBackward,
    )

    ZarinaBottomSheet(windowInsets = WindowInsets.statusBars) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(onCloseClicked = onCloseClicked)

            // TODO: [High] Extract to Components
            Crossfade(
                targetState = cityListState,
                label = "CitySelectorScreen content",
            ) { cityListState ->
                when (cityListState) {
                    CityListState.InitialLoading -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            ZarinaCircularLoader(
                                color = UiKitTheme.colorsReworked.icon.regular.default,
                                modifier = Modifier.size(40.dp),
                            )
                        }
                    }

                    is CityListState.CityList -> {
                        LazyColumn {
                            items(
                                items = cityListState.list,
                                key = { item ->
                                    when (item) {
                                        is CityListItem.City -> item.city.kladrId.value
                                        is CityListItem.CityFirstLetterHeader -> item.letter
                                    }
                                },
                            ) { item ->
                                when (item) {
                                    is CityListItem.City -> {
                                        City(
                                            city = item.city,
                                            onClick = {},
                                            modifier = Modifier.fillMaxWidth(),
                                        )
                                    }

                                    is CityListItem.CityFirstLetterHeader -> {
                                        CityFirstLetterHeader(item.letter)
                                    }
                                }
                            }
                        }
                    }

                    is CityListState.Error -> {
                        // TODO: [High] Implement
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    // TODO: [High] Add preview
}
