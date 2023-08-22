package ru.zarina.zarina.data.search.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FacetValueDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("value")
    val value: Long? = null,
    @SerialName("pictureUrl")
    val pictureUrl: String? = null,
    @SerialName("children")
    val children: List<FacetValueDto>? = null,
    @SerialName("open")
    val isOpen: Boolean? = null,
    @SerialName("selected")
    val isSelected: Boolean? = null,
)
