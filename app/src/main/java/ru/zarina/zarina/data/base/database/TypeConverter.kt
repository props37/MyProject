package ru.zarina.zarina.data.base.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@ProvidedTypeConverter
class TypeConverter(
    private val json: Json,
) {

    @TypeConverter
    fun intListToString(value: List<Int>): String = json.encodeToString(value)

    @TypeConverter
    fun stringToIntList(value: String): List<Int> = json.decodeFromString(value)

}
