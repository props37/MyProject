package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uicompose.textString
import ru.livetyping.zarina.core.uikit.bottomsheet.ZarinaModalBottomSheet
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.GenericBottomSheetState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GenericModalBottomSheet(
    state: GenericBottomSheetState,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (state is GenericBottomSheetState.Visible) {
        ZarinaModalBottomSheet(
            onDismissRequest = onClose,
            sheetState = sheetState,
            modifier = modifier,
        ) {
            Column {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = textString(state.body),
                    style = UiKitTheme.typography.secondary.regular,
                    color = UiKitTheme.colors.text.general.regular.default,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )

                if (state.buttonState != null) {
                    val coroutineScope = rememberCoroutineScope()

                    Spacer(modifier = Modifier.height(20.dp))

                    ZarinaButton(
                        onClick = {
                            coroutineScope
                                .launch { sheetState.hide() }
                                .invokeOnCompletion { onClose() }
                        },
                        colors = ZarinaButtonDefaults.outlineColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    ) {
                        Text(text = textString(state.buttonState.text).uppercase())
                    }
                }

                Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
            }
        }
    }
}
