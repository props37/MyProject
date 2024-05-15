package ru.livetyping.zarina.domain.validation

interface Validator<T> {
    fun validate(input: T)
}
