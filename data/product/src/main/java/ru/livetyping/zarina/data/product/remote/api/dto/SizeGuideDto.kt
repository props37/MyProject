package ru.livetyping.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.ProductSizeEn
import ru.livetyping.zarina.core.domain.model.product.ProductSizeFull
import ru.livetyping.zarina.core.domain.model.product.ProductSizeRu
import ru.livetyping.zarina.core.domain.model.product.SizeGuide
import timber.log.Timber

@Serializable
internal data class SizeGuideDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("ru")
    val ru: String? = null,

    @SerialName("bust")
    val bust: String? = null,

    @SerialName("waist")
    val waist: String? = null,

    @SerialName("hips")
    val hips: String? = null,

    @SerialName("growth")
    val growth: String? = null,
) {
    fun toSizeGuideEntry(): SizeGuide.Entry? {
        return if (id != null && name != null && ru != null && bust != null && waist != null && hips != null && growth != null) {
            SizeGuide.Entry(
                sizeEn = ProductSizeEn(id),
                sizeRu = ProductSizeRu(ru),
                sizeFull = ProductSizeFull(name),
                bust = bust,
                waist = waist,
                hips = hips,
                height = growth,
            )
        } else {
            Timber.tag(TAG).e("Ignore $this because it can't be mapped to SizeGuide.Entry")
            null
        }
    }

    private companion object {
        private const val TAG = "SizeGuideDto"
    }
}