package ru.livetyping.zarina.util.library.navigation

import androidx.navigation.NavType
import kotlin.reflect.KType
import kotlin.reflect.typeOf

inline fun <reified T : Enum<T>> getTypeMapEnumTypePair(): Pair<KType, NavType<T>> {
    return typeOf<T>() to NavType.EnumType(T::class.java)
}
