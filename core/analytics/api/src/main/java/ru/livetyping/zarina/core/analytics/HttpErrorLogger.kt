package ru.livetyping.zarina.core.analytics

public interface HttpErrorLogger {
    public fun logHttpError(error: HttpError)
}
