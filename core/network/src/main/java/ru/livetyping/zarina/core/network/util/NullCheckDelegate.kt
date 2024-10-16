package ru.livetyping.zarina.core.network.util

import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

public class NullCheckDelegate<T, V>(private val value: V?) : ReadOnlyProperty<T, V> {
    override fun getValue(thisRef: T, property: KProperty<*>): V {
        checkNotNull(value) { "Property \"${property.name}\" is null" }
        return value
    }
}

@Suppress("UnusedReceiverParameter", "NOTHING_TO_INLINE")
public inline fun <T, V> Delegates.checkNotNull(value: V?): ReadOnlyProperty<T, V> {
    return NullCheckDelegate(value)
}
