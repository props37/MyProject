package ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable

import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.gender.Gender
import ru.livetyping.zarina.core.domain.model.product.ProductMeasurements
import ru.livetyping.zarina.core.domain.model.product.SizeGuide
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigationutil.parcelableNavType
import ru.livetyping.zarina.core.uimodel.common.GenderParcelable
import ru.livetyping.zarina.core.uimodel.product.ProductMeasurementsParcelable
import ru.livetyping.zarina.core.uimodel.product.SizeGuideParcelable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Parcelize
@Serializable
internal class SizeTableNavEntry private constructor(
    private val productMeasurements: ProductMeasurementsParcelable,
    private val sizeGuide: SizeGuideParcelable,
    private val gender: GenderParcelable,
) : NavigationEntry, Parcelable {
    fun getProductMeasurements(): ProductMeasurements {
        return productMeasurements.toProductMeasurements()
    }

    fun getSizeGuide(): SizeGuide {
        return sizeGuide.toSizeGuide()
    }

    fun getGender(): Gender {
        return gender.toGender()
    }

    companion object {
        fun from(
            productMeasurements: ProductMeasurements,
            sizeGuide: SizeGuide,
            gender: Gender,
        ): SizeTableNavEntry {
            return SizeTableNavEntry(
                productMeasurements = ProductMeasurementsParcelable.from(productMeasurements),
                sizeGuide = SizeGuideParcelable.from(sizeGuide),
                gender = GenderParcelable.from(gender),
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
                typeOf<GenderParcelable>() to NavType.EnumType(GenderParcelable::class.java),
            )
        }
    }
}
