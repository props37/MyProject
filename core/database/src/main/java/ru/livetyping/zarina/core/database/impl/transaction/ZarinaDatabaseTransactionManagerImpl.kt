package ru.livetyping.zarina.core.database.impl.transaction

import androidx.room.withTransaction
import ru.livetyping.zarina.core.database.ZarinaDatabase2
import ru.livetyping.zarina.core.database.transaction.ZarinaDatabaseTransactionManager

internal class ZarinaDatabaseTransactionManagerImpl(
    private val database: ZarinaDatabase2,
) : ZarinaDatabaseTransactionManager {
    override suspend fun <R> withTransaction(block: suspend () -> R): R {
        return database.withTransaction(block)
    }
}
