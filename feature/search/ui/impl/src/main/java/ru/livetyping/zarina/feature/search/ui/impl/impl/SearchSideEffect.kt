package ru.livetyping.zarina.feature.search.ui.impl.impl

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface SearchSideEffect : SideEffect {
    data class Navigate(val action: SearchScreenAction) : SearchSideEffect

    data object ClearSearchBarTextFieldFocus : SearchSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : SearchSideEffect
}
