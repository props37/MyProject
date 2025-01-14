package ru.livetyping.zarina.data.cart.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.model.cart.CartPrice
import ru.livetyping.zarina.core.domain.model.cart.CartSize
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.giftcert.AppliedGiftCertificate
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import ru.livetyping.zarina.core.domain.model.giftcert.GiftCertificate as GiftCertificateDomain
import ru.livetyping.zarina.core.domain.model.user.MyCard as MyCardDomain

@Serializable
internal data class CartDto(
    @SerialName("items")
    val items: List<CartProductDto>? = null,

    @SerialName("total_count")
    val totalCount: Int? = null,

    @SerialName("delivery_count")
    val deliveryCount: Int? = null,

    @SerialName("retail_count")
    val retailCount: Int? = null,

    @SerialName("total_sum")
    val totalSum: Int? = null,

    @SerialName("total_discount")
    val totalDiscount: Int? = null,

    @SerialName("discount")
    val discount: Int? = null,

    @SerialName("bonus_balance")
    val bonusBalance: Int? = null,

    @SerialName("max_bonuses_to_charge_off")
    val maxBonusToChargeOff: Int? = null,

    @SerialName("bonus_action")
    val bonusAction: BonusAction? = null,

    @SerialName("myCard")
    val myCard: MyCard? = null,

    @SerialName("giftCard")
    val giftCard: GiftCertificate? = null,

    @SerialName("deliveryPrice") 
    val deliveryPrice: Int? = null,

    @SerialName("is_promocode_applied")
    val isPromoCodeApplied: Boolean? = null,

    @SerialName("promocode")
    val promoCode: PromoCode? = null,

    @SerialName("limit")
    val limit: ProductLimit? = null,
) {
    fun toCart(cartType: CartType): Cart {
        checkPropertyNotNull(items) { ::items }
        return Cart(
            products = items.map { it.toCartProduct() },
            size = getCartSize(),
            price = getCartPrice(),
            bonuses = getBonuses(),
            myCard = getMyCard(),
            giftCertificate = getAppliedGiftCertificate(),
            promoCode = getPromoCode(),
            productLimit = getProductLimit(cartType),
        )
    }

    private fun getCartSize(): CartSize {
        checkPropertyNotNull(deliveryCount) { ::deliveryCount }
        checkPropertyNotNull(retailCount) { ::retailCount }
        return CartSize(
            deliveryProductCount = deliveryCount,
            pickupProductCount = retailCount,
        )
    }

    private fun getCartPrice(): CartPrice {
        checkPropertyNotNull(discount) { ::discount }
        checkPropertyNotNull(totalDiscount) { ::totalDiscount }
        checkPropertyNotNull(totalSum) { ::totalSum }
        val giftCertificateWriteOffSize = giftCard?.awayAmount?.toIntOrNull()
        val finalPrice = if (giftCard?.total != null) {
            giftCard.total
        } else {
            totalSum
        }
        return CartPrice(
            cartPrice = discount,
            discountSize = totalDiscount,
            finalPrice = finalPrice,
            deliveryPrice = deliveryPrice,
            giftCertificateWriteOffSize = giftCertificateWriteOffSize,
        )
    }

    private fun getBonuses(): Cart.Bonuses {
        checkPropertyNotNull(bonusAction) { ::bonusAction }
        checkPropertyNotNull(bonusAction.bonusCharge) { bonusAction::bonusCharge }
        checkPropertyNotNull(bonusAction.isChargingOffApplied) { bonusAction::isChargingOffApplied }
        checkPropertyNotNull(bonusAction.bonusChargeOff) { bonusAction::bonusChargeOff }
        checkPropertyNotNull(maxBonusToChargeOff) { ::maxBonusToChargeOff }
        val writeOff = Cart.Bonuses.WriteOff(
            isApplied = bonusAction.isChargingOffApplied,
            value = bonusAction.bonusChargeOff,
            max = maxBonusToChargeOff,
        )
        return Cart.Bonuses(
            available = bonusBalance ?: 0,
            accrualForPurchase = bonusAction.bonusCharge,
            writeOff = writeOff,
        )
    }

    private fun getMyCard(): Cart.MyCard? {
        if (myCard?.value == null) return null
        checkPropertyNotNull(myCard.isApplied) { myCard::isApplied }
        checkPropertyNotNull(myCard.productsFirstPriceSum) { myCard::productsFirstPriceSum }
        return Cart.MyCard(
            number = MyCardDomain.Number(myCard.value),
            info = myCard.info,
            isApplied = myCard.isApplied,
            productsFirstPriceSum = myCard.productsFirstPriceSum,
        )
    }

    private fun getAppliedGiftCertificate(): AppliedGiftCertificate? {
        if (giftCard?.barcode == null) return null
        checkPropertyNotNull(giftCard.amount) { giftCard::amount }
        val writeOffSize = giftCard.awayAmount?.toIntOrNull()
        checkNotNull(writeOffSize) { "writeOffSize is null" }
        return AppliedGiftCertificate(
            number = GiftCertificateDomain.Number(giftCard.barcode),
            balance = giftCard.amount,
            writeOffSize = writeOffSize,
        )
    }

    private fun getPromoCode(): Cart.PromoCode? {
        if (promoCode?.code == null) return null
        checkPropertyNotNull(isPromoCodeApplied) { ::isPromoCodeApplied }
        return Cart.PromoCode(
            isApplied = isPromoCodeApplied,
            value = promoCode.code,
        )
    }

    private fun getProductLimit(cartType: CartType): Cart.ProductLimit {
        checkPropertyNotNull(limit) { ::limit }
        checkPropertyNotNull(limit.max) { limit::max }
        val productCount = when (cartType) {
            CartType.DELIVERY -> deliveryCount
            CartType.PICKUP -> retailCount
        }
        checkNotNull(productCount) { "product count is null" }
        val limit = limit.max
        return Cart.ProductLimit(
            limit = limit,
            isExceeded = productCount > limit,
        )
    }

    @Serializable
    data class BonusAction(
        @SerialName("bonus_charge")
        val bonusCharge: Int? = null,

        @SerialName("is_charging_off_applied")
        val isChargingOffApplied: Boolean? = null,

        @SerialName("bonus_charge_off")
        val bonusChargeOff: Int? = null,
    )

    @Serializable
    data class MyCard(
        @SerialName("value") 
        val value: String? = null,

        @SerialName("info") 
        val info: String? = null,

        @SerialName("isApplied") 
        val isApplied: Boolean? = null,

        @SerialName("productsFirstPriceSum")
        val productsFirstPriceSum: Int? = null,
    )

    @Serializable
    data class GiftCertificate(
        @SerialName("barcode")
        val barcode: String? = null,

        @SerialName("amount")
        val amount: Int? = null,

        @SerialName("away_amount")
        val awayAmount: String? = null,

        @SerialName("total")
        val total: Int? = null,
    )

    @Serializable
    data class PromoCode(
        @SerialName("code")
        val code: String? = null,
    )

    @Serializable
    data class ProductLimit(
        @SerialName("max") 
        val max: Int? = null,
    )
}
