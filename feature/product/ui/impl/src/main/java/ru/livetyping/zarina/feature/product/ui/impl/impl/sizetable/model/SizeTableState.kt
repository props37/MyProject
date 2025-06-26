package ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.model

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.domain.model.product.SizeGuide
import ru.livetyping.zarina.core.uimodel.tab.TabRowState

@Immutable
internal data class SizeTableState(
    val viewModeSelectorState: TabRowState<ViewMode>,
    val sizeGuide: SizeGuide,
)
