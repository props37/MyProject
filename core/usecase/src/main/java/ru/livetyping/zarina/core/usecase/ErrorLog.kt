package ru.livetyping.zarina.core.usecase

internal fun getErrorLogMessage(className: String, params: Any?): String {
    return "Exception occurred while executing $className with parameters $params"
}
