package com.cineplusapp.cineplusspaapp.repository

import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import com.cineplusapp.cineplusspaapp.data.remote.ApiService
import com.cineplusapp.cineplusspaapp.data.remote.dto.LoginRequest
import com.cineplusapp.cineplusspaapp.data.remote.dto.LoginResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

@ExperimentalCoroutinesApi
class AuthRepositoryTest {

    private lateinit var mockApiService: ApiService
    private lateinit var mockSessionManager: SessionManager
    private lateinit var repository: AuthRepository

    @Before
    fun setup() {
        mockApiService = mockk()
        mockSessionManager = mockk(relaxed = true)
        repository = AuthRepositoryImpl(mockApiService, mockSessionManager)
    }

    @Test
    fun loginSuccessfulShouldReturnSuccessWithAuthTokens() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val loginResponse = LoginResponse(
            id = 1,
            username = "testuser",
            email = email,
            firstName = "Test",
            lastName = "User",
            accessToken = "mock_token_12345",
            refreshToken = "mock_refresh_token"
        )
        coEvery { mockApiService.login(LoginRequest(email, password)) } returns loginResponse

        // When
        val result = repository.login(email, password)

        // Then
        assertTrue(result.isSuccess)
        val authTokens = result.getOrNull()
        assertEquals("mock_token_12345", authTokens?.access)
        assertEquals("mock_refresh_token", authTokens?.refresh)

        coVerify { mockSessionManager.saveTokens("mock_token_12345", "mock_refresh_token") }
    }

    @Test
    fun loginWithInvalidCredentialsShouldReturnFailure() = runTest {
        // Given
        val email = "wrong@example.com"
        val password = "wrongpassword"
        val errorResponse = "{\"error\":\"Invalid credentials\"}".toResponseBody(null)
        val httpException = HttpException(Response.error<Any>(401, errorResponse))
        coEvery { mockApiService.login(any()) } throws httpException

        // When
        val result = repository.login(email, password)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Invalid credentials", result.exceptionOrNull()?.message)
    }

    @Test
    fun loginWithNetworkErrorShouldReturnFailure() = runTest {
        // Given
        val ioException = IOException("Network error")
        coEvery { mockApiService.login(any()) } throws ioException

        // When
        val result = repository.login("test@example.com", "password")

        // Then
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}
