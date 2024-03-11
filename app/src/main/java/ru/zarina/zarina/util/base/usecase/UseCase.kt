package ru.zarina.zarina.util.base.usecase

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.usecase.base.BasicUseCase
import ru.zarina.zarina.usecase.base.FlowUseCase
import ru.zarina.zarina.usecase.base.UseCase

operator fun <T> BasicUseCase<Unit, T>.invoke(): T = this.invoke(Unit)

suspend operator fun <T> UseCase<Unit, T>.invoke(): Result<T> = this.invoke(Unit)

operator fun <T> FlowUseCase<Unit, T>.invoke(): Flow<Result<T>> = this.invoke(Unit)
