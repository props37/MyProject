package ru.zarina.zarina.data.old.product.remote.api.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MaterialFilterDto(
    @SerialName("id")
    override val id: String? = null,
    @SerialName("name")
    override val name: String? = null,
    @SerialName("is_applied")
    override val isApplied: Boolean? = null,
) : FilterItemDto
