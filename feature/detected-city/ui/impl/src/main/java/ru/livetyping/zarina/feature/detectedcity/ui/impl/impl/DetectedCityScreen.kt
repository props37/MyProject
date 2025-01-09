package ru.livetyping.zarina.feature.detectedcity.ui.impl.impl

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.dialog.ZarinaDialogContainer
import ru.livetyping.zarina.feature.detectedcity.ui.DetectedCityNavActions
import ru.livetyping.zarina.feature.detectedcity.ui.impl.R
import ru.livetyping.zarina.feature.detectedcity.ui.impl.impl.component.rememberBodyText
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun DetectedCityScreen(
    navActions: DetectedCityNavActions,
    viewModel: DetectedCityViewModel = hiltViewModel(),
) {
    val cityName by viewModel.cityName.collectAsStateWithLifecycle()

    ScreenContent(
        cityName = cityName,
        onCloseClicked = viewModel::onCloseClicked,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
internal fun ScreenContent(
    cityName: String,
    onCloseClicked: () -> Unit,
    sideEffects: Flow<DetectedCitySideEffect>,
    navActions: DetectedCityNavActions,
) {
    DetectedCityScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    ZarinaDialogContainer(
        title = {
            Text(text = stringResource(R.string.detected_city_title))
        },
        body = {
            Text(text = rememberBodyText(cityName))
        },
        buttons = {
            ZarinaButton(
                onClick = onCloseClicked,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(RCommon.string.res_close).uppercase())
            }
        },
    )
}
