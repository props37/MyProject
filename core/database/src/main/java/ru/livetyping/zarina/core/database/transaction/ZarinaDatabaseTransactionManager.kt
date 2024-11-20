package ru.livetyping.zarina.core.database.transaction

public interface ZarinaDatabaseTransactionManager {
    public suspend fun <R> withTransaction(block: suspend () -> R): R
}
