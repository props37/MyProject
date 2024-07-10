package ru.livetyping.zarina.data.cart.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.cart.CartPrice
import ru.livetyping.zarina.domain.cart.CartSize
import ru.livetyping.zarina.domain.user.MyCard as DomainMyCard

@Serializable
data class CartDto(
    @SerialName("items")
    val products: List<CartProductDto>? = null,

    @SerialName("total_count")
    val totalProductCount: Int? = null,

    @SerialName("delivery_count")
    val deliveryProductCount: Int? = null,

    @SerialName("retail_count")
    val pickUpFromStoresProductCount: Int? = null,

    @SerialName("total_sum")
    val totalPrice: Int? = null,

    @SerialName("total_discount")
    val discountSize: Int? = null,

    @SerialName("discount")
    val cartPrice: Int? = null,

    @SerialName("bonus_balance")
    val availableBonusCount: Int? = null,

    @SerialName("max_bonuses_to_charge_off")
    val maxBonusWriteOff: Int? = null,

    @SerialName("bonus_action")
    val bonusAction: BonusAction? = null,
    
    @SerialName("myCard")
    val myCard: MyCard? = null,
) {
    fun toCart(): Cart {
        checkNotNull(products) { "products is null" }
        return Cart(
            products = products.map { it.toCartProduct() },
            size = getCartSize(),
            price = getCartPrice(),
            bonuses = getBonuses(),
            myCard = getMyCard(),
        )
    }

    private fun getCartSize(): CartSize {
        checkNotNull(totalProductCount) { "totalProductCount is null" }
        checkNotNull(deliveryProductCount) { "deliveryProductCount is null" }
        checkNotNull(pickUpFromStoresProductCount) { "pickUpFromStoresProductCount is null" }
        return CartSize(
            totalProductCount = totalProductCount,
            deliveryProductCount = deliveryProductCount,
            pickUpFromStoreProductCount = pickUpFromStoresProductCount,
        )
    }

    private fun getCartPrice(): CartPrice {
        checkNotNull(totalPrice) { "cartPrice is null" }
        checkNotNull(discountSize) { "discountSize is null" }
        checkNotNull(cartPrice) { "totalPrice is null" }
        return CartPrice(
            cartPrice = cartPrice,
            discountSize = discountSize,
            totalPrice = totalPrice,
        )
    }

    private fun getBonuses(): Cart.Bonuses {
        checkNotNull(bonusAction) { "bonusAction is null" }
        checkNotNull(bonusAction.bonusAccrualForPurchase) { "bonusAccrualForPurchase is null" }
        checkNotNull(bonusAction.isBonusWriteOffApplied) { "isBonusWriteOffApplied is null" }
        checkNotNull(bonusAction.bonusWriteOff) { "bonusWriteOff is null" }
        checkNotNull(maxBonusWriteOff) { "maxBonusWriteOff is null" }
        val writeOff = Cart.Bonuses.WriteOff(
            isApplied = bonusAction.isBonusWriteOffApplied,
            value = bonusAction.bonusWriteOff,
            max = maxBonusWriteOff,
        )
        return Cart.Bonuses(
            available = availableBonusCount ?: 0,
            accrualForPurchase = bonusAction.bonusAccrualForPurchase,
            writeOff = writeOff,
        )
    }

    private fun getMyCard(): Cart.MyCard? {
        if (myCard?.number == null) return null

        checkNotNull(myCard.isApplied) { "isApplied is null" }
        checkNotNull(myCard.productsFirstPriceSum) { "productsFirstPriceSum is null" }
        return Cart.MyCard(
            number = DomainMyCard.Number(myCard.number),
            info = myCard.info,
            isApplied = myCard.isApplied,
            productsFirstPriceSum = myCard.productsFirstPriceSum,
        )
    }

    @Serializable
    data class BonusAction(
        @SerialName("bonus_charge")
        val bonusAccrualForPurchase: Int? = null,

        @SerialName("is_charging_off_applied")
        val isBonusWriteOffApplied: Boolean? = null,

        @SerialName("bonus_charge_off")
        val bonusWriteOff: Int? = null,
    )

    @Serializable
    data class MyCard(
        @SerialName("value") 
        val number: String? = null,
        
        @SerialName("info") 
        val info: String? = null,
        
        @SerialName("isApplied") 
        val isApplied: Boolean? = null,

        @SerialName("productsFirstPriceSum")
        val productsFirstPriceSum: Int? = null,
    )
}
