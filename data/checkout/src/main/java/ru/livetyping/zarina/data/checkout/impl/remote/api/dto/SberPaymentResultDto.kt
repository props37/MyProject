package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SberPaymentResultDto(
    @SerialName("sberOrderStatus")
    val sberOrderStatus: Int? = null,
) {
    fun isSuccess(): Boolean {
        return sberOrderStatus == STATUS_SUCCESS_1 || sberOrderStatus == STATUS_SUCCESS_2
    }

    private companion object {
        private const val STATUS_SUCCESS_1 = 1
        private const val STATUS_SUCCESS_2 = 2
    }
}
