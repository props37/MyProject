package ru.zarina.zarina.ui.screens.catalog.selectsort

import org.koin.core.annotation.Factory
import ru.zarina.zarina.ui.screens.catalog.CatalogCoordinator

@Factory
class SelectSortInteractor(
    private val coordinator: CatalogCoordinator,
)
