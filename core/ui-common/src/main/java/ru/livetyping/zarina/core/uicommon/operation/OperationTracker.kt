package ru.livetyping.zarina.core.uicommon.operation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.UUID

public class OperationTracker {
    private val ongoingOperations = MutableStateFlow(setOf<Operation>())

    public val ongoingOperationKeys: Flow<Set<OperationKey>> = ongoingOperations
        .map { operations ->
            operations.mapTo(mutableSetOf()) { operation -> operation.key }
        }

    public suspend fun <R> track(key: OperationKey, operation: suspend () -> R): R {
        val request = Operation(UUID.randomUUID().toString(), key)
        try {
            ongoingOperations.update { it + request }
            return operation()
        } finally {
            ongoingOperations.update { it - request }
        }
    }

    public fun isOperationOngoing(vararg keys: OperationKey): Flow<Boolean> {
        val keySet = keys.toSet()
        return ongoingOperationKeys.map { ongoingKeys ->
            ongoingKeys.any { it in keySet }
        }
    }
}
