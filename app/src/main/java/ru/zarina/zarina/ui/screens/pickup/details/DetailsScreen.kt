package ru.zarina.zarina.ui.screens.pickup.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.form.SectionHeader
import ru.zarina.zarina.ui.common.components.toolbar.BackButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.screens.pickup.PickupViewModel
import ru.zarina.zarina.ui.theme.ZarinaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreenContent(
    onBackClick: () -> Unit,
) {
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = stringResource(id = R.string.pickup_at_shop),
                startIcon = {
                    BackButton(onClick = onBackClick)
                },
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            RecipientInformation()
        }
    }
}

@Composable
private fun ColumnScope.RecipientInformation() {
    SectionHeader(
        text = stringResource(id = R.string.recipient_information),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun DetailsScreen(
    parentEntry: NavBackStackEntry,
    goBack: () -> Unit,
) {
    val parentViewModel = hiltViewModel<PickupViewModel>(parentEntry)
    val viewModel = hiltViewModel<DetailsViewModel>()

    DetailsScreenBehavior(
        sideEffects = viewModel.sideEffects,
        goBack = goBack,
    )

    DetailsScreenContent(
        onBackClick = viewModel::onBackClick,
    )
}

@Composable
fun DetailsScreenBehavior(
    sideEffects: Flow<DetailsViewModel.SideEffect>,
    goBack: () -> Unit,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                DetailsViewModel.SideEffect.GoBack -> goBack()
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun DetailsScreenContentPreview() {
    ZarinaTheme {
        DetailsScreenContent(
            onBackClick = {},
        )
    }
}
