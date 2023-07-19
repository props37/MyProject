package ru.zarina.zarina.ui.common.tooling.preview.providers.domain

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import ru.zarina.zarina.domain.Barcode
import ru.zarina.zarina.domain.Color
import ru.zarina.zarina.domain.Media
import ru.zarina.zarina.domain.Offer
import ru.zarina.zarina.domain.Price
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.Size
import ru.zarina.zarina.domain.Url

class ProductProvider : PreviewParameterProvider<Product> {
    override val values = sequenceOf(
        Product(
            id = Product.Id("1329404704-50"),
            name = "Джинсы mom fit",
            media = persistentListOf(
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
            colorVariants = persistentMapOf(
                Color(
                    id = "109",
                    name = "Темно-серый деним",
                    code = Color.Code("#858585")
                ) to Product.Variant(
                    id = Product.Id("1329404704-109"),
                    isCurrent = false,
                ),
                Color(
                    id = "50",
                    name = "Чёрный",
                    code = Color.Code("#000000")
                ) to Product.Variant(
                    id = Product.Id("1329404704-50"),
                    isCurrent = true,
                ),
            ),
            offers = persistentListOf(
                Offer(
                    id = Offer.Id("110160"),
                    barcode = Barcode("4640078695058"),
                    isAvailable = true,
                    size = Size(
                        id = "XS",
                        name = "XS (RU 42)"
                    ),
                ),
                Offer(
                    id = Offer.Id("110161"),
                    barcode = Barcode("4640078695072"),
                    isAvailable = false,
                    size = Size(
                        id = "S",
                        name = "S (RU 44)"
                    ),
                )
            ),
            description = persistentListOf(
                "Состав" to "99% хлопок, 1% эластан",
                "Страна-производитель" to "ВЬЕТНАМ",
                "Уход" to "Бережная стирка при максимальной температуре 30ºС, Не отбеливать, Машинная сушка запрещена, Глажение при 150ºС, Сухая чистка запрещена",
                "Арт." to "1329404704",
            ),
            url = Url("https://zarina.ru/catalog/product/1329404704-50/"),
            isLookPart = true,
            attributes = persistentListOf("эксклюзивно онлайн"),
            isFavorite = false,
        )
    )
}
