package ru.zarina.zarina.ui.common.tooling

import ru.zarina.zarina.domain.rework.common.Color
import ru.zarina.zarina.domain.rework.common.Media
import ru.zarina.zarina.domain.rework.common.MediaType
import ru.zarina.zarina.domain.rework.common.Url
import ru.zarina.zarina.domain.rework.product.Price
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.domain.rework.product.ProductColor
import java.util.UUID
import kotlin.random.Random

object FakeDataGenerator {
    fun getProducts(
        count: Int = 10,
        generator: (Int) -> Product = { getProduct() },
    ): List<Product> = List(count) {
        generator(it)
    }

    fun getProduct(
        id: Product.Id = Product.Id(getRandomString()),
        name: String = getProductNames().random(),
        price: Price = getPrice(),
        colors: List<ProductColor> = getProductColors(),
        media: List<Media> = getMediaList(),
        isInFavorites: Boolean = Random.nextBoolean(),
        isInCart: Boolean = Random.nextBoolean(),
    ): Product = Product(
        id = id,
        name = name,
        price = price,
        colors = colors,
        media = media,
        isInFavorites = isInFavorites,
        isInCart = isInCart,
    )

    fun getMediaList(
        count: Int = 10,
        generator: (Int) -> Media = { getMedia() },
    ): List<Media> = List(count) {
        generator(it)
    }

    fun getMedia(
        url: Url = Url(getRandomString()),
        type: MediaType = MediaType.IMAGE,
    ): Media = Media(
        url = url,
        type = type,
    )

    fun getProductColors(
        count: Int = 5,
        generator: (Int) -> ProductColor = { getProductColor() },
    ): List<ProductColor> = List(count) {
        generator(it)
    }

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

    private fun getRandomString(): String = UUID.randomUUID().toString()
}
