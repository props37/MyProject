package ru.livetyping.zarina.base.usecase

interface BasicUseCase<in P, out R> {
    operator fun invoke(params: P): R
}
