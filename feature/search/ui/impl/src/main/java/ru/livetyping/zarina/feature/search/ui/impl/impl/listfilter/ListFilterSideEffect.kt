package ru.livetyping.zarina.feature.search.ui.impl.impl.listfilter

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface ListFilterSideEffect : SideEffect {
    data class Navigate(val action: ListFilterScreenAction) : ListFilterSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : ListFilterSideEffect
}
