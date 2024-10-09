package ru.livetyping.zarina.feature.home.domain

import ru.livetyping.zarina.core.domain.category.Category

public sealed interface ClickAction {
    public data class Products(val categoryId: Category.Id) : ClickAction
}
