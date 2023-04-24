package ru.zarina.zarina.ui.common.tooling.preview.providers.domain

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.zarina.zarina.domain.Media
import ru.zarina.zarina.domain.Price
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.Url

class ProductProvider : PreviewParameterProvider<Product> {
    override val values = sequenceOf(
        Product(
            id = "1329404704-50",
            media = listOf(
                Media(
                    url = Url("https://imgcdn.zarina.ru/upload/images/13294/thumb/450_9999/1329404704_50_1.jpg?t=1631575743"),
                    type = Media.Type.IMAGE,
                ),
                Media(
                    url = Url("https://imgcdn.zarina.ru/upload/images/13294/1329404704_50_3.mp4?t=1661930646"),
                    type = Media.Type.VIDEO,
                ),
                Media(
                    url = Url("https://imgcdn.zarina.ru/upload/images/13294/thumb/450_9999/1329404704_50_2.jpg?t=1631575743"),
                    type = Media.Type.IMAGE,
                ),
            ),
            price = Price(current = 1699, original = 2399),
        )
    )
}
