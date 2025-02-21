package ru.livetyping.zarina.feature.productlist.ui.impl.impl.listfilter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.navigation.ScreenResult
import ru.livetyping.zarina.core.uimodel.product.filter.ProductListFilterParcelable
import java.util.UUID

@Serializable
@Parcelize
internal data class ListFilterResult(
    override val id: String = UUID.randomUUID().toString(),
    val filter: ProductListFilterParcelable,
) : ScreenResult, Parcelable {
    companion object {
        const val KEY = "list_filter_result_key"
    }
}
