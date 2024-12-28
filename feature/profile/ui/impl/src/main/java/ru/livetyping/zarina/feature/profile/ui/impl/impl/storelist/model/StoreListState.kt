package ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

@Stable
internal sealed class StoreListState {
    @Immutable
    data class Success(val stores: ImmutableList<Store>) : StoreListState()

    data object Loading : StoreListState()

    @Immutable
    data class Error(val state: ZarinaErrorScreenState) : StoreListState()
}
