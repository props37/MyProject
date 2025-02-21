package ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.core.navigation.ScreenResult
import ru.livetyping.zarina.core.uimodel.product.filter.ProductFiltersParcelable
import java.util.UUID

@Parcelize
internal data class FiltrationResult(
    override val id: String = UUID.randomUUID().toString(),
    val filters: ProductFiltersParcelable,
) : ScreenResult, Parcelable {
    companion object {
        const val KEY = "filtration_result_key"
    }
}
