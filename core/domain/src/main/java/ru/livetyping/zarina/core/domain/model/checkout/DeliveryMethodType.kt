package ru.livetyping.zarina.core.domain.model.checkout

// Marked as stable on config/compose/stability_config.txt
public enum class DeliveryMethodType {
    COURIER, // delivery_service on backend
    POST, // post on backend
    PICKUP_FROM_PICKUP_POINT, // pickup on backend
    PICKUP_FROM_STORE_WAREHOUSE, // pickupinstore on backend
    PICKUP_FROM_STORE, // retail on backend
    YANDEX_EXPRESS, // yandex on backend
    COURIER_EXPRESS, // express on backend
}
