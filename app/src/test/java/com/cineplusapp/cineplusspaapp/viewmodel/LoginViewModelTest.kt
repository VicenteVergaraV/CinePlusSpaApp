package com.cineplusapp.cineplusspaapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import com.cineplusapp.cineplusspaapp.data.remote.dto.AuthTokens
import com.cineplusapp.cineplusspaapp.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
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
    fun `login exitoso debe retornar Success con AuthTokens`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val authTokens = AuthTokens("mock_token_12345", "mock_refresh_token")
        val successResult = Result.success(authTokens)

        coEvery { mockSessionManager.isUserRegistered(email) } returns true
        coEvery { mockSessionManager.validateLocalCredentials(email, password) } returns true
        coEvery { mockAuthRepository.login(email, password) } returns successResult

        var resultAccess: String? = null
        var resultRefresh: String? = null

        // When
        viewModel.login(email, password) { access, refresh ->
            resultAccess = access
            resultRefresh = refresh
        }
        testDispatcher.scheduler.advanceUntilIdle()


        // Then
        assertEquals("mock_token_12345", resultAccess)
        assertEquals("mock_refresh_token", resultRefresh)
        assertNull(viewModel.ui.error)
    }

    @Test
    fun `login con credenciales invalidas debe retornar Failure`() = runTest {
        // Given
        val email = "wrong@example.com"
        val password = "wrongpassword"
        val failureResult = Result.failure<AuthTokens>(Exception("Invalid credentials"))

        coEvery { mockSessionManager.isUserRegistered(email) } returns true
        coEvery { mockSessionManager.validateLocalCredentials(email, password) } returns true
        coEvery { mockAuthRepository.login(email, password) } returns failureResult

        // When
        viewModel.login(email, password) { _, _ -> }
        testDispatcher.scheduler.advanceUntilIdle()


        // Then
        assertNotNull(viewModel.ui.error)
        assertEquals("Invalid credentials", viewModel.ui.error)
    }

    @Test
    fun `login con error de red debe retornar Failure con mensaje de error`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password"
        val failureResult = Result.failure<AuthTokens>(Exception("Network error"))

        coEvery { mockSessionManager.isUserRegistered(email) } returns true
        coEvery { mockSessionManager.validateLocalCredentials(email, password) } returns true
        coEvery { mockAuthRepository.login(email, password) } returns failureResult

        // When
        viewModel.login(email, password) { _, _ -> }
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertNotNull(viewModel.ui.error)
        assertTrue(viewModel.ui.error!!.contains("Network error"))
    }
}
