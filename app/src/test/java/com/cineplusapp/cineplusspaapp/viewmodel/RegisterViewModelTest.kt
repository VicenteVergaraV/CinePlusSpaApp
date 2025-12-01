package com.cineplusapp.cineplusspaapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cineplusapp.cineplusspaapp.data.local.SessionManager
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RegisterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: RegisterViewModel
    private lateinit var mockSessionManager: SessionManager

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockSessionManager = mockk(relaxed = true)
        viewModel = RegisterViewModel(mockSessionManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `registro exitoso`() = runTest {
        // Given
        val name = "Test User"
        val email = "test@example.com"
        val password = "password123"

        coEvery { mockSessionManager.isUserRegistered(email) } returns false

        var onSuccessCalled = false

        // When
        viewModel.register(name, email, password) { onSuccessCalled = true }
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { mockSessionManager.registerUser(email, name, password) }
        assert(onSuccessCalled)
        assertNull(viewModel.ui.error)
        assert(viewModel.ui.done)
    }

    @Test
    fun `usuario ya registrado`() = runTest {
        // Given
        val name = "Test User"
        val email = "test@example.com"
        val password = "password123"

        coEvery { mockSessionManager.isUserRegistered(email) } returns true

        var onSuccessCalled = false

        // When
        viewModel.register(name, email, password) { onSuccessCalled = true }
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 0) { mockSessionManager.registerUser(any(), any(), any()) }
        assert(!onSuccessCalled)
        assertNotNull(viewModel.ui.error)
        assertEquals("El usuario ya está registrado.", viewModel.ui.error)
    }

    @Test
    fun `campos invalidos`() = runTest {
        // Given
        val name = ""
        val email = ""
        val password = ""

        var onSuccessCalled = false

        // When
        viewModel.register(name, email, password) { onSuccessCalled = true }
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 0) { mockSessionManager.registerUser(any(), any(), any()) }
        assert(!onSuccessCalled)
        assertNotNull(viewModel.ui.error)
        assertEquals("Completa todos los campos.", viewModel.ui.error)
    }
}
