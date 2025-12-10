package com.cineplusapp.cineplusspaapp.auth

import com.cineplusapp.cineplusspaapp.data.local.SessionManager
import com.cineplusapp.cineplusspaapp.data.remote.ApiService
import com.cineplusapp.cineplusspaapp.data.remote.dto.ApiResponse
import com.cineplusapp.cineplusspaapp.data.remote.dto.AuthTokens
import com.cineplusapp.cineplusspaapp.data.remote.dto.LoginRequest
import com.cineplusapp.cineplusspaapp.data.remote.dto.LoginResponse
import com.cineplusapp.cineplusspaapp.data.remote.dto.RegisterRequest
import com.cineplusapp.cineplusspaapp.data.remote.dto.RegisterResponse
import com.cineplusapp.cineplusspaapp.data.remote.dto.UserDto
import com.cineplusapp.cineplusspaapp.domain.repository.AuthRepository
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
    private lateinit var mockSessionManager: SessionManager
    private lateinit var repository: AuthRepository

    @Before
    fun setup() {
        mockApiService = mockk()
        mockSessionManager = mockk(relaxed = true)
        repository = AuthRepositoryImpl(mockApiService, mockSessionManager)
    }

    @Test
    fun `login exitoso 200`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val userDto = UserDto(id = "1", email = email, role = "USUARIO")
        val loginResponse = LoginResponse(user = userDto, accessToken = "mock_token_12345")
        val apiResponse = ApiResponse(success = true, message = "OK", data = loginResponse)

        coEvery { mockApiService.login(LoginRequest(email, password)) } returns apiResponse

        // When
        val result = repository.login(email, password)

        // Then
        assertTrue(result.isSuccess)
        val authTokens: AuthTokens? = result.getOrNull()
        assertEquals("mock_token_12345", authTokens?.access)
    }

    @Test
    fun `login con credenciales invalidas 401`() = runTest {
        // Given
        val email = "wrong@example.com"
        val password = "wrongpassword"
        val errorResponse = "{\"error\":\"Unauthorized\"}".toResponseBody(null)
        val httpException = HttpException(Response.error<Any>(401, errorResponse))

        coEvery { mockApiService.login(any()) } throws httpException

        // When
        val result = repository.login(email, password)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is HttpException)
    }
    
    @Test
    fun `login con respuesta de API no exitosa`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password"
        val apiResponse = ApiResponse<LoginResponse>(success = false, message = "Credenciales inválidas", data = null)
        coEvery { mockApiService.login(any()) } returns apiResponse

        // When
        val result = repository.login(email, password)

        // Then
        assertTrue(result.isFailure)
        val ex = result.exceptionOrNull()
        assertTrue(ex is RuntimeException)
        assertEquals("Credenciales inválidas", ex?.message)
    }

    @Test
    fun `login con error de red`() = runTest {
        // Given
        val ioException = IOException("Network error")
        coEvery { mockApiService.login(any()) } throws ioException

        // When
        val result = repository.login("test@example.com", "password")

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IOException)
    }

    @Test
    fun `register exitoso 201`() = runTest {
        // Given
        val name = "Test User"
        val email = "test@example.com"
        val password = "password123"
        val userDto = UserDto(id = "2", email = email, role = "USUARIO")
        val registerResponse = RegisterResponse(user = userDto, accessToken = "new_mock_token")
        val apiResponse = ApiResponse(success = true, message = "Created", data = registerResponse)

        coEvery {
            mockApiService.register(
                RegisterRequest(email = email, password = password, role = "USUARIO", nombre = name)
            )
        } returns apiResponse

        // When
        val result = repository.register(email, password, name)

        // Then
        assertTrue(result.isSuccess)
        val authTokens: AuthTokens? = result.getOrNull()
        assertEquals("new_mock_token", authTokens?.access)
    }

    @Test
    fun `register con email existente 409`() = runTest {
        // Given
        val name = "Test User"
        val email = "existing@example.com"
        val password = "password123"
        val errorResponse = "{\"error\":\"Conflict\"}".toResponseBody(null)
        val httpException = HttpException(Response.error<Any>(409, errorResponse))
        
        coEvery { mockApiService.register(any()) } throws httpException

        // When
        val result = repository.register(email, password, name)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is HttpException)
    }

    @Test
    fun `register con datos invalidos 400`() = runTest {
        // Given
        val name = ""
        val email = "invalid-email"
        val password = "123"
        val errorResponse = "{\"error\":\"Bad Request\"}".toResponseBody(null)
        val httpException = HttpException(Response.error<Any>(400, errorResponse))
        
        coEvery { mockApiService.register(any()) } throws httpException

        // When
        val result = repository.register(email, password, name)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is HttpException)
    }
}
