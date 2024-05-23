package ru.livetyping.zarina.presentation.screen.generic.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.base.text.textString
import ru.livetyping.zarina.presentation.common.component.bottomsheet.ZarinaBottomSheet
import ru.livetyping.zarina.presentation.common.component.button.ZarinaCloseIconButton
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.generic.bottomsheet.GenericBottomSheetViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun GenericBottomSheetScreen(
    navigate: (GenericBottomSheetScreenAction) -> Unit,
    viewModel: GenericBottomSheetViewModel = hiltViewModel(),
) {
    val title by viewModel.title.collectAsStateWithLifecycle()
    val body by viewModel.body.collectAsStateWithLifecycle()

    ScreenContent(
        title = title,
        body = body,
        onCloseClicked = viewModel::onCloseClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    title: Text,
    body: Text,
    onCloseClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (GenericBottomSheetScreenAction) -> Unit,
) {
    GenericBottomSheetScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    ZarinaBottomSheet {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = textString(title),
                    style = UiKitTheme.typography.primary.bold,
                    color = UiKitTheme.colors.text.general.regular.default,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                )

                ZarinaCloseIconButton(
                    onClick = onCloseClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(end = 2.dp),
                )
            }

            Text(
                text = textString(body),
                style = UiKitTheme.typography.secondary.regular,
                color = UiKitTheme.colors.text.general.regular.default,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            val navigationBarHeight =
                WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            Spacer(modifier = Modifier.height(navigationBarHeight))
        }
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        ScreenContent(
            title = remember { Text.String("ТРЦ МЕГА Парнас") },
            body = remember {
                Text.String("КАД, 117-й километр, внешнее кольцо, 1\nс 10:00 до 22:00")
            },
            onCloseClicked = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}
