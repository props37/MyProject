package ru.livetyping.zarina.ui.screen.onboarding.defaultcity

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.ui.common.component.button.ZarinaButton
import ru.livetyping.zarina.ui.common.component.dialog.ZarinaDialogContainer
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.screen.onboarding.defaultcity.DefaultCityScreenComponents.bodyText
import ru.livetyping.zarina.ui.screen.onboarding.defaultcity.DefaultCityViewModel.SideEffect

@Composable
fun DefaultCityDialogScreen(
    navigate: (DefaultCityScreenAction) -> Unit,
    viewModel: DefaultCityViewModel = hiltViewModel(),
) {
    val defaultCity by viewModel.defaultCity.collectAsStateWithLifecycle()

    ScreenContent(
        defaultCity = defaultCity,
        onCloseClicked = viewModel::onCloseClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    defaultCity: City,
    onCloseClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (DefaultCityScreenAction) -> Unit,
) {
    DefaultCityScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    ZarinaDialogContainer(
        title = {
            Text(text = stringResource(R.string.default_city_dialog_title))
        },
        body = {
            Text(text = bodyText(defaultCity))
        },
        buttons = {
            ZarinaButton(
                onClick = onCloseClicked,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.close).uppercase())
            }
        },
    )
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        ScreenContent(
            defaultCity = City.DEFAULT,
            onCloseClicked = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}

