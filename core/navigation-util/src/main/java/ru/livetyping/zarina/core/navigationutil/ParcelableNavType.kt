package ru.livetyping.zarina.core.navigationutil

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

/**
 * Consider using [parcelableNavType] function to create instances of
 * [ParcelableNavType].
 */
public open class ParcelableNavType<T : Parcelable?>(
    override val isNullableAllowed: Boolean,
    private val serializer: KSerializer<T>,
) : NavType<T>(isNullableAllowed) {

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putParcelable(key, value)
    }

    @Suppress("DEPRECATION")
    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): T {
        return Json.decodeFromString(serializer, value)
    }

    override fun serializeAsValue(value: T): String {
        return Uri.encode(Json.encodeToString(serializer, value))
    }
}

public inline fun <reified T : Parcelable?> parcelableNavType(
    isNullableAllowed: Boolean,
): ParcelableNavType<T> {
    return ParcelableNavType(
        isNullableAllowed = isNullableAllowed,
        serializer = kotlinx.serialization.serializer(),
    )
}
