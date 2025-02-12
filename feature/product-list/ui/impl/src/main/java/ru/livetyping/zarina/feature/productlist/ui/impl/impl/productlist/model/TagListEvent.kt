package ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.model

import ru.livetyping.zarina.core.domain.model.category.Category

internal sealed interface TagListEvent {
    data class TagClicked(val tag: Category) : TagListEvent
}
