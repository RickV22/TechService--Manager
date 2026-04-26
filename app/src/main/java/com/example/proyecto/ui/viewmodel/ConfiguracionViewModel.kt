package com.example.proyecto.ui.viewmodel

import androidx.lifecycle.*
import com.example.proyecto.data.local.PreferencesManager
import com.example.proyecto.data.local.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ConfiguracionViewModel(private val preferencesManager: PreferencesManager) : ViewModel() {

    val preferencias: StateFlow<UserPreferences> = preferencesManager.preferencias
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UserPreferences("Técnico", false, "USD", true, "ASC")
        )

    fun setNombreTecnico(nombre: String) = viewModelScope.launch {
        preferencesManager.setNombreTecnico(nombre)
    }

    fun setTemaOscuro(oscuro: Boolean) = viewModelScope.launch {
        preferencesManager.setTemaOscuro(oscuro)
    }

    fun setMoneda(moneda: String) = viewModelScope.launch {
        preferencesManager.setMoneda(moneda)
    }

    fun setNotificaciones(activo: Boolean) = viewModelScope.launch {
        preferencesManager.setNotificaciones(activo)
    }

    fun setOrdenRegistros(orden: String) = viewModelScope.launch {
        preferencesManager.setOrdenRegistros(orden)
    }
}

class ConfiguracionViewModelFactory(private val preferencesManager: PreferencesManager) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ConfiguracionViewModel(preferencesManager) as T
    }
}