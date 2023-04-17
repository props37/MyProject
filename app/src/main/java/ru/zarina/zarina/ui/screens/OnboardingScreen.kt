package ru.zarina.zarina.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.common.components.StateSnackbar
import ru.zarina.zarina.ui.common.components.StateSnackbarDefaults
import ru.zarina.zarina.ui.common.components.buttons.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.components.buttons.ZarinaTextButton
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.providers.domain.CityProvider
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme
import ru.zarina.zarina.utils.compose.minInteractionSize

@Composable
private fun OnboardingScreenContent(
    step: OnboardingViewModel.OnboardingStep,
    isDetectButtonLoading: Boolean,
    onDetectClick: () -> Unit,
    onSelectManuallyClick: () -> Unit,
    onCloseClick: () -> Unit,
    detectedCity: City?,
    onConfirmDetectedCity: () -> Unit,
    isSnackbarVisible: Boolean,
    snackbarText: Text,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Banner(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
        ) {
            CloseButton(
                onClick = onCloseClick,
                modifier = Modifier.align(Alignment.End),
            )
            Logo(
                modifier = Modifier.weight(1f),
            )
            BottomContent(
                step = step,
                isDetectButtonLoading = isDetectButtonLoading,
                onDetectClick = onDetectClick,
                onSelectManuallyClick = onSelectManuallyClick,
                detectedCity = detectedCity,
                onConfirmDetectedCity = onConfirmDetectedCity
            )
        }

        StateSnackbar(
            isVisible = isSnackbarVisible,
            text = snackbarText,
            enter = slideInVertically(StateSnackbarDefaults.slideAnimationSpec) { -it * 2 },
            exit = slideOutVertically(StateSnackbarDefaults.slideAnimationSpec) { -it * 2 },
            modifier = Modifier
                .systemBarsPadding()
                .align(Alignment.TopCenter)
                .fillMaxWidth()
        )
    }
}

@Composable
fun Banner(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.onboarding_default_banner),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth(),
        )
        val gradientBrush = Brush.verticalGradient(
            0f to Color.Transparent,
            1f to Color.Black.copy(alpha = 0.5f),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBrush)
        )
    }
    // TODO load current banner from backend
}

@Composable
fun CloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(end = 8.dp)
            .minInteractionSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_close_24),
            contentDescription = stringResource(id = R.string.skip)
        )
    }
}

@Composable
fun Logo(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = BiasAlignment(0f, 0.5f),
        modifier = modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = modifier.fillMaxWidth(0.65f)
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomContent(
    step: OnboardingViewModel.OnboardingStep,
    isDetectButtonLoading: Boolean,
    onDetectClick: () -> Unit,
    onSelectManuallyClick: () -> Unit,
    detectedCity: City?,
    onConfirmDetectedCity: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val citySelection = @Composable {
        CitySelection(
            isDetectButtonLoading = isDetectButtonLoading,
            onDetectClick = onDetectClick,
            onSelectManuallyClick = onSelectManuallyClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }

    val selectionResult = @Composable {
        SelectionResult(
            city = detectedCity,
            onConfirmClick = onConfirmDetectedCity,
            onSelectManuallyClick = onSelectManuallyClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }

    SubcomposeLayout(
        modifier = modifier,
    ) { constraints ->
        val mainPlaceables = subcompose("main", citySelection).map {
            it.measure(constraints)
        }
        val maxSize = mainPlaceables.fold(IntSize.Zero) { currentMax, placeable ->
            IntSize(
                width = maxOf(currentMax.width, placeable.width),
                height = maxOf(currentMax.height, placeable.height)
            )
        }
        val reducedConstraints = constraints.copy(maxHeight = maxSize.height)
        layout(maxSize.width, maxSize.height) {
            subcompose("dependent") {
                AnimatedContent(
                    targetState = step,
                    label = "step",
                    transitionSpec = {
                        fadeIn() + slideInHorizontally { it } with fadeOut() + slideOutHorizontally { -it }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(UiKitTheme.colors.screenBackground)
                ) { step ->
                    when (step) {
                        OnboardingViewModel.OnboardingStep.CITY_SELECTION_TYPE -> citySelection()
                        OnboardingViewModel.OnboardingStep.DETECTION_RESULT -> selectionResult()
                    }
                }
            }.forEach {
                it.measure(reducedConstraints).placeRelative(0, 0)
            }
        }
    }
}


@Composable
fun CitySelection(
    isDetectButtonLoading: Boolean,
    onDetectClick: () -> Unit,
    onSelectManuallyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.background(UiKitTheme.colors.screenBackground)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(R.string.select_your_city),
            style = UiKitTheme.typography.onboardingHeader,
            color = UiKitTheme.colors.primaryContentColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.to_see_in_your_city),
            style = UiKitTheme.typography.onboardingBody,
            color = UiKitTheme.colors.primaryContentColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(40.dp))
        ZarinaTextButton(
            text = stringResource(id = R.string.select_automatically),
            onClick = onDetectClick,
            isLoading = isDetectButtonLoading,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(20.dp))
        ZarinaTextButton(
            text = stringResource(id = R.string.select_manually),
            onClick = onSelectManuallyClick,
            colors = ZarinaButtonDefaults.secondaryColors(),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SelectionResult(
    city: City?,
    onConfirmClick: () -> Unit,
    onSelectManuallyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.background(UiKitTheme.colors.screenBackground)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.is_your_city, city?.name.orEmpty()),
            style = UiKitTheme.typography.onboardingHeader,
            color = UiKitTheme.colors.primaryContentColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(40.dp))
        ZarinaTextButton(
            text = stringResource(id = R.string.yes_correct),
            onClick = onConfirmClick,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(20.dp))
        ZarinaTextButton(
            text = stringResource(id = R.string.no_change),
            onClick = onSelectManuallyClick,
            colors = ZarinaButtonDefaults.secondaryColors(),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun OnboardingScreen(
    showHome: () -> Unit,
) {
    val viewModel = hiltViewModel<OnboardingViewModel>()

    val step by viewModel.step.collectAsStateWithLifecycle()
    val isDetectButtonLoading by viewModel.isDetectButtonLoading.collectAsStateWithLifecycle()
    val isSnackbarVisible by viewModel.isSnackbarVisible.collectAsStateWithLifecycle()
    val detectedCity by viewModel.detectedCity.collectAsStateWithLifecycle()
    val snackbarText by viewModel.snackbarText.collectAsStateWithLifecycle()

    OnboardingScreenBehavior(
        sideEffects = viewModel.sideEffects,
        showHome = showHome,
        onLocationPermissionResult = viewModel::onLocationPermissionResult,
    )

    OnboardingScreenContent(
        step = step,
        isDetectButtonLoading = isDetectButtonLoading,
        onDetectClick = viewModel::onDetectClick,
        onSelectManuallyClick = viewModel::onSelectManuallyClick,
        onCloseClick = viewModel::onCloseClick,
        detectedCity = detectedCity,
        onConfirmDetectedCity = viewModel::onConfirmDetectedCityClick,
        isSnackbarVisible = isSnackbarVisible,
        snackbarText = snackbarText,
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun OnboardingScreenBehavior(
    sideEffects: Flow<OnboardingViewModel.SideEffect>,
    showHome: () -> Unit,
    onLocationPermissionResult: (isGranted: Boolean) -> Unit,
) {
    val locationPermissionState = rememberPermissionState(
        permission = android.Manifest.permission.ACCESS_COARSE_LOCATION,
        onPermissionResult = onLocationPermissionResult
    )
    LaunchedEffect(locationPermissionState, sideEffects, showHome) {
        sideEffects.collect { effect ->
            when (effect) {
                OnboardingViewModel.SideEffect.ShowHome -> showHome()
                OnboardingViewModel.SideEffect.RequestLocationPermission -> locationPermissionState.launchPermissionRequest()
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun OnboardingScreenContentPreview(
    @PreviewParameter(CityProvider::class, limit = 1)
    city: City,
) {
    ZarinaTheme {
        OnboardingScreenContent(
            step = OnboardingViewModel.OnboardingStep.CITY_SELECTION_TYPE,
            isDetectButtonLoading = true,
            onDetectClick = {},
            onSelectManuallyClick = {},
            onCloseClick = {},
            detectedCity = city,
            onConfirmDetectedCity = {},
            isSnackbarVisible = true,
            snackbarText = Text.Resource(R.string.cant_detect_city),
        )
    }
}

@Preview
@Composable
fun OnboardingScreenDetectionResultContentPreview(
    @PreviewParameter(CityProvider::class, limit = 1)
    city: City,
) {
    ZarinaTheme {
        OnboardingScreenContent(
            step = OnboardingViewModel.OnboardingStep.DETECTION_RESULT,
            isDetectButtonLoading = true,
            onDetectClick = {},
            onSelectManuallyClick = {},
            onCloseClick = {},
            detectedCity = city,
            onConfirmDetectedCity = {},
            isSnackbarVisible = true,
            snackbarText = Text.Resource(R.string.cant_detect_city),
        )
    }
}
