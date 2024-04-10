package ru.livetyping.zarina.ui.common.tooling

import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.common.Color
import ru.livetyping.zarina.domain.common.Media
import ru.livetyping.zarina.domain.common.MediaType
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.product.Price
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductColor
import ru.livetyping.zarina.domain.product.ProductOffer
import java.util.UUID
import kotlin.random.Random

object FakeDataGenerator {
    fun getCategories(
        count: Int = 10,
        generator: (Int) -> Category = { getCategory() },
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

    fun getLoremIpsum(words: Int): String {
        val wordList = LOREM_IPSUM.split(" ")
        return wordList.take(words).joinToString(" ")
    }

    private fun getProductNames(): List<String> = listOf(
        "Свитер из вискозы",
        "Платье",
        "Длинное платье",
        "Джемпер оверсайз",
        "Куртка из эко-кожи",
    )

    @Suppress("NOTHING_TO_INLINE")
    private inline fun getRandomString(): String = UUID.randomUUID().toString()

    @Suppress("MaxLineLength")
    private const val LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse pharetra ante ut justo euismod, vitae tempor diam venenatis. Sed egestas, tellus at vulputate ultricies, elit neque facilisis nibh, non commodo nunc mi nec quam. Proin mollis viverra est in faucibus. Aliquam erat volutpat. Cras id arcu porttitor, dignissim velit iaculis, congue magna. Etiam tincidunt ex vitae diam varius pellentesque. Ut gravida, lacus ac mollis blandit, nibh ante vulputate nisl, in ullamcorper tellus nibh sit amet enim. Phasellus fermentum odio diam, at pellentesque arcu luctus id. Proin ac urna id nisi convallis imperdiet. Pellentesque tellus dolor, feugiat scelerisque congue a, hendrerit id leo."
}
