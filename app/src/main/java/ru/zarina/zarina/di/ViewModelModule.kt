package ru.zarina.zarina.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.zarina.zarina.ui.screens.catalog.filters.FiltersViewModel
import ru.zarina.zarina.ui.screens.catalog.filters.list.ListFilterViewModel
import ru.zarina.zarina.ui.screens.catalog.products.ProductsViewModel
import ru.zarina.zarina.ui.screens.catalog.selectcity.SelectCityViewModel
import ru.zarina.zarina.ui.screens.catalog.selectshop.SelectShopViewModel
import ru.zarina.zarina.ui.screens.catalog.selectsort.SelectSortViewModel
import ru.zarina.zarina.ui.screens.search.SearchViewModel

val viewModelModule = module {
    viewModel { parameters ->
        ProductsViewModel(
            savedStateHandle = parameters[0],
            interactor = get()
        )
    }
    viewModel { parameters ->
        SelectSortViewModel(
            productSavedStateHandle = parameters[0]
        )
    }
    viewModel { parameters ->
        FiltersViewModel(
            savedStateHandle = parameters[0],
            productsSavedStateHandle = parameters[1],
            interactor = get()
        )
    }
    viewModel { parameters ->
        ListFilterViewModel(
            savedStateHandle = get(),
            parentSavedStateHandle = parameters[0]
        )
    }
    viewModel { parameters ->
        SelectShopViewModel(
            savedStateHandle = parameters[0],
            filtersSavedStateHandle = parameters[1],
            interactor = get()
        )
    }
    viewModel { parameters ->
        SelectCityViewModel(
            savedStateHandle = get(),
            selectShopSavedStateHandle = parameters[0],
            interactor = get(),
            selectCityComponent = get(),
        )
    }
    viewModel { parameters ->
        SearchViewModel(
            savedStateHandle = parameters[0],
            interactor = get()
        )
    }
}
