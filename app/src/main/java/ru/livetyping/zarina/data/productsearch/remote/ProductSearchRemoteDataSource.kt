package ru.livetyping.zarina.data.productsearch.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.data.productsearch.remote.api.ProductSearchApi
import ru.livetyping.zarina.domain.productsearch.ProductSearchSuggestions
import javax.inject.Inject

class ProductSearchRemoteDataSource @Inject constructor(
    private val api: ProductSearchApi,
) {
    fun getSearchSuggestionsFlow(query: String): Flow<ProductSearchSuggestions> = flow {
        val suggestions = api.getSearchSuggestions(query).toProductSearchSuggestions()
        emit(suggestions)
    }
}
