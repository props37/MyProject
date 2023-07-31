package ru.zarina.zarina.utils.clean

import ru.zarina.zarina.base.clean.FlowUseCase
import ru.zarina.zarina.base.clean.UseCase

suspend operator fun <T> UseCase<Unit, T>.invoke() = this.invoke(Unit)

operator fun <T> FlowUseCase<Unit, T>.invoke() = this.invoke(Unit)
