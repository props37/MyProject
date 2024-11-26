package ru.livetyping.zarina.core.network.util

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.reflect.KProperty

@OptIn(ExperimentalContracts::class)
public inline fun <T> checkPropertyNotNull(value: T?, lazyProperty: () -> KProperty<*>): T {
    contract {
        returns() implies (value != null)
    }

    return checkNotNull(value) { "Property \"${lazyProperty().name}\" is null" }
}
