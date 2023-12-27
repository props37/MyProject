package ru.zarina.zarina.utils.clean

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.base.clean.FlowUseCase
import ru.zarina.zarina.base.clean.UseCase

suspend operator fun <T> UseCase<Unit, T>.invoke(): Result<T> = this.invoke(Unit)

operator fun <T> FlowUseCase<Unit, T>.invoke(): Flow<Result<T>> = this.invoke(Unit)
