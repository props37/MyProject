package ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable

import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.ProductMeasurements
import ru.livetyping.zarina.core.domain.model.product.SizeGuide
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigationutil.parcelableNavType
import ru.livetyping.zarina.core.uimodel.product.ProductMeasurementsParcelable
import ru.livetyping.zarina.core.uimodel.product.SizeGuideParcelable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Parcelize
@Serializable
internal class SizeTableNavEntry private constructor(
    private val productMeasurements: ProductMeasurementsParcelable,
    private val sizeGuide: SizeGuideParcelable,
) : NavigationEntry, Parcelable {
    fun getProductMeasurements(): ProductMeasurements {
        return productMeasurements.toProductMeasurements()
    }

    fun getSizeGuide(): SizeGuide {
        return sizeGuide.toSizeGuide()
    }

    companion object {
        fun from(
            productMeasurements: ProductMeasurements,
            sizeGuide: SizeGuide,
        ): SizeTableNavEntry {
            return SizeTableNavEntry(
                productMeasurements = ProductMeasurementsParcelable.from(productMeasurements),
                sizeGuide = SizeGuideParcelable.from(sizeGuide),
            )
        }

        fun typeMap(): Map<KType, NavType<*>> {
            val measurementsNavType = parcelableNavType<ProductMeasurementsParcelable>(
                isNullableAllowed = false,
            )
            val sizeGuideNavType = parcelableNavType<SizeGuideParcelable>(
                isNullableAllowed = false,
            )
            return mapOf(
                typeOf<ProductMeasurementsParcelable>() to measurementsNavType,
                typeOf<SizeGuideParcelable>() to sizeGuideNavType,
            )
        }
    }
}
