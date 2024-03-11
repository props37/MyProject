package ru.zarina.zarina.ui.common.tooling

import ru.zarina.zarina.domain.category.Category
import ru.zarina.zarina.domain.rework.common.Barcode
import ru.zarina.zarina.domain.rework.common.Color
import ru.zarina.zarina.domain.rework.common.Media
import ru.zarina.zarina.domain.rework.common.MediaType
import ru.zarina.zarina.domain.rework.common.Url
import ru.zarina.zarina.domain.rework.product.Price
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.domain.rework.product.ProductColor
import ru.zarina.zarina.domain.rework.product.ProductOffer
import java.util.UUID
import kotlin.random.Random

object FakeDataGenerator {
    fun getCategories(
        count: Int = 10,
        generator: (Int) -> Category,
    ): List<Category> = List(count) { generator(it) }

    fun getCategory(
        id: Category.Id = Category.Id(Random.nextLong()),
        name: String = "Одежда",
        label: String? = "Акция",
        color: Color? = null,
        isExpandable: Boolean = true,
        children: List<Category>? = List(5) { getCategory(children = null) },
    ): Category = Category(
        id = id,
        name = name,
        label = label,
        color = color,
        isExpandable = isExpandable,
        children = children,
    )

    fun getProducts(
        count: Int = 10,
        generator: (Int) -> Product = { getProduct() },
    ): List<Product> = List(count) { generator(it) }

    fun getProduct(
        id: Product.Id = Product.Id(getRandomString()),
        name: String = getProductNames().random(),
        price: Price = getPrice(),
        offers: List<ProductOffer> = getProductOffers(),
        colors: List<ProductColor> = getProductColors(),
        media: List<Media> = getMediaList(),
        isInFavorites: Boolean = Random.nextBoolean(),
        isInCart: Boolean = Random.nextBoolean(),
    ): Product = Product(
        id = id,
        name = name,
        price = price,
        offers = offers,
        colors = colors,
        media = media,
        isInFavorites = isInFavorites,
        isInCart = isInCart,
    )

    fun getMediaList(
        count: Int = 10,
        generator: (Int) -> Media = { getMedia() },
    ): List<Media> = List(count) { generator(it) }

    fun getMedia(
        url: Url = Url(getRandomString()),
        type: MediaType = MediaType.IMAGE,
    ): Media = Media(
        url = url,
        type = type,
    )

    fun getProductOffers(
        count: Int = 5,
        generator: (Int) -> ProductOffer = { getProductOffer() },
    ): List<ProductOffer> = List(count) { generator(it) }

    fun getProductOffer(
        id: ProductOffer.Id = ProductOffer.Id(getRandomString()),
        size: String = "M",
        sizeRu: String = "48",
        isAvailable: Boolean = Random.nextBoolean(),
        height: String = "170",
        barcode: Barcode = Barcode(getRandomString()),
        onlineCount: Int = if (isAvailable) Random.nextInt(1, 50) else 0,
        retailCount: Int = if (isAvailable) Random.nextInt(1, 50) else 0,
    ): ProductOffer = ProductOffer(
        id = id,
        size = size,
        sizeRu = sizeRu,
        isAvailable = isAvailable,
        height = height,
        barcode = barcode,
        onlineCount = onlineCount,
        retailCount = retailCount,
    )

    fun getProductColors(
        count: Int = 5,
        generator: (Int) -> ProductColor = { getProductColor() },
    ): List<ProductColor> = List(count) { generator(it) }

    fun getProductColor(
        id: ProductColor.Id = ProductColor.Id(getRandomString()),
        name: String = "Синий",
        color: Color = Color(String.format("#%06X", (0xFFFFFF and android.graphics.Color.BLUE))),
        productId: Product.Id = Product.Id(getRandomString()),
    ): ProductColor = ProductColor(
        id = id,
        name = name,
        color = color,
        productId = productId,
    )

    fun getPrice(
        originalPrice: Long = 4999,
        hasDiscount: Boolean = Random.nextBoolean(),
        discountPrice: Long = if (hasDiscount) {
            (originalPrice * Random.nextDouble(0.2, 0.9)).toLong()
        } else {
            originalPrice
        },
        discountPercent: Int = ((originalPrice - discountPrice) / originalPrice.toFloat() * 100).toInt(),
    ): Price = Price(
        originalPrice = originalPrice,
        hasDiscount = hasDiscount,
        discountPrice = discountPrice,
        discountPercent = discountPercent,
    )

    private fun getProductNames(): List<String> = listOf(
        "Свитер из вискозы",
        "Платье",
        "Длинное платье",
        "Джемпер оверсайз",
        "Куртка из эко-кожи",
    )

    @Suppress("NOTHING_TO_INLINE")
    private inline fun getRandomString(): String = UUID.randomUUID().toString()
}
