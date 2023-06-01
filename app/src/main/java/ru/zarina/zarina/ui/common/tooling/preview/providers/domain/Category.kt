package ru.zarina.zarina.ui.common.tooling.preview.providers.domain

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.Url

class CategoryListProvider : PreviewParameterProvider<List<Category>> {
    override val values = sequenceOf(
        listOf(
            Category(
                id = Category.Id(0),
                image = Url("https://imgcdn.zarina.ru/upload/iblock/085/085e776db43ebc233bbdf8057cdcb697.jpg"),
                name = "ВЫПУСКНОЙ",
                subcategories = listOf(
                    Category(
                        id = Category.Id(1),
                        image = null,
                        name = "Смотреть все",
                        subcategories = emptyList()
                    ),
                    Category(
                        id = Category.Id(2),
                        image = null,
                        name = "Платья",
                        subcategories = emptyList()
                    ),
                )
            ),
            Category(
                id = Category.Id(3),
                image = Url("https://imgcdn.zarina.ru/upload/iblock/eff/eff62805075aaa01925f7d5b0b0e58ca.jpg"),
                name = "Новинки",
                subcategories = listOf(
                    Category(
                        id = Category.Id(4),
                        image = null,
                        name = "Смотреть все",
                        subcategories = emptyList()
                    ),
                    Category(
                        id = Category.Id(5),
                        image = null,
                        name = "Платья",
                        subcategories = emptyList()
                    ),
                )
            ),
        )
    )
}
