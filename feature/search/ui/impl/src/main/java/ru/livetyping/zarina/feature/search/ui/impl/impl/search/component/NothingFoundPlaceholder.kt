package ru.livetyping.zarina.feature.search.ui.impl.impl.search.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.error.rememberZarinaErrorButtonState
import ru.livetyping.zarina.core.uikit.error.rememberZarinaErrorScreenState
import ru.livetyping.zarina.feature.search.ui.impl.R
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun NothingFoundPlaceholder(
    modifier: Modifier = Modifier,
) {
    val errorState = rememberZarinaErrorScreenState(
        iconResId = RCommon.drawable.ic_magnifying_glass_64,
        title = stringResource(R.string.search_nothing_found),
        body = stringResource(R.string.search_try_another_query),
        buttonState = rememberZarinaErrorButtonState(isButtonVisible = false),
    )
    ZarinaErrorScreen(
        state = errorState,
        onButtonClicked = {},
        modifier = modifier,
    )
}
