package ru.zarina.zarina.ui.screens.catalog

import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.annotation.Singleton
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.ProductSort

@Singleton
class CatalogCoordinator {

    val sort = MutableStateFlow(ProductSort.DEFAULT)
    val filtration = MutableStateFlow<Filtration?>(null)

}
