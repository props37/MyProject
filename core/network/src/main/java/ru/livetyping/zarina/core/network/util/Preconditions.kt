package ru.livetyping.zarina.core.network.util

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
public inline fun <T> checkPropertyNotNull(value: T?, propertyName: () -> String): T {
    contract {
        returns() implies (value != null)
    }

    return checkNotNull(value) { "Property \"${propertyName()}\" is null" }
}
