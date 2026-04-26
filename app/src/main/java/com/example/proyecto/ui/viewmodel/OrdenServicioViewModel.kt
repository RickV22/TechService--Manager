package com.example.proyecto.ui.viewmodel

import androidx.lifecycle.*
import com.example.proyecto.data.local.entity.EstadoServicio
import com.example.proyecto.data.local.entity.OrdenServicio
import com.example.proyecto.data.repository.OrdenServicioRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class OrdenServicioViewModel(private val repository: OrdenServicioRepository) : ViewModel() {

    val totalOrdenes: StateFlow<Int> = repository.getTotalOrdenes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val pendientes: StateFlow<Int> = repository.countByEstado(EstadoServicio.PENDIENTE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val enProceso: StateFlow<Int> = repository.countByEstado(EstadoServicio.EN_PROCESO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val finalizados: StateFlow<Int> = repository.countByEstado(EstadoServicio.FINALIZADO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val totalIngresos: StateFlow<Double> = repository.getTotalIngresos()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    fun getOrdenesByEquipo(equipoId: Long): StateFlow<List<OrdenServicio>> =
        repository.getOrdenesByEquipo(equipoId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun getOrdenById(id: Long): Flow<OrdenServicio?> = repository.getOrdenById(id)

    fun insertOrden(orden: OrdenServicio) = viewModelScope.launch {
        repository.insertOrden(orden)
    }

    fun updateOrden(orden: OrdenServicio) = viewModelScope.launch {
        repository.updateOrden(orden)
    }

    fun deleteOrden(orden: OrdenServicio) = viewModelScope.launch {
        repository.deleteOrden(orden)
    }
}

class OrdenServicioViewModelFactory(private val repository: OrdenServicioRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return OrdenServicioViewModel(repository) as T
    }
}