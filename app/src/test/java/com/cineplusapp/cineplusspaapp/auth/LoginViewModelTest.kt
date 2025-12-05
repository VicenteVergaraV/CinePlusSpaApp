package com.cineplusapp.cineplusspaapp.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import com.cineplusapp.cineplusspaapp.data.remote.dto.AuthTokens
import com.cineplusapp.cineplusspaapp.domain.repository.AuthRepository
import com.cineplusapp.cineplusspaapp.viewmodel.LoginViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
        mockSessionManager = mockk(relaxed = true)
        viewModel = LoginViewModel(mockAuthRepository, mockSessionManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login exitoso`() = runTest(testDispatcher) {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val authTokens = AuthTokens(access = "mock_token_12345")
        val successResult = Result.success(authTokens)

        coEvery { mockAuthRepository.login(email, password) } returns successResult

        // When & Then
        viewModel.loginSuccess.test {
            viewModel.login(email, password)

            val successToken = awaitItem()
            assertEquals("mock_token_12345", successToken.access)

            val finalState = viewModel.uiState.value
            assertNull(finalState.error)

            coVerify { mockSessionManager.saveAuthToken("mock_token_12345") }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `credenciales invalidas`() = runTest(testDispatcher) {
        // Given
        val email = "wrong@example.com"
        val password = "wrongpassword"
        val failureResult = Result.failure<AuthTokens>(Exception("Credenciales inválidas"))

        coEvery { mockAuthRepository.login(email, password) } returns failureResult

        // When
        viewModel.login(email, password)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.value
        assertNotNull(finalState.error)
        assertEquals("Credenciales inválidas", finalState.error)
        coVerify(exactly = 0) { mockSessionManager.saveAuthToken(any()) }
    }

    @Test
    fun `error de red`() = runTest(testDispatcher) {
        // Given
        val email = "test@example.com"
        val password = "password"
        val failureResult = Result.failure<AuthTokens>(Exception("Error de red"))

        coEvery { mockAuthRepository.login(email, password) } returns failureResult

        // When
        viewModel.login(email, password)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.value
        assertNotNull(finalState.error)
        assertTrue(finalState.error!!.contains("Error de red"))
        coVerify(exactly = 0) { mockSessionManager.saveAuthToken(any()) }
    }
}
