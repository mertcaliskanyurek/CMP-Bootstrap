package com.mertcaliskanyurek.cmpbootstrap.domain

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class UseCaseTest {

    // --- UseCase ---

    private class SuccessUseCase : UseCase<Int, String>() {
        override suspend fun execute(params: Int): String = "result-$params"
    }

    private class ThrowingUseCase : UseCase<Unit, String>() {
        override suspend fun execute(params: Unit): String = throw IllegalStateException("boom")
    }

    @Test
    fun `UseCase returns success wrapping the execute result`() = runTest {
        val result = SuccessUseCase()(42)
        assertTrue(result.isSuccess)
        assertEquals("result-42", result.getOrNull())
    }

    @Test
    fun `UseCase returns failure when execute throws`() = runTest {
        val result = ThrowingUseCase()(Unit)
        assertTrue(result.isFailure)
        assertIs<IllegalStateException>(result.exceptionOrNull())
        assertEquals("boom", result.exceptionOrNull()?.message)
    }

    // --- NoParamUseCase ---

    private class NoParamSuccess : NoParamUseCase<Int>() {
        override suspend fun execute(params: Unit): Int = 7
    }

    @Test
    fun `NoParamUseCase invokes with Unit and returns success`() = runTest {
        val result = NoParamSuccess()(Unit)
        assertTrue(result.isSuccess)
        assertEquals(7, result.getOrNull())
    }
}
