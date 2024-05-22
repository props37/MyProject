package ru.livetyping.zarina.presentation.screen.productsearch

import ru.livetyping.zarina.usecase.productsearch.GetProductSearchSuggestionsFlowUseCase
import javax.inject.Inject

class ProductSearchInteractor @Inject constructor(
    val getProductSearchSuggestionsFlow: GetProductSearchSuggestionsFlowUseCase,
)
