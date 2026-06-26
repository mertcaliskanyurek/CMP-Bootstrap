package com.mertcaliskanyurek.cmpbootstrap.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FlowUseCaseTest {

    private class EmittingFlowUseCase(private val values: List<Int>) : NoParamFlowUseCase<Int>() {
        override fun execute(params: Unit): Flow<Int> = flow {
            values.forEach { emit(it) }
        }
    }

    private class ThrowingFlowUseCase : NoParamFlowUseCase<Int>() {
        override fun execute(params: Unit): Flow<Int> = flow {
            emit(1)
            throw RuntimeException("stream error")
        }
    }

    @Test
    fun `FlowUseCase wraps each emission in Result success`() = runTest {
        val results = EmittingFlowUseCase(listOf(10, 20, 30))().toList()
        assertEquals(3, results.size)
        assertTrue(results.all { it.isSuccess })
        assertEquals(listOf(10, 20, 30), results.map { it.getOrNull() })
    }

    @Test
    fun `FlowUseCase catches upstream exception and emits Result failure`() = runTest {
        val results = ThrowingFlowUseCase()().toList()
        assertEquals(2, results.size)
        assertTrue(results[0].isSuccess)
        assertEquals(1, results[0].getOrNull())
        assertTrue(results[1].isFailure)
        assertEquals("stream error", results[1].exceptionOrNull()?.message)
    }
}
