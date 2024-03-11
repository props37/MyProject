package ru.zarina.zarina.ui.screen.defaultcitydialog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.geography.City
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.dialog.ZarinaDialogContainer
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.defaultcitydialog.DefaultCityDialogScreenComponents.bodyText
import ru.zarina.zarina.ui.screen.defaultcitydialog.DefaultCityDialogViewModel.SideEffect
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun DefaultCityDialogScreen(
    navigate: (DefaultCityDialogScreenAction) -> Unit,
    viewModel: DefaultCityDialogViewModel = hiltViewModel(),
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
    navigate: (DefaultCityDialogScreenAction) -> Unit,
) {
    DefaultCityDialogScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    ZarinaDialogContainer {
        Text(
            text = stringResource(R.string.default_city_dialog_title),
            style = UiKitTheme.typographyReworked.primary.bold,
            color = UiKitTheme.colorsReworked.text.general.regular.default,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = bodyText(defaultCity),
            style = UiKitTheme.typographyReworked.secondary.regular,
            color = UiKitTheme.colorsReworked.text.general.regular.default,
        )

        Spacer(modifier = Modifier.height(20.dp))

        ZarinaButton(
            onClick = onCloseClicked,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.close).uppercase())
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
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

