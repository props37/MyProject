package ru.livetyping.zarina.domain.old

import kotlinx.collections.immutable.ImmutableList

data class DeliveryAvailability(
    val cityName: String,
    val options: ImmutableList<Option>,
) {

    data class Option(
        val type: Type,
        val name: String,
        val estimatedTimeDays: Int,
    ) {
        enum class Type { EXPRESS, POST, PICKUP, RETAIL }
    }

}
