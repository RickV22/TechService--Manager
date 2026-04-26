package com.example.proyecto.data.repository

import com.example.proyecto.data.local.dao.OrdenServicioDao
import com.example.proyecto.data.local.entity.EstadoServicio
import com.example.proyecto.data.local.entity.OrdenServicio
import kotlinx.coroutines.flow.Flow

class OrdenServicioRepository(private val dao: OrdenServicioDao) {

    fun getOrdenesByEquipo(equipoId: Long): Flow<List<OrdenServicio>> =
        dao.getOrdenesByEquipo(equipoId)

    fun getOrdenById(id: Long): Flow<OrdenServicio?> = dao.getOrdenById(id)

    fun getOrdenesByEstado(estado: EstadoServicio): Flow<List<OrdenServicio>> =
        dao.getOrdenesByEstado(estado)

    fun countByEstado(estado: EstadoServicio): Flow<Int> = dao.countByEstado(estado)

    fun getTotalIngresos(): Flow<Double?> = dao.getTotalIngresos()

    fun getTotalOrdenes(): Flow<Int> = dao.getTotalOrdenes()

    suspend fun insertOrden(orden: OrdenServicio): Long = dao.insertOrden(orden)

    suspend fun updateOrden(orden: OrdenServicio) = dao.updateOrden(orden)

    suspend fun deleteOrden(orden: OrdenServicio) = dao.deleteOrden(orden)
}