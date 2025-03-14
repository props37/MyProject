package ru.livetyping.zarina.data.cart.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.model.cart.CartPrice
import ru.livetyping.zarina.core.domain.model.cart.CartSize
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.giftcert.AppliedGiftCertificate
import ru.livetyping.zarina.core.domain.model.giftcert.GiftCertificate
import ru.livetyping.zarina.core.domain.model.user.MyCard
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull

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
    val bonusAction: BonusActionDto? = null,

    @SerialName("myCard")
    val myCard: MyCardDto? = null,

    @SerialName("giftCard")
    val giftCard: GiftCertificateDto? = null,

    @SerialName("deliveryPrice") 
    val deliveryPrice: Int? = null,

    @SerialName("is_promocode_applied")
    val isPromoCodeApplied: Boolean? = null,

    @SerialName("promocode")
    val promoCode: PromoCodeDto? = null,

    @SerialName("limit")
    val limit: ProductLimitDto? = null,
) {
    fun toCart(cartType: CartType): Cart {
        checkPropertyNotNull(items) { ::items }
        return Cart(
            products = items.map { it.toCartProduct() },
            size = getCartSize(),
            price = getCartPrice(),
            bonusAccount = getBonusAccount(),
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
        val giftCertificateRedemptionValue = giftCard?.awayAmount?.toIntOrNull()
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
            giftCertificateRedemptionValue = giftCertificateRedemptionValue,
        )
    }

    private fun getBonusAccount(): Cart.BonusAccount {
        checkPropertyNotNull(bonusAction) { ::bonusAction }
        checkPropertyNotNull(bonusAction.bonusCharge) { bonusAction::bonusCharge }
        checkPropertyNotNull(bonusAction.isChargingOffApplied) { bonusAction::isChargingOffApplied }
        checkPropertyNotNull(bonusAction.bonusChargeOff) { bonusAction::bonusChargeOff }
        checkPropertyNotNull(maxBonusToChargeOff) { ::maxBonusToChargeOff }
        val redemption = Cart.BonusAccount.Redemption(
            isApplied = bonusAction.isChargingOffApplied,
            value = bonusAction.bonusChargeOff,
            max = maxBonusToChargeOff,
        )
        return Cart.BonusAccount(
            balance = bonusBalance ?: 0,
            addForPurchase = bonusAction.bonusCharge,
            redemption = redemption,
        )
    }

    private fun getMyCard(): Cart.MyCard? {
        if (myCard?.value == null) return null
        checkPropertyNotNull(myCard.isApplied) { myCard::isApplied }
        checkPropertyNotNull(myCard.productsFirstPriceSum) { myCard::productsFirstPriceSum }
        return Cart.MyCard(
            number = MyCard.Number(myCard.value),
            info = myCard.info,
            isApplied = myCard.isApplied,
            productsFirstPriceSum = myCard.productsFirstPriceSum,
        )
    }

    private fun getAppliedGiftCertificate(): AppliedGiftCertificate? {
        if (giftCard?.barcode == null) return null
        checkPropertyNotNull(giftCard.amount) { giftCard::amount }
        val redemptionValue = giftCard.awayAmount?.toIntOrNull()
        checkNotNull(redemptionValue) { "writeOffSize is null" }
        return AppliedGiftCertificate(
            number = GiftCertificate.Number(giftCard.barcode),
            balance = giftCard.amount,
            redemptionValue = redemptionValue,
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
    data class BonusActionDto(
        @SerialName("bonus_charge")
        val bonusCharge: Int? = null,

        @SerialName("is_charging_off_applied")
        val isChargingOffApplied: Boolean? = null,

        @SerialName("bonus_charge_off")
        val bonusChargeOff: Int? = null,
    )

    @Serializable
    data class MyCardDto(
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
    data class GiftCertificateDto(
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
    data class PromoCodeDto(
        @SerialName("code")
        val code: String? = null,
    )

    @Serializable
    data class ProductLimitDto(
        @SerialName("max") 
        val max: Int? = null,
    )
}
