package ru.livetyping.zarina.core.uicomponent.permissionrequired

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uicomponent.R
import ru.livetyping.zarina.core.uicompose.textString
import ru.livetyping.zarina.core.uikit.bottomsheet.ZarinaModalBottomSheet
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaCloseIconButton
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun PermissionRequiredModalBottomSheet(
    state: PermissionRequiredDialogState,
    onEvent: (PermissionRequiredDialogEvent) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
    if (state is PermissionRequiredDialogState.PermissionRequired) {
        val coroutineScope = rememberCoroutineScope()

        ZarinaModalBottomSheet(
            onDismissRequest = { onEvent(PermissionRequiredDialogEvent.CloseClicked) },
            sheetState = sheetState,
            modifier = modifier,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = textString(state.title),
                        style = UiKitTheme.typography.primary.bold,
                        color = UiKitTheme.colors.text.general.regular.default,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp),
                    )

                    ZarinaCloseIconButton(
                        onClick = {
                            coroutineScope
                                .launch { sheetState.hide() }
                                .invokeOnCompletion {
                                    onEvent(PermissionRequiredDialogEvent.CloseClicked)
                                }
                        },
                        iconSize = 20.dp,
                        modifier = Modifier.padding(end = 2.dp),
                    )
                }

                Text(
                    text = textString(state.body),
                    style = UiKitTheme.typography.secondary.regular,
                    color = UiKitTheme.colors.text.general.regular.default,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )

                Spacer(modifier = Modifier.height(20.dp))

                ZarinaButton(
                    onClick = {
                        coroutineScope
                            .launch { sheetState.hide() }
                            .invokeOnCompletion {
                                onEvent(PermissionRequiredDialogEvent.GoToSettingsClicked(state.permission))
                            }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Text(text = stringResource(R.string.uicomponent_to_settings).uppercase())
                }

                Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
            }
        }
    }
}
