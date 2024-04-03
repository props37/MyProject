package ru.livetyping.zarina.data.old

import timber.log.Timber
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


object ApiContract {

    @OptIn(ExperimentalContracts::class)
    fun <T> isNotNull(
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

}
