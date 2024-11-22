package ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.livetyping.zarina.core.uikit.tab.ZarinaTab
import ru.livetyping.zarina.core.uikit.tab.ZarinaTabRow
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.signin.ui.impl.R
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.model.SignInType

@Composable
internal fun SignInTypeSelector(
    state: TabRowState<SignInType>,
    onEvent: (TabRowEvent<SignInType>) -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTabRow(
        selectedTabIndex = state.currentTabIndex,
        modifier = modifier,
    ) {
        state.tabs.forEach { signInType ->
            key(signInType) {
                val textResId = when (signInType) {
                    SignInType.EMAIL -> R.string.sign_in_by_email
                    SignInType.PHONE -> R.string.sign_in_by_phone
                }
                val isSelected = signInType == state.currentTab

                ZarinaTab(
                    text = stringResource(textResId),
                    onClick = {
                        if (!isSelected) {
                            onEvent(TabRowEvent.TabChanged(signInType))
                        } else {
                            onEvent(TabRowEvent.TabReselected(signInType))
                        }
                    },
                    isSelected = isSelected,
                )
            }
        }
    }
}
