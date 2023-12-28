package ru.zarina.zarina.utils.clean

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.usecase.base.FlowUseCase
import ru.zarina.zarina.usecase.base.UseCase

suspend operator fun <T> UseCase<Unit, T>.invoke(): Result<T> = this.invoke(Unit)

operator fun <T> FlowUseCase<Unit, T>.invoke(): Flow<Result<T>> = this.invoke(Unit)
