package ru.livetyping.zarina.presentation.common.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.component.button.ZarinaCloseIconButton
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZarinaClubModalBottomSheet(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    onUrlClicked: (Url) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
    val coroutineScope = rememberCoroutineScope()

    if (isVisible) {
        ZarinaModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            modifier = modifier,
        ) {
            ZarinaClubModalBottomSheetContent(
                onCloseClicked = {
                    coroutineScope
                        .launch { sheetState.hide() }
                        .invokeOnCompletion { onDismissRequest() }
                },
                onLearnMoreClicked = onUrlClicked,
            )
        }
    }
}

@Composable
private fun ZarinaClubModalBottomSheetContent(
    onCloseClicked: () -> Unit,
    onLearnMoreClicked: (Url) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = BackgroundColor,
) {
    Column(modifier = modifier.background(backgroundColor)) {
        ZarinaTopBar(
            backgroundColor = backgroundColor,
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 4.dp,
                end = 2.dp,
                bottom = 4.dp,
            ),
        ) {
            Text(
                text = stringResource(R.string.for_zarina_club_members),
                style = UiKitTheme.typography.primary.bold,
                color = UiKitTheme.colors.text.general.regular.default,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
            )

            Spacer(modifier = Modifier.width(8.dp))

            ZarinaCloseIconButton(
                onClick = onCloseClicked,
                iconSize = 20.dp,
            )
        }

        Text(
            text = stringResource(R.string.zarina_club_program_description_1),
            style = UiKitTheme.typography.secondary.regular,
            color = UiKitTheme.colors.text.general.regular.default,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.zarina_club_program_description_2),
            style = UiKitTheme.typography.secondary.regular,
            color = UiKitTheme.colors.text.general.regular.default,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(20.dp))

        val learnMoreUrlText = stringResource(R.string.loyalty_policy_url)
        ZarinaButton(
            onClick = { onLearnMoreClicked(Url(learnMoreUrlText)) },
            colors = ZarinaButtonDefaults.outlineColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
        ) {
            Text(text = stringResource(R.string.learn_more).uppercase())
        }
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        ZarinaClubModalBottomSheetContent(
            onCloseClicked = {},
            onLearnMoreClicked = {},
        )
    }
}

private val BackgroundColor: Color
    @Composable
    get() = UiKitTheme.colors.background.general.regular.default
