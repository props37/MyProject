package ru.livetyping.zarina.core.domain.model.product

// Marked as stable on config/compose/stability_config.txt
public enum class ProductSorting {
    NEW,
    POPULAR,
    DISCOUNT,
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW;

    public companion object {
        public fun getDefault(): ProductSorting = NEW
    }
}
