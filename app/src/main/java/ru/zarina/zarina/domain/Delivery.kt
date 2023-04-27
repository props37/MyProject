package ru.zarina.zarina.domain

data class Delivery(
    val cityName: String,
    val options: List<Option>,
) {

    data class Option(
        val type: Type,
        val name: String,
        val estimatedTimeDays: Int,
    ) {
        enum class Type { EXPRESS, POST, PICKUP, RETAIL }
    }

}
