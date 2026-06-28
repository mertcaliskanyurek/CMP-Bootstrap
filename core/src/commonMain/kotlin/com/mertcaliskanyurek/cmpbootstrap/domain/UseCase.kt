package com.mertcaliskanyurek.cmpbootstrap.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class UseCase<in Params, out T>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend operator fun invoke(params: Params): Result<T> {
        return try {
            withContext(dispatcher) {
                Result.success(execute(params))
            }
        } catch (e: Exception) {
            val appError = e as? AppError ?: AppError.Unexpected(e)
            Result.failure(appError)
        }
    }

    protected abstract suspend fun execute(params: Params): T
}

abstract class NoParamUseCase<out T>(
    dispatcher: CoroutineDispatcher = Dispatchers.Default
): UseCase<Unit,T>(dispatcher)
