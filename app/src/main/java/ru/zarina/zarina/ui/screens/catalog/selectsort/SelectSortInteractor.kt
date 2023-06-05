package ru.zarina.zarina.ui.screens.catalog.selectsort

import ru.zarina.zarina.ui.screens.catalog.CatalogCoordinator
import javax.inject.Inject

class SelectSortInteractor @Inject constructor(
    private val coordinator: CatalogCoordinator,
) {

    val sort
        get() = coordinator.sort

}
