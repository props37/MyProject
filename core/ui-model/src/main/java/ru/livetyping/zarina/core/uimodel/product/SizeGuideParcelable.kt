package ru.livetyping.zarina.core.uimodel.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.ProductSizeEn
import ru.livetyping.zarina.core.domain.model.product.ProductSizeFull
import ru.livetyping.zarina.core.domain.model.product.ProductSizeRu
import ru.livetyping.zarina.core.domain.model.product.SizeGuide

@Parcelize
@Serializable
public data class SizeGuideParcelable(
    val entries: List<EntryParcelable>,
) : Parcelable {

    public fun toSizeGuide(): SizeGuide {
        return SizeGuide(
            entries = entries.map { it.toSizeGuideEntry() },
        )
    }

    @Parcelize
    @Serializable
    public data class EntryParcelable(
        val sizeEn: String,
        val sizeRu: String,
        val sizeFull: String,
        val bust: String,
        val waist: String,
        val hips: String,
        val height: String,
    ) : Parcelable {
        public fun toSizeGuideEntry(): SizeGuide.Entry {
            return SizeGuide.Entry(
                sizeEn = ProductSizeEn(sizeEn),
                sizeRu = ProductSizeRu(sizeRu),
                sizeFull = ProductSizeFull(sizeFull),
                bust = bust,
                waist = waist,
                hips = hips,
                height = height,
            )
        }

        internal companion object {
            fun fromSizeGuideEntry(entry: SizeGuide.Entry): EntryParcelable {
                return EntryParcelable(
                    sizeEn = entry.sizeEn.size,
                    sizeRu = entry.sizeRu.size,
                    sizeFull = entry.sizeFull.size,
                    bust = entry.bust,
                    waist = entry.waist,
                    hips = entry.hips,
                    height = entry.height,
                )
            }
        }
    }

    public companion object {
        public fun fromSizeGuide(sizeGuide: SizeGuide): SizeGuideParcelable {
            return SizeGuideParcelable(
                entries = sizeGuide.entries.map { EntryParcelable.fromSizeGuideEntry(it) },
            )
        }
    }
}
