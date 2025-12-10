package com.cineplusapp.cineplusspaapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cineplusapp.cineplusspaapp.data.remote.dto.AuthTokens
import com.cineplusapp.cineplusspaapp.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class RegisterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: RegisterViewModel
    private lateinit var mockAuthRepository: AuthRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockAuthRepository = mockk()
        viewModel = RegisterViewModel(mockAuthRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `registro exitoso actualiza estado y llama onSuccess`() = runTest {
        // Given
        val name = "Test User"
        val email = "test@example.com"
        val password = "password123"

        val tokens = AuthTokens(
            access = "fake-access-token",
            refresh = "fake-refresh-token"
        )

        // El repositorio responde OK
        coEvery {
            mockAuthRepository.register(
                email,
                password,
                name
            )
        } returns Result.success(tokens)

        var onSuccessCalled = false

        // When
        viewModel.register(name, email, password) {
            onSuccessCalled = true
        }
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.ui

        // Se ejecutó el callback de éxito
        Assert.assertTrue(onSuccessCalled)

        // Estado final correcto
        Assert.assertTrue(finalState.done)
        Assert.assertFalse(finalState.loading)
        Assert.assertNull(finalState.nameError)
        Assert.assertNull(finalState.emailError)
        Assert.assertNull(finalState.passwordError)
        Assert.assertNull(finalState.generalError)

        // El repositorio se llamó exactamente una vez
        coVerify(exactly = 1) {
            mockAuthRepository.register(email, password, name)
        }
    }


    @Test
    fun `registro con email existente (409) muestra error`() = runTest {
        // Given
        val name = "Test User"
        val email = "existing@example.com"
        val password = "password123"
        val errorResponse = "{\"error\":\"Conflict\"}".toResponseBody(null)
        val httpException = HttpException(Response.error<Any>(409, errorResponse))
        val failureResult = Result.failure<AuthTokens>(httpException)
        var onSuccessCalled = false

        coEvery { mockAuthRepository.register(email, password, name) } returns failureResult

        // When
        viewModel.register(name, email, password) { onSuccessCalled = true }
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        Assert.assertFalse(onSuccessCalled)
        val finalState = viewModel.ui
        Assert.assertFalse(finalState.done)
        Assert.assertFalse(finalState.loading)
        Assert.assertEquals(
            "Ya existe una cuenta registrada con este correo.",
            finalState.generalError
        )
    }

    @Test
    fun `registro con error de red (IOException) muestra error`() = runTest {
        // Given
        val name = "Test User"
        val email = "test@example.com"
        val password = "password123"
        val ioException = IOException("Network Error")
        val failureResult = Result.failure<AuthTokens>(ioException)
        var onSuccessCalled = false

        coEvery { mockAuthRepository.register(email, password, name) } returns failureResult

        // When
        viewModel.register(name, email, password) { onSuccessCalled = true }
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        Assert.assertFalse(onSuccessCalled)
        val finalState = viewModel.ui
        Assert.assertFalse(finalState.done)
        Assert.assertFalse(finalState.loading)
        Assert.assertEquals(
            "No se pudo conectar. Revisa tu conexión a internet.",
            finalState.generalError
        )
    }

    @Test
    fun `registro con nombre vacio muestra error de validacion`() = runTest {
        // When
        viewModel.register("", "test@example.com", "password123") {}
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.ui
        Assert.assertEquals("El nombre es obligatorio.", finalState.nameError)
        Assert.assertNull(finalState.emailError)
        Assert.assertNull(finalState.passwordError)
        Assert.assertFalse(finalState.loading)
        Assert.assertFalse(finalState.done)
    }

    @Test
    fun `registro con email invalido muestra error de validacion`() = runTest {
        // When
        viewModel.register("Test User", "invalid-email", "password123") {}
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.ui
        Assert.assertNull(finalState.nameError)
        Assert.assertEquals("Email no válido.", finalState.emailError)
        Assert.assertNull(finalState.passwordError)
    }

    @Test
    fun `registro con contraseña corta muestra error de validacion`() = runTest {
        // When
        viewModel.register("Test User", "test@example.com", "123") {}
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.ui
        Assert.assertNull(finalState.nameError)
        Assert.assertNull(finalState.emailError)
        Assert.assertEquals(
            "La contraseña debe tener al menos 6 caracteres.",
            finalState.passwordError
        )
    }
}