package com.cineplusapp.cineplusspaapp.auth

import com.cineplusapp.cineplusspaapp.data.remote.ApiService
import com.cineplusapp.cineplusspaapp.data.remote.dto.AuthUserDto
import com.cineplusapp.cineplusspaapp.data.remote.dto.LoginRequest
import com.cineplusapp.cineplusspaapp.data.remote.dto.LoginResponse
import com.cineplusapp.cineplusspaapp.repository.impl.AuthRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class AuthRepositoryTest {

    private lateinit var mockApiService: ApiService
    private lateinit var repository: AuthRepository

    @Before
    fun setup() {
        mockApiService = mockk()
        repository = AuthRepositoryImpl(mockApiService)
    }

    @Test
    fun `login exitoso 200`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val loginResponse = LoginResponse(
            user = AuthUserDto(_id = "1", email = email, role = "user", nombre = "Test User"),
            accessToken = "mock_token_12345"
        )
        coEvery { mockApiService.login(LoginRequest(email, password)) } returns loginResponse

        // When
        val result = repository.login(email, password)

        // Then
        assertTrue(result.isSuccess)
        val authTokens = result.getOrNull()
        assertEquals("mock_token_12345", authTokens?.access)
    }

    @Test
    fun `login success with empty token returns failure`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val loginResponse = LoginResponse(
            user = AuthUserDto(_id = "1", email = email, role = "user", nombre = "Test User"),
            accessToken = ""
        )
        coEvery { mockApiService.login(LoginRequest(email, password)) } returns loginResponse

        // When
        val result = repository.login(email, password)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalStateException)
    }

    @Test
    fun `credenciales invalidas`() = runTest {
        // Given
        val email = "wrong@example.com"
        val password = "wrongpassword"
        val errorResponse = "{\"error\":\"Credenciales inválidas\"}".toResponseBody(null)
        val httpException = HttpException(Response.error<Any>(401, errorResponse))
        coEvery { mockApiService.login(any()) } throws httpException

        // When
        val result = repository.login(email, password)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is HttpException)
    }

    @Test
    fun `error de red`() = runTest {
        // Given
        val ioException = IOException("Network error")
        coEvery { mockApiService.login(any()) } throws ioException

        // When
        val result = repository.login("test@example.com", "password")

        // Then
        assertTrue(result.isFailure)
        assertEquals(ioException, result.exceptionOrNull())
    }
}
