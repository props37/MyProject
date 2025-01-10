package ru.livetyping.zarina.feature.home.domain.model

import ru.livetyping.zarina.core.domain.model.category.Category

public sealed interface ClickAction {
    public data class OpenProductList(val categoryId: Category.Id) : ClickAction
}
