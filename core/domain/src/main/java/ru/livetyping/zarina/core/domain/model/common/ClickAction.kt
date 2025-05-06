package ru.livetyping.zarina.core.domain.model.common

import ru.livetyping.zarina.core.domain.model.category.Category

public sealed interface ClickAction {
    public data class OpenProductList(val categoryId: Category.Id) : ClickAction

    public data class OpenUrl(val url: Url) : ClickAction
}
