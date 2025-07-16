package ru.livetyping.zarina.core.database.transaction

import ru.livetyping.zarina.core.database.ZarinaDatabase2

public interface ZarinaDatabaseTransactionManager {
    public suspend fun <R> withTransaction(block: suspend () -> R): R

    public companion object {
        public fun createInstance(database: ZarinaDatabase2): ZarinaDatabaseTransactionManager {
            return ZarinaDatabaseTransactionManagerImpl(database)
        }
    }
}
