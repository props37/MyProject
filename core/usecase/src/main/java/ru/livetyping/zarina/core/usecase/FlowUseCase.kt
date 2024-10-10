package ru.livetyping.zarina.core.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen

public abstract class FlowUseCase<in P, out R>(private val logger: UseCaseLogger?) {
    private val className by lazy { this.javaClass.simpleName ?: TAG }

    public operator fun invoke(params: P): Flow<Result<R>> = execute(params)
        .map { Result.success(it) }
        .retryWhen { t, attempt ->
            logError(t, params)
            emit(Result.failure(t))
            shouldRetry(t, attempt)
        }
        .catch { t ->
            logError(t, params)
            emit(Result.failure(t))
        }

    protected abstract fun execute(params: P): Flow<R>

    protected open suspend fun shouldRetry(exception: Throwable, attempt: Long): Boolean = false

    private fun logError(t: Throwable, params: P) {
        logger?.e(className, t, getErrorLogMessage(className, params))
    }

    private companion object {
        private const val TAG = "FlowUseCase"
    }
}
