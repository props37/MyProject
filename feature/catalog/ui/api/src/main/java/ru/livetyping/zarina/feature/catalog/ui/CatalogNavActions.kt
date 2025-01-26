package ru.livetyping.zarina.feature.catalog.ui

import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.navigation.NavigationActions

public class CatalogNavActions(
    public val onBackClicked: () -> Unit,
    public val onCategoryClicked: (Category.Id) -> Unit,
) : NavigationActions
