package ru.zarina.zarina.ui.screens.catalog

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.MutableStateFlow
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.ProductSort
import javax.inject.Inject

@ActivityRetainedScoped
class CatalogCoordinator @Inject constructor() {

    val sort = MutableStateFlow(ProductSort.DEFAULT)
    val filtration = MutableStateFlow<Filtration?>(null)

}
