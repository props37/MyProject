package ru.zarina.zarina.utils.kotlin

import timber.log.Timber
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


@OptIn(ExperimentalContracts::class)
fun <T> tryCheckNotNull(
    value: T?,
    fieldName: String? = null,
): Boolean {
    contract { returns(true) implies (value != null) }
    return if (value == null) {
        if (fieldName != null)
            Timber.w("Unexpected null value for field $fieldName")
        else
            Timber.w("Unexpected null value")
        false
    } else {
        true
    }
}

fun all(vararg values: Boolean): Boolean = values.all { it }
