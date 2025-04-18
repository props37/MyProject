package ru.livetyping.zarina.core.analytics.model

/**
 * [typeName] matches delivery method types on backend.
 */
public enum class DeliveryMethodType(public val typeName: String) {
    COURIER("delivery_service"),
    POST("post"),
    PICKUP_FROM_PICKUP_POINT("pickup"),
    PICKUP_FROM_STORE_WAREHOUSE("pickupinstore"),
    PICKUP_FROM_STORE("retail"),
    YANDEX_EXPRESS("yandex"),
    COURIER_EXPRESS("express"),
}
