package ru.livetyping.zarina.feature.home.domain.model

import ru.livetyping.zarina.core.domain.model.category.Category

public sealed interface ClickAction {
    public data class Products(val categoryId: Category.Id) : ClickAction
}
