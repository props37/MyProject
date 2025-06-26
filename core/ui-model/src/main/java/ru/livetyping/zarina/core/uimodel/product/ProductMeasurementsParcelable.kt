package ru.livetyping.zarina.core.uimodel.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.ProductHeight
import ru.livetyping.zarina.core.domain.model.product.ProductMeasurement
import ru.livetyping.zarina.core.domain.model.product.ProductMeasurements
import ru.livetyping.zarina.core.domain.model.product.ProductSizeEn

@Parcelize
@Serializable
public data class ProductMeasurementsParcelable(
    val entries: List<EntryParcelable>,
) : Parcelable {
    public fun toProductMeasurements(): ProductMeasurements {
        return ProductMeasurements(
            entries = entries.map { it.toEntry() },
        )
    }

    @Parcelize
    @Serializable
    public data class EntryParcelable(
        val size: String,
        val height: String,
        val measurements: List<ProductMeasurementParcelable>,
    ) : Parcelable {
        public fun toEntry(): ProductMeasurements.Entry {
            return ProductMeasurements.Entry(
                size = ProductSizeEn(size),
                height = ProductHeight(height),
                measurements = measurements.map { it.toProductMeasurement() },
            )
        }

        public companion object {
            public fun from(entry: ProductMeasurements.Entry): EntryParcelable {
                return EntryParcelable(
                    size = entry.size.size,
                    height = entry.height.height,
                    measurements = entry.measurements.map { ProductMeasurementParcelable.from(it) },
                )
            }
        }
    }

    public companion object {
        public fun from(productMeasurements: ProductMeasurements): ProductMeasurementsParcelable {
            return ProductMeasurementsParcelable(
                entries = productMeasurements.entries.map { EntryParcelable.from(it) },
            )
        }
    }
}

@Parcelize
@Serializable
public data class ProductMeasurementParcelable(
    val title: String,
    val value: String,
) : Parcelable {
    public fun toProductMeasurement(): ProductMeasurement {
        return ProductMeasurement(title, value)
    }

    public companion object {
        public fun from(productMeasurement: ProductMeasurement): ProductMeasurementParcelable {
            return ProductMeasurementParcelable(productMeasurement.title, productMeasurement.value)
        }
    }
}
