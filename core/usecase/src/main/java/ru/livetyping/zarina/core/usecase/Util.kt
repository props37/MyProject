package ru.livetyping.zarina.core.usecase

import kotlinx.coroutines.flow.Flow

public suspend inline fun <T> UseCase<Unit, T>.call(): Result<T> = this.call(Unit)

@Suppress("NOTHING_TO_INLINE")
public inline fun <T> FlowUseCase<Unit, T>.call(): Flow<Result<T>> = this.call(Unit)
