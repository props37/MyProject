package ru.zarina.zarina.ui.screens

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.components.buttons.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.components.buttons.ZarinaTextButton
import ru.zarina.zarina.ui.common.tooling.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.FontScalePreviews
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme
import ru.zarina.zarina.utils.compose.minInteractionSize

@Composable
private fun OnboardingScreenContent(
    onCloseClick: () -> Unit,
) {
    Banner()
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
        CitySelection()
    }
}

@Composable
fun Banner(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
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

@Composable
fun CitySelection(
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
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(20.dp))
        ZarinaTextButton(
            text = stringResource(id = R.string.select_manually),
            onClick = { /*TODO*/ },
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

    OnboardingScreenBehavior(
        sideEffects = viewModel.sideEffects,
        showHome = showHome,
    )

    OnboardingScreenContent(
        onCloseClick = viewModel::onCloseClick,
    )
}

@Composable
fun OnboardingScreenBehavior(
    sideEffects: Flow<OnboardingViewModel.SideEffect>,
    showHome: () -> Unit,
) {
    LaunchedEffect(sideEffects, showHome) {
        sideEffects.collect { effect ->
            when (effect) {
                OnboardingViewModel.SideEffect.ShowHome -> showHome()
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun OnboardingScreenContentPreview() {
    ZarinaTheme {
        OnboardingScreenContent(
            onCloseClick = {},
        )
    }
}
