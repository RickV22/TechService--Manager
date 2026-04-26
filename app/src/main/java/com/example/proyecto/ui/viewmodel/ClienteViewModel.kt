package com.example.proyecto.ui.viewmodel

import androidx.lifecycle.*
import com.example.proyecto.data.local.entity.Cliente
import com.example.proyecto.data.repository.ClienteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ClienteViewModel(private val repository: ClienteRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val clientes: StateFlow<List<Cliente>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) repository.getAllClientes()
            else repository.searchClientes(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val totalClientes: StateFlow<Int> = repository.getTotalClientes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    fun setSearchQuery(query: String) { _searchQuery.value = query }

    fun getClienteById(id: Long): Flow<Cliente?> = repository.getClienteById(id)

    fun insertCliente(cliente: Cliente) = viewModelScope.launch {
        repository.insertCliente(cliente)
    }

    fun updateCliente(cliente: Cliente) = viewModelScope.launch {
        repository.updateCliente(cliente)
    }

    fun deleteCliente(cliente: Cliente) = viewModelScope.launch {
        repository.deleteCliente(cliente)
    }
}

class ClienteViewModelFactory(private val repository: ClienteRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ClienteViewModel(repository) as T
    }
}