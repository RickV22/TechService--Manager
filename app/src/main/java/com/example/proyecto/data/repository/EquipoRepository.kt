package com.example.proyecto.data.repository

import com.example.proyecto.data.local.dao.EquipoDao
import com.example.proyecto.data.local.entity.Equipo
import kotlinx.coroutines.flow.Flow

class EquipoRepository(private val dao: EquipoDao) {

    fun getEquiposByCliente(clienteId: Long): Flow<List<Equipo>> =
        dao.getEquiposByCliente(clienteId)

    fun getEquipoById(id: Long): Flow<Equipo?> = dao.getEquipoById(id)

    fun getTotalEquipos(): Flow<Int> = dao.getTotalEquipos()

    suspend fun insertEquipo(equipo: Equipo): Long = dao.insertEquipo(equipo)

    suspend fun updateEquipo(equipo: Equipo) = dao.updateEquipo(equipo)

    suspend fun deleteEquipo(equipo: Equipo) = dao.deleteEquipo(equipo)
}