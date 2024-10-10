package ru.livetyping.zarina.core.usecase

import kotlin.coroutines.cancellation.CancellationException
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.milliseconds

public abstract class UseCase<in P, out R>(private val logger: UseCaseLogger?) {
    private val className by lazy { this.javaClass.simpleName ?: TAG }

    public suspend operator fun invoke(params: P): Result<R> {
        return try {
            val executionResult: R
            val executionDuration = measureTimeMillis {
                executionResult = execute(params)
            }.milliseconds
            logger?.v(className, "Execution of $className took $executionDuration")
            Result.success(executionResult)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logger?.e(className, e, getErrorLogMessage(className, params))
            Result.failure(e)
        }
    }

    protected abstract suspend fun execute(params: P): R

    private companion object {
        private const val TAG = "UseCase"
    }
}
