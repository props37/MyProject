package ru.livetyping.zarina.core.domain.validation

public interface Validator<T> {
    public fun validate(input: T)
}
