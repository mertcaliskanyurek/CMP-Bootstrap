package com.mertcaliskanyurek.cmpbootstrap.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

/**
 * Base class for use cases that return a [Flow] of [Result]-wrapped values.
 *
 * Each emission is wrapped in [Result.success], and any exception from the
 * upstream flow is caught and emitted as [Result.failure] — consistent with
 * [UseCase]'s error handling pattern.
 *
 * Use this for reactive/streaming operations (e.g. observing database changes).
 * For one-shot operations, use [UseCase] instead.
 */
abstract class FlowUseCase<in Params, out T>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    operator fun invoke(params: Params): Flow<Result<T>> =
        execute(params)
            .map { Result.success(it) }
            .catch { e ->
                val appError = e as? AppError ?: AppError.Unexpected(e)
                emit(Result.failure(appError))
            }
            .flowOn(dispatcher)

    protected abstract fun execute(params: Params): Flow<T>
}

abstract class NoParamFlowUseCase<out T>(
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) : FlowUseCase<Unit, T>(dispatcher) {

    operator fun invoke(): Flow<Result<T>> = invoke(Unit)
}
