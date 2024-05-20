package ru.livetyping.zarina.data.productsearch.remote

import ru.livetyping.zarina.data.productsearch.remote.api.ProductSearchApi
import ru.livetyping.zarina.domain.productsearch.ProductSearchSuggestions
import javax.inject.Inject

class ProductSearchRemoteDataSource @Inject constructor(
    private val api: ProductSearchApi,
) {
    suspend fun getSearchSuggestions(query: String): ProductSearchSuggestions {
        return api.getSearchSuggestions(query).toProductSearchSuggestions()
    }
}
