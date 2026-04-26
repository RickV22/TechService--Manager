package com.example.proyecto.ui.viewmodel

import app.cash.turbine.test
import com.example.proyecto.data.local.entity.Cliente
import com.example.proyecto.data.repository.ClienteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ClienteViewModelTest {

    private lateinit var viewModel: ClienteViewModel
    private val repository: ClienteRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        // Mock default behavior for common flows
        every { repository.getAllClientes() } returns flowOf(emptyList())
        every { repository.getTotalClientes() } returns flowOf(0)
        
        viewModel = ClienteViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `clientes flow should emit all clientes when search query is blank`() = runTest {
        val clientes = listOf(
            Cliente(id = 1, nombre = "Juan", telefono = "123", email = "juan@test.com", direccion = "Calle 1"),
            Cliente(id = 2, nombre = "Maria", telefono = "456", email = "maria@test.com", direccion = "Calle 2")
        )
        every { repository.getAllClientes() } returns flowOf(clientes)

        viewModel.clientes.test {
            assertEquals(emptyList<Cliente>(), awaitItem()) // Initial state
            
            // Advance time for debounce
            testDispatcher.scheduler.advanceTimeBy(400)
            assertEquals(clientes, awaitItem())
        }
    }

    @Test
    fun `clientes flow should emit filtered results when search query is provided`() = runTest {
        val query = "Juan"
        val filteredClientes = listOf(
            Cliente(id = 1, nombre = "Juan", telefono = "123", email = "juan@test.com", direccion = "Calle 1")
        )
        every { repository.searchClientes(query) } returns flowOf(filteredClientes)

        viewModel.setSearchQuery(query)

        viewModel.clientes.test {
            assertEquals(emptyList<Cliente>(), awaitItem()) // Initial state
            
            // Advance time for debounce (300ms)
            testDispatcher.scheduler.advanceTimeBy(400)
            assertEquals(filteredClientes, awaitItem())
        }
    }

    @Test
    fun `totalClientes should reflect repository value`() = runTest {
        val totalFlow = MutableStateFlow(0)
        every { repository.getTotalClientes() } returns totalFlow
        
        viewModel = ClienteViewModel(repository)

        viewModel.totalClientes.test {
            assertEquals(0, awaitItem()) // Initial value from stateIn
            
            totalFlow.value = 10
            assertEquals(10, awaitItem())
        }
    }

    @Test
    fun `insertCliente should call repository insert`() = runTest {
        val cliente = Cliente(nombre = "Nuevo", telefono = "111", email = "nuevo@test.com", direccion = "Calle 3")
        coEvery { repository.insertCliente(cliente) } returns 1L

        viewModel.insertCliente(cliente)
        advanceUntilIdle()

        coVerify { repository.insertCliente(cliente) }
    }

    @Test
    fun `updateCliente should call repository update`() = runTest {
        val cliente = Cliente(id = 1, nombre = "Editado", telefono = "111", email = "edit@test.com", direccion = "Calle 1")
        coEvery { repository.updateCliente(cliente) } returns Unit

        viewModel.updateCliente(cliente)
        advanceUntilIdle()

        coVerify { repository.updateCliente(cliente) }
    }

    @Test
    fun `deleteCliente should call repository delete`() = runTest {
        val cliente = Cliente(id = 1, nombre = "Borrar", telefono = "111", email = "del@test.com", direccion = "Calle 1")
        coEvery { repository.deleteCliente(cliente) } returns Unit

        viewModel.deleteCliente(cliente)
        advanceUntilIdle()

        coVerify { repository.deleteCliente(cliente) }
    }
}
