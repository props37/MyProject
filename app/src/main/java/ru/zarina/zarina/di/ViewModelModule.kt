package ru.zarina.zarina.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.zarina.zarina.ui.screens.catalog.filters.FiltersViewModel
import ru.zarina.zarina.ui.screens.catalog.filters.list.ListFilterViewModel
import ru.zarina.zarina.ui.screens.catalog.products.ProductsViewModel
import ru.zarina.zarina.ui.screens.catalog.selectsort.SelectSortViewModel

val viewModelModule = module {
    viewModel { parameters ->
        ProductsViewModel(parameters[0], get())
    }
    viewModel { parameters ->
        SelectSortViewModel(parameters[0])
    }
    viewModel { parameters ->
        FiltersViewModel(parameters[0], parameters[1], get())
    }
    viewModel { parameters ->
        ListFilterViewModel(get(), parameters[0])
    }
}
