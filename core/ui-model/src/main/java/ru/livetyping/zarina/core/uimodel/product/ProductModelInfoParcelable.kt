package ru.livetyping.zarina.core.uimodel.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.ProductDetailed

@Parcelize
@Serializable
public data class ProductModelInfoParcelable(
    val sizeOnModel: String?,
    val modelParameters: List<ModelParameterParcelable>?,
) : Parcelable {

    public fun toModelInfo(): ProductDetailed.ModelInfo {
        return ProductDetailed.ModelInfo(
            sizeOnModel = sizeOnModel,
            modelParameters = modelParameters?.map { it.toModelParameter() },
        )
    }

    @Parcelize
    @Serializable
    public data class ModelParameterParcelable(
        val title: String,
        val value: String,
    ) : Parcelable {
        public fun toModelParameter(): ProductDetailed.ModelInfo.ModelParameter {
            return ProductDetailed.ModelInfo.ModelParameter(title, value)
        }

        public companion object {
            public fun from(parameter: ProductDetailed.ModelInfo.ModelParameter): ModelParameterParcelable {
                return ModelParameterParcelable(parameter.title, parameter.value)
            }
        }
    }

    public companion object {
        public fun from(info: ProductDetailed.ModelInfo): ProductModelInfoParcelable {
            return ProductModelInfoParcelable(
                sizeOnModel = info.sizeOnModel,
                modelParameters = info.modelParameters?.map { ModelParameterParcelable.from(it) },
            )
        }
    }
}
