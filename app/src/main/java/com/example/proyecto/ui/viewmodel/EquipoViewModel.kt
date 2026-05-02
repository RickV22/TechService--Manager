package com.example.proyecto.ui.viewmodel

import androidx.lifecycle.*
import com.example.proyecto.data.local.entity.Equipo
import com.example.proyecto.data.repository.EquipoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EquipoViewModel(private val repository: EquipoRepository) : ViewModel() {

    fun getEquiposByCliente(clienteId: Long): StateFlow<List<Equipo>> =
        repository.getEquiposByCliente(clienteId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun getEquipoById(id: Long): Flow<Equipo?> = repository.getEquipoById(id)

    // val totalEquipos: StateFlow<Int> = repository.getTotalEquipos()
    //     .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    fun insertEquipo(equipo: Equipo) = viewModelScope.launch {
        repository.insertEquipo(equipo)
    }

    fun updateEquipo(equipo: Equipo) = viewModelScope.launch {
        repository.updateEquipo(equipo)
    }

    fun deleteEquipo(equipo: Equipo) = viewModelScope.launch {
        repository.deleteEquipo(equipo)
    }
}

class EquipoViewModelFactory(private val repository: EquipoRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return EquipoViewModel(repository) as T
    }
}