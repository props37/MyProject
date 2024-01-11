package ru.zarina.zarina.usecase.base

interface BasicUseCase<in P, out R> {
    operator fun invoke(params: P): R
}
