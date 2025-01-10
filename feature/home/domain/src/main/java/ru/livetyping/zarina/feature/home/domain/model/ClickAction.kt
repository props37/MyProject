package ru.livetyping.zarina.feature.home.domain.model

import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.common.Url

public sealed interface ClickAction {
    public data class OpenProductList(val categoryId: Category.Id) : ClickAction

    public data class OpenUrl(val url: Url) : ClickAction
}
