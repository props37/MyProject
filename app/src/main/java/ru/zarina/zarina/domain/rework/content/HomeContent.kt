package ru.zarina.zarina.domain.rework.content

import ru.zarina.zarina.domain.rework.common.MediaType
import ru.zarina.zarina.domain.rework.common.Url

data class HomeContent(
    val womenBanners: List<Banner>,
    val menBanners: List<Banner>,
) {
    sealed class Banner(open val id: Id) {
        data class SingleItem(val item: Item) : Banner(id = item.createBannerId())

        data class MultipleItems(
            val items: List<Item>,
            val viewType: ViewType,
        ) : Banner(id = items.createBannerId()) {
            enum class ViewType { GRID }
        }

        @JvmInline
        value class Id(val value: String)

        data class Item(
            val id: Id,
            val mediaType: MediaType,
            val mediaUrl: Url,
            val title: String?,
        ) {
            @JvmInline
            value class Id(val value: Long)
        }
    }

    companion object {
        val EMPTY: HomeContent
            get() = HomeContent(womenBanners = emptyList(), menBanners = emptyList())
    }
}

private fun HomeContent.Banner.Item.createBannerId(): HomeContent.Banner.Id {
    return HomeContent.Banner.Id(this.id.value.toString())
}

private fun List<HomeContent.Banner.Item>.createBannerId(): HomeContent.Banner.Id {
    val stringId = this.fold(initial = "") { acc, item ->
        acc + item.id.value.toString()
    }
    return HomeContent.Banner.Id(stringId)
}
