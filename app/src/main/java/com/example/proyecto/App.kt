package com.example.proyecto

import android.app.Application
import com.example.proyecto.data.local.AppDatabase
import com.example.proyecto.data.local.PreferencesManager
import com.example.proyecto.data.repository.ClienteRepository
import com.example.proyecto.data.repository.EquipoRepository
import com.example.proyecto.data.repository.OrdenServicioRepository

class App : Application() {

    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

    val clienteRepository: ClienteRepository by lazy {
        ClienteRepository(database.clienteDao())
    }

    val equipoRepository: EquipoRepository by lazy {
        EquipoRepository(database.equipoDao())
    }

    val ordenServicioRepository: OrdenServicioRepository by lazy {
        OrdenServicioRepository(database.ordenServicioDao())
    }

    val preferencesManager: PreferencesManager by lazy {
        PreferencesManager(this)
    }
}