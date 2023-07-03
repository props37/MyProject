package ru.zarina.zarina.ui.screens.catalog

import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.annotation.Singleton
import ru.zarina.zarina.domain.Filtration

@Singleton
class CatalogCoordinator {

    val filtration = MutableStateFlow<Filtration?>(null)

}
