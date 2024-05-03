package ru.livetyping.zarina.ui.common.tooling

import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.common.Color
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.Media
import ru.livetyping.zarina.domain.common.MediaType
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.domain.order.OrderItem
import ru.livetyping.zarina.domain.order.OrderStatus
import ru.livetyping.zarina.domain.product.Price
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductColor
import ru.livetyping.zarina.domain.product.ProductItem
import ru.livetyping.zarina.domain.product.ProductOffer
import ru.livetyping.zarina.domain.user.LoyaltyCard
import ru.livetyping.zarina.domain.user.LoyaltyCardLevel
import ru.livetyping.zarina.domain.user.User
import java.time.LocalDate
import java.util.UUID
import kotlin.random.Random

object FakeDataGenerator {
    fun getLoyaltyCard(
        number: LoyaltyCard.Number = LoyaltyCard.Number("7820103395338"),
        level: LoyaltyCardLevel = LoyaltyCardLevel.PRIME,
        nextLevelInfo: LoyaltyCard.NextLevelInfo? = LoyaltyCard.NextLevelInfo(
            level = LoyaltyCardLevel.PRIORITY,
            requiredPurchaseSum = 10000,
        ),
        bonuses: LoyaltyCard.Bonuses = LoyaltyCard.Bonuses(
            bonusCount = 8000,
            expectedBonusCount = 1200,
        ),
        totalPurchaseSum: Int = 2500,
    ): LoyaltyCard = LoyaltyCard(
        number = number,
        level = level,
        nextLevelInfo = nextLevelInfo,
        bonuses = bonuses,
        totalPurchaseSum = totalPurchaseSum,
    )

    fun getOrderItem(
        id: Order.Id = Order.Id(Random.nextLong(from = 0, until = Long.MAX_VALUE)),
        number: Order.Number = Order.Number(Random.nextLong(from = 123456, until = 987654).toString()),
        productCount: Int = Random.nextInt(from = 1, until = 10),
        date: LocalDate = getLocalDate(),
        status: OrderStatus = OrderStatus.entries.random(),
        totalPrice: Long = 7999,
        products: List<OrderItem.Product> = List(productCount) { getOrderItemProduct() },
    ): OrderItem = OrderItem(
        id = id,
        number = number,
        productCount = productCount,
        date = date,
        status = status,
        totalPrice = totalPrice,
        products = products,
    )

    fun getOrderItemProduct(
        imageUrl: Url = Url.EMPTY,
        count: Int = Random.nextInt(from = 1, until = 10)
    ): OrderItem.Product = OrderItem.Product(
        imageUrl = imageUrl,
        count = count,
    )

    fun getUser(
        id: User.Id = User.Id(getRandomString()),
        email: Email = getEmail(),
        phone: PhoneNumber = getPhoneNumber(),
        firstName: String = "Артём",
        lastName: String = "Сидоров",
        birthDate: LocalDate = LocalDate.of(1998, 2, 26),
    ): User = User(
        id = id,
        email = email,
        phone = phone,
        firstName = firstName,
        lastName = lastName,
        birthDate = birthDate,
    )

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

    fun getProductItems(
        count: Int = 10,
        generator: (Int) -> ProductItem = { getProductItem() },
    ): List<ProductItem> = List(count) { generator(it) }

    fun getProductItem(
        id: Product.Id = Product.Id(getRandomString()),
        name: String = getProductNames().random(),
        price: Price = getPrice(),
        offers: List<ProductOffer> = getProductOffers(),
        colors: List<ProductColor> = getProductColors(),
        media: List<Media> = getMediaList(),
        isInFavorites: Boolean = Random.nextBoolean(),
        isInCart: Boolean = Random.nextBoolean(),
    ): ProductItem = ProductItem(
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

    fun getLocalDate(
        year: Int = LocalDate.now().year,
        month: Int = LocalDate.now().monthValue,
        dayOfMonth: Int = LocalDate.now().dayOfMonth,
    ): LocalDate = LocalDate.of(year, month, dayOfMonth)

    fun getPhoneNumber(value: String = "+78005553535"): PhoneNumber = PhoneNumber.create(value)

    fun getEmail(value: String = "example@mail.com"): Email = Email.create(value)

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
