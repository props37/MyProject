package ru.livetyping.zarina.core.usecase

public interface UseCaseLogger {
    public fun v(tag: String, message: String)
    public fun v(tag: String, throwable: Throwable, message: String)
    public fun e(tag: String, message: String)
    public fun e(tag: String, throwable: Throwable, message: String)
}
