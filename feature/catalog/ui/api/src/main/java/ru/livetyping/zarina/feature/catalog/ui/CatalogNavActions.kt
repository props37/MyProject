package ru.livetyping.zarina.feature.catalog.ui

import ru.livetyping.zarina.core.domain.model.category.Category

public class CatalogNavActions(
    public val onBackClicked: () -> Unit,
    public val onCategoryClicked: (Category.Id) -> Unit,
)
