package ru.zarina.zarina.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
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
import timber.log.Timber

@Composable
private fun OnboardingScreenContent(
    step: OnboardingViewModel.OnboardingStep,
    splashState: OnboardingViewModel.SplashState,
    isDetectButtonLoading: Boolean,
    onDetectClick: () -> Unit,
    onSelectManuallyClick: () -> Unit,
    onCloseClick: () -> Unit,
    detectedCity: City?,
    isConfirmDetectedCityButtonLoading: Boolean,
    onConfirmDetectedCity: () -> Unit,
    isSnackbarVisible: Boolean,
    snackbarText: Text,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.screenBackground)
    ) {
        var isBannerLoaded by remember(splashState) { mutableStateOf(false) }
        val splashBannerAlpha by animateFloatAsState(
            targetValue = if (isBannerLoaded) 1f else 0f,
            animationSpec = spring(stiffness = Spring.StiffnessLow),
            label = "banner alpha"
        )

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                SplashBanner(
                    splashState = splashState,
                    onBannerLoaded = { isBannerLoaded = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer { alpha = splashBannerAlpha },
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                ) {
                    CloseButton(
                        onClick = onCloseClick,
                        modifier = Modifier.align(Alignment.End),
                    )
                    val logoColor by animateColorAsState(
                        targetValue = if (isBannerLoaded) Color.White else Color.Black,
                        animationSpec = spring(stiffness = Spring.StiffnessLow),
                        label = "logo color"
                    )
                    Logo(
                        color = logoColor,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            BottomContent(
                step = step,
                isDetectButtonLoading = isDetectButtonLoading,
                onDetectClick = onDetectClick,
                onSelectManuallyClick = onSelectManuallyClick,
                isConfirmDetectedCityButtonLoading = isConfirmDetectedCityButtonLoading,
                detectedCity = detectedCity,
                onConfirmDetectedCity = onConfirmDetectedCity,
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
fun SplashBanner(
    splashState: OnboardingViewModel.SplashState,
    onBannerLoaded: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        LaunchedEffect(splashState) {
            if (splashState is OnboardingViewModel.SplashState.Error) onBannerLoaded()
        }

        var isUrlLoaded by remember(splashState) { mutableStateOf<Boolean?>(null) }
        when {
            splashState is OnboardingViewModel.SplashState.Loading -> Unit
            splashState is OnboardingViewModel.SplashState.Error || isUrlLoaded == false -> Image(
                painter = painterResource(id = R.drawable.onboarding_default_banner),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            splashState is OnboardingViewModel.SplashState.Success -> {
                val context = LocalContext.current
                val model = remember(context, splashState) {
                    ImageRequest.Builder(context)
                        .data(splashState.url.value)
                        .size(Size.ORIGINAL)
                        .build()
                }
                AsyncImage(
                    model = model,
                    onState = { state ->
                        Timber.v("$state")
                        when (state) {
                            is AsyncImagePainter.State.Success -> {
                                onBannerLoaded()
                                isUrlLoaded = true
                            }

                            is AsyncImagePainter.State.Error -> {
                                onBannerLoaded()
                                isUrlLoaded = false
                            }

                            else -> Unit
                        }
                    },
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
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
    color: Color,
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
            colorFilter = ColorFilter.tint(color),
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
    isConfirmDetectedCityButtonLoading: Boolean,
    onConfirmDetectedCity: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentModifier = Modifier
        .fillMaxWidth()
        .background(color = UiKitTheme.colors.screenBackground)
        .navigationBarsPadding()
    val citySelection = @Composable {
        CitySelection(
            isDetectButtonLoading = isDetectButtonLoading,
            onDetectClick = onDetectClick,
            onSelectManuallyClick = onSelectManuallyClick,
            modifier = contentModifier,
        )
    }

    val selectionResult = @Composable {
        SelectionResult(
            city = detectedCity,
            isConfirmButtonLoading = isConfirmDetectedCityButtonLoading,
            onConfirmClick = onConfirmDetectedCity,
            onSelectManuallyClick = onSelectManuallyClick,
            modifier = contentModifier,
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
        modifier = modifier
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
    isConfirmButtonLoading: Boolean,
    onConfirmClick: () -> Unit,
    onSelectManuallyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
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
            isLoading = isConfirmButtonLoading,
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
    showCitySelection: () -> Unit,
) {
    val viewModel = hiltViewModel<OnboardingViewModel>()

    val step by viewModel.step.collectAsStateWithLifecycle()
    val splashState by viewModel.splashState.collectAsStateWithLifecycle()
    val isDetectButtonLoading by viewModel.isDetectButtonLoading.collectAsStateWithLifecycle()
    val detectedCity by viewModel.detectedCity.collectAsStateWithLifecycle()
    val isConfirmDetectedCityButtonLoading by viewModel.isConfirmDetectedCityButtonLoading.collectAsStateWithLifecycle()
    val isSnackbarVisible by viewModel.isSnackbarVisible.collectAsStateWithLifecycle()
    val snackbarText by viewModel.snackbarText.collectAsStateWithLifecycle()

    OnboardingScreenBehavior(
        sideEffects = viewModel.sideEffects,
        showHome = showHome,
        showCitySelection = showCitySelection,
        onLocationPermissionResult = viewModel::onLocationPermissionResult
    )

    OnboardingScreenContent(
        step = step,
        splashState = splashState,
        isDetectButtonLoading = isDetectButtonLoading,
        onDetectClick = viewModel::onDetectClick,
        onSelectManuallyClick = viewModel::onSelectManuallyClick,
        onCloseClick = viewModel::onCloseClick,
        detectedCity = detectedCity,
        isConfirmDetectedCityButtonLoading = isConfirmDetectedCityButtonLoading,
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
    showCitySelection: () -> Unit,
    onLocationPermissionResult: (isGranted: Boolean) -> Unit,
) {
    val locationPermissionState = rememberPermissionState(
        permission = android.Manifest.permission.ACCESS_COARSE_LOCATION,
        onPermissionResult = onLocationPermissionResult
    )
    LaunchedEffect(locationPermissionState, sideEffects, showHome, showCitySelection) {
        sideEffects.collect { effect ->
            when (effect) {
                OnboardingViewModel.SideEffect.ShowHome -> showHome()
                OnboardingViewModel.SideEffect.ShowCitySelection -> showCitySelection()
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
            splashState = OnboardingViewModel.SplashState.Error,
            isDetectButtonLoading = true,
            onDetectClick = {},
            onSelectManuallyClick = {},
            onCloseClick = {},
            detectedCity = city,
            isConfirmDetectedCityButtonLoading = false,
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
            splashState = OnboardingViewModel.SplashState.Error,
            isDetectButtonLoading = true,
            onDetectClick = {},
            onSelectManuallyClick = {},
            onCloseClick = {},
            detectedCity = city,
            isConfirmDetectedCityButtonLoading = false,
            onConfirmDetectedCity = {},
            isSnackbarVisible = true,
            snackbarText = Text.Resource(R.string.cant_detect_city),
        )
    }
}
