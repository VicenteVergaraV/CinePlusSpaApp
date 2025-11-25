package com.cineplusapp.cineplusspaapp.repository;


import android.content.Context
import android.content.SharedPreferences
import com.cineplusapp.cineplusspaapp.data.remote.ApiService
import com.cineplusapp.cineplusspaapp.data.remote.dto.*
import com.cineplusapp.cineplusspaapp.data.local.entity.User
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// Revisar en base al proyecto..
public class AuthRepositoryTest {
    //Mock
    private lateinit var mockContext: Context
    private lateinit var mockApiService: ApiService
    private lateinit var mockSharedPreferences: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor

    //SUT (System Under Test)
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
     //Crear mock
     mockContext = mockk(relaxed = true);
     mockApiService = mockk();
     mockSharedPreferences = mockk(relaxed = true);
     mockEditor = mockk(relaxed = true);

    //Configurar comportamiento de SharedPreferences
     every { mockContext.getSharedPreferences(any(), any()) } returns mockSharedPreferences;
     every { mockSharedPreferences.edit() } returns mockEditor;
     every { mockEditor.putString(any(), any()) } returns mockEditor;
     every { mockEditor.apply() } just Runs;
     every { mockEditor.clear() } returns mockEditor;

     // Mockear RetroFitClient
     mockkObject(com.cineplusapp.cineplusspaapp.data.remote.RetrofitClient);
     every { com.cineplusapp.cineplusspaapp.data.remote.RetrofitClient.ApiService } returns mockApiService;

     // Crear Instancia del Repository
     repository = AuthRepository(mockContext);
    }

    @After
    fun tearDown() {
        // Limpiar mocks
        unmockkAll();
    }

    @Test
    fun 'login exitoso debe retornar Succes con User'() = runTest {
        // Datos de Prueba
        val email = "test@example.com";
        val password = "password123";

        val userDto = UserDto(
            id = 1,
            username = "Test User",
            email = email,
            firstName = "Test",
            lastName = "User",
            image = null
        )

        val authData = AuthData(
           user = userDto,
           accesstoken = "mock_token_12345"
        )

        val apiResponse = ApiResponse(
           success = true,
           message = "Login Exitoso",
           data(authData)
        )

        val response = Response.success(apiResponse)

        // Configurar mock para retornar la respuesta exitosa
        coEvery {
            mockApiService.login(
                    LoginRequest(email, password)
            )
        } returns response

        // When - Ejecutar login
        val result = repository.login(email, password)

        // Then - Verificar el resultado
        assertTrue(result.isSuccess, "El resultado debe ser Success")
    }
}
