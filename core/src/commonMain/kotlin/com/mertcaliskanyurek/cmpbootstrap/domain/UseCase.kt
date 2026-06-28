package com.mertcaliskanyurek.cmpbootstrap.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

abstract class UseCase<in Params, out T>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend operator fun invoke(params: Params): Result<T> {
        return try {
            withContext(dispatcher) {
                Result.success(execute(params))
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            val appError = e as? AppError ?: AppError.Unexpected(e)
            Result.failure(appError)
        }
    }

    protected abstract suspend fun execute(params: Params): T
}

abstract class NoParamUseCase<out T>(
    dispatcher: CoroutineDispatcher = Dispatchers.Default
): UseCase<Unit,T>(dispatcher) {
    suspend operator fun invoke(): Result<T> = invoke(Unit)
}

abstract class NoResultUseCase<in Params>(
    dispatcher: CoroutineDispatcher = Dispatchers.Default
): UseCase<Params, Unit>(dispatcher)
