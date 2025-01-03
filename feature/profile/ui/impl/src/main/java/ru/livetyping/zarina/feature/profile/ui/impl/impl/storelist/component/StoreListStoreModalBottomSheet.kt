package ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.uikit.bottomsheet.ZarinaModalBottomSheet
import ru.livetyping.zarina.core.uikit.button.ZarinaCloseIconButton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StoreListStoreModalBottomSheet(
    store: Store,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
    val coroutineScope = rememberCoroutineScope()

    ZarinaModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier,
    ) {
        Column {
            Spacer(modifier = Modifier.height(8.dp))

            ZarinaTopBar(
                startContent = {
                    Text(
                        text = store.name,
                        style = UiKitTheme.typography.primary.bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                endContent = {
                    ZarinaCloseIconButton(
                        onClick = {
                            coroutineScope
                                .launch { sheetState.hide() }
                                .invokeOnCompletion { onDismissRequest() }
                        },
                        iconSize = 20.dp,
                        modifier = Modifier.padding(end = 2.dp),
                    )
                },
                contentPadding = PaddingValues(vertical = 4.dp),
            )

            Text(
                text = remember(store) { getStoreDescription(store) },
                style = UiKitTheme.typography.secondary.regular,
                color = UiKitTheme.colors.text.general.regular.default,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

private fun getStoreDescription(store: Store): String {
    return buildString {
        append(store.address)
        if (!store.schedule.isNullOrBlank()) {
            append(NEW_LINE)
            append(store.schedule)
        }
        if (!store.phone?.value.isNullOrBlank()) {
            append(NEW_LINE)
            append(store.phone?.value)
        }
    }
}

private const val NEW_LINE = "\n"
