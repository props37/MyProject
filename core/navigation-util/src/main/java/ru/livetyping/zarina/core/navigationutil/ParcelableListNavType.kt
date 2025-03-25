package ru.livetyping.zarina.core.navigationutil

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ArraySerializer
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

/**
 * Consider using [parcelableListNavType] function to create instances of
 * [ParcelableListNavType].
 */
public open class ParcelableListNavType<T : Parcelable>(
    override val isNullableAllowed: Boolean,
    private val itemClass: KClass<T>,
    serializer: KSerializer<T>,
) : NavType<List<T>>(isNullableAllowed) {

    @OptIn(ExperimentalSerializationApi::class)
    private val arraySerializer = ArraySerializer(itemClass, serializer)

    override fun put(bundle: Bundle, key: String, value: List<T>) {
        val array = value.toTypedArray()
        bundle.putParcelableArray(key, array)
    }

    @Suppress("DEPRECATION", "UNCHECKED_CAST")
    override fun get(bundle: Bundle, key: String): List<T>? {
        val array = bundle[key] as Array<T>
        return array.toList()
    }

    override fun parseValue(value: String): List<T> {
        val array = Json.decodeFromString(arraySerializer, value)
        return array.toList()
    }

    override fun serializeAsValue(value: List<T>): String {
        val array = value.toTypedArray()
        return Uri.encode(Json.encodeToString(arraySerializer, array))
    }

    @Suppress("UNCHECKED_CAST", "PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    private fun List<T>.toTypedArray(): Array<T> {
        val array = java.lang.reflect.Array.newInstance(itemClass.java, this.size) as Array<T>
        return (this as java.util.Collection<T>).toArray(array)
    }
}

public inline fun <reified T : Parcelable> parcelableListNavType(
    isNullableAllowed: Boolean,
): ParcelableListNavType<T> {
    return ParcelableListNavType(
        isNullableAllowed = isNullableAllowed,
        itemClass = T::class,
        serializer = kotlinx.serialization.serializer(),
    )
}
