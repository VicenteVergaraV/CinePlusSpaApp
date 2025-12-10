package com.cineplusapp.cineplusspaapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cineplusapp.cineplusspaapp.data.local.SessionManager
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
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: LoginViewModel
    private lateinit var mockAuthRepository: AuthRepository
    private lateinit var mockSessionManager: SessionManager

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockAuthRepository = mockk()
        mockSessionManager =
            mockk(relaxed = true) // relaxed para que no se use la funcion saveAuthToken
        viewModel = LoginViewModel(mockAuthRepository, mockSessionManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login exitoso guarda token y limpia errores`() = runTest(testDispatcher) {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val tokens = AuthTokens(
            access = "fake-access-token",
            refresh = "fake-refresh-token"
        )

        coEvery { mockAuthRepository.login(email, password) } returns Result.success(tokens)

        // When
        viewModel.login(email, password)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.value

        // No está cargando
        Assert.assertFalse(finalState.loading)

        // No hay errores
        Assert.assertNull(finalState.emailError)
        Assert.assertNull(finalState.passwordError)
        Assert.assertNull(finalState.generalError)

        // Se guarda el token de acceso exactamente 1 vez
        coVerify(exactly = 1) { mockSessionManager.saveAuthToken(tokens.access) }
    }

    @Test
    fun `login con credenciales invalidas (401) muestra error`() = runTest(testDispatcher) {
        // Given
        val email = "wrong@example.com"
        val password = "wrongpassword"
        val errorResponse = "{\"error\":\"Unauthorized\"}".toResponseBody(null)
        val httpException = HttpException(Response.error<Any>(401, errorResponse))
        val failureResult = Result.failure<AuthTokens>(httpException)

        coEvery { mockAuthRepository.login(email, password) } returns failureResult

        // When
        viewModel.login(email, password)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.value
        Assert.assertFalse(finalState.loading)
        Assert.assertEquals("Correo o contraseña incorrectos.", finalState.generalError)
        coVerify(exactly = 0) { mockSessionManager.saveAuthToken(any()) }
    }

    @Test
    fun `login con error de red (IOException) muestra error`() = runTest(testDispatcher) {
        // Given
        val email = "test@example.com"
        val password = "password"
        val ioException = IOException("Network error")
        val failureResult = Result.failure<AuthTokens>(ioException)

        coEvery { mockAuthRepository.login(email, password) } returns failureResult

        // When
        viewModel.login(email, password)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.value
        Assert.assertFalse(finalState.loading)
        Assert.assertEquals(
            "No se pudo conectar. Verifica tu conexión a internet.",
            finalState.generalError
        )
        coVerify(exactly = 0) { mockSessionManager.saveAuthToken(any()) }
    }

    @Test
    fun `login con error de servidor (500) muestra error`() = runTest(testDispatcher) {
        // Given
        val email = "test@example.com"
        val password = "password"
        val errorResponse = "{\"error\":\"Server Error\"}".toResponseBody(null)
        val httpException = HttpException(Response.error<Any>(500, errorResponse))
        val failureResult = Result.failure<AuthTokens>(httpException)

        coEvery { mockAuthRepository.login(email, password) } returns failureResult

        // When
        viewModel.login(email, password)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.value
        Assert.assertFalse(finalState.loading)
        Assert.assertEquals(
            "El servidor está teniendo problemas. Intenta más tarde.",
            finalState.generalError
        )
        coVerify(exactly = 0) { mockSessionManager.saveAuthToken(any()) }
    }

    @Test
    fun `login con contraseña corta muestra error de validacion`() = runTest {
        // When
        viewModel.login("test@example.com", "123")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.value
        Assert.assertNull(finalState.emailError)
        Assert.assertEquals(
            "La contraseña debe tener al menos 6 caracteres",
            finalState.passwordError
        )
        Assert.assertNull(finalState.generalError)
        Assert.assertFalse(finalState.loading)
        coVerify(exactly = 0) { mockAuthRepository.login(any(), any()) }
    }
}