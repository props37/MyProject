package ru.zarina.zarina.util.base.usecase

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.base.usecase.BasicUseCase
import ru.zarina.zarina.base.usecase.FlowUseCase
import ru.zarina.zarina.base.usecase.UseCase

@Suppress("NOTHING_TO_INLINE")
inline operator fun <T> BasicUseCase<Unit, T>.invoke(): T = this.invoke(Unit)

suspend inline operator fun <T> UseCase<Unit, T>.invoke(): Result<T> = this.invoke(Unit)

@Suppress("NOTHING_TO_INLINE")
inline operator fun <T> FlowUseCase<Unit, T>.invoke(): Flow<Result<T>> = this.invoke(Unit)
