package ru.livetyping.zarina.core.network.util

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.reflect.KProperty

// TODO: [High] Rename to checkPropertyNotNull
@OptIn(ExperimentalContracts::class)
public inline fun <T> checkNotNull(value: T?, lazyProperty: () -> KProperty<*>): T {
    contract {
        returns() implies (value != null)
    }

    return kotlin.checkNotNull(value) { "Property \"${lazyProperty().name}\" is null" }
}
