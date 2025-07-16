package ru.livetyping.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.ProductHeight
import ru.livetyping.zarina.core.domain.model.product.ProductMeasurement
import ru.livetyping.zarina.core.domain.model.product.ProductMeasurements
import ru.livetyping.zarina.core.domain.model.product.ProductSizeEn
import timber.log.Timber

@Serializable
internal data class ProductMeasurementDto(
    @SerialName("size")
    val size: String? = null,

    @SerialName("height")
    val height: List<HeightDto>? = null,
) {
    fun toProductMeasurementEntries(): List<ProductMeasurements.Entry>? {
        return if (size != null) {
            height
                ?.mapNotNull { heightDto ->
                    if (heightDto.value != null) {
                        val measurements = heightDto.measurements
                            ?.mapNotNull { it.toProductMeasurement() }
                            ?.takeIf { it.isNotEmpty() }
                        if (measurements != null) {
                            ProductMeasurements.Entry(
                                size = ProductSizeEn(size),
                                height = ProductHeight(heightDto.value),
                                measurements = measurements,
                            )
                        } else {
                            Timber.tag(TAG).w("Ignore $this because it can't be mapped to ProductMeasurements.Entry")
                            null
                        }
                    } else {
                        Timber.tag(TAG).w("Ignore $this because it can't be mapped to ProductMeasurements.Entry")
                        null
                    }
                }
                ?.takeIf { it.isNotEmpty() }
        } else {
            Timber.tag(TAG).w("Ignore $this because it can't be mapped to List<ProductMeasurements.Entry>")
            null
        }
    }

    @Serializable
    data class HeightDto(
        @SerialName("value")
        val value: String? = null,

        @SerialName("measurements")
        val measurements: List<MeasurementDto>? = null,
    ) {
        @Serializable
        data class MeasurementDto(
            @SerialName("title")
            val title: String? = null,

            @SerialName("value")
            val value: String? = null,
        ) {
            fun toProductMeasurement(): ProductMeasurement? {
                return if (title != null && value != null) {
                    ProductMeasurement(title, value)
                } else {
                    Timber.tag(TAG).w("Ignore $this because it can't be mapped to ProductMeasurement")
                    null
                }
            }
        }
    }

    private companion object {
        private const val TAG = "ProductMeasurementDto"
    }
}