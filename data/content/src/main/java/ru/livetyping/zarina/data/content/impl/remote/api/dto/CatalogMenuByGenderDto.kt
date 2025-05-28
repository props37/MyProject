package ru.livetyping.zarina.data.content.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuByGender
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull

@Serializable
internal data class CatalogMenuByGenderDto(
    @SerialName("woman")
    val woman: CatalogMenuDto? = null,

    @SerialName("man")
    val man: CatalogMenuDto? = null,
) {
    fun toCatalogMenu(): CatalogMenuByGender {
        checkPropertyNotNull(woman) { "woman" }
        checkPropertyNotNull(man) { "man" }
        return CatalogMenuByGender(
            women = woman.toCatalogMenu(),
            men = man.toCatalogMenu(),
        )
    }
}
