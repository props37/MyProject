package ru.livetyping.zarina.data.old.search.remote

import ru.livetyping.zarina.domain.old.FilteredProducts
import ru.livetyping.zarina.domain.old.Filtration
import ru.livetyping.zarina.domain.old.Page
import ru.livetyping.zarina.domain.old.ProductSort
import ru.livetyping.zarina.domain.old.SearchAutocomplete

interface ISearchRemoteSource {
    suspend fun getAutocomplete(query: String): SearchAutocomplete
    suspend fun getSearchPage(
        query: String,
        sort: ProductSort,
        filtration: Filtration?,
        pageIndex: Int,
    ): Page<FilteredProducts>
}
