package ru.livetyping.zarina.core.database.impl.transaction

import androidx.room.withTransaction
import ru.livetyping.zarina.core.database.impl.database.ZarinaDatabase
import ru.livetyping.zarina.core.database.transaction.ZarinaDatabaseTransactionManager
import javax.inject.Inject

internal class ZarinaDatabaseTransactionManagerImpl @Inject constructor(
    private val database: ZarinaDatabase,
) : ZarinaDatabaseTransactionManager {
    override suspend fun <R> withTransaction(block: suspend () -> R): R {
        return database.withTransaction(block)
    }
}
