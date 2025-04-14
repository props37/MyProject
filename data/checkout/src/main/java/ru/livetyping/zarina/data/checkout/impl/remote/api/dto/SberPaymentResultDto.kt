package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SberPaymentResultDto(
    @SerialName("sberOrderStatus")
    val sberOrderStatus: Int? = null,
) {
    fun isSuccess(): Boolean = sberOrderStatus == STATUS_SUCCESS

    private companion object {
        private const val STATUS_SUCCESS = 1
    }
}
