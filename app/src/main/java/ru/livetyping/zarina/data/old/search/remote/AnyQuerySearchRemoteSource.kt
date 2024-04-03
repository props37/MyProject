package ru.livetyping.zarina.data.old.search.remote

import org.koin.core.annotation.Factory
import ru.livetyping.zarina.data.old.search.remote.api.IAnyQuerySearchApi
import ru.livetyping.zarina.data.old.search.remote.api.dto.FacetDto
import ru.livetyping.zarina.data.old.search.remote.api.dto.SortDto
import ru.livetyping.zarina.domain.old.FilteredProducts
import ru.livetyping.zarina.domain.old.Filtration
import ru.livetyping.zarina.domain.old.Page
import ru.livetyping.zarina.domain.old.ProductSort

@Factory
class AnyQuerySearchRemoteSource(
    private val api: IAnyQuerySearchApi,
) : ISearchRemoteSource {

    override suspend fun getAutocomplete(query: String) = api.getAutocomplete(query).toDomain()

    override suspend fun getSearchPage(
        query: String,
        sort: ProductSort,
        filtration: Filtration?,
        pageIndex: Int,
    ): Page<FilteredProducts> {
        val response = api.getSearchResults(
            query = query,
            offset = pageIndex * PAGE_SIZE,
            sort = SortDto.from(sort),
            filters = filtration?.toDto().orEmpty(),
        )
        return response.toDomain(pageIndex = pageIndex, pageSize = PAGE_SIZE)
    }

    private fun Filtration.toDto(): List<String> {
        return buildList {
            if (price != null)
                add("${FacetDto.NAME_PRICE}:${price.min};${price.max}")
            if (sizes != null) {
                val selectedItems = sizes.items.filter { it.isSelected }
                if (selectedItems.isNotEmpty()) {
                    add("${FacetDto.NAME_SIZE}:${selectedItems.joinToString(";") { it.id }}")
                }
            }
            if (colors != null) {
                val selectedItems = colors.items.filter { it.isSelected }
                if (selectedItems.isNotEmpty()) {
                    add("${FacetDto.NAME_COLOR}:${selectedItems.joinToString(";") { it.id }}")
                }
            }
            if (categories != null && !categories.isEmpty) {
                val selectedItems = categories.getSelectedOptimized()
                if (selectedItems.isNotEmpty()) {
                    add("${FacetDto.NAME_CATEGORIES}:${selectedItems.joinToString(";") { it.id }}")
                }
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 10
    }

}
