package com.example.proyecto.data.local.dao

import androidx.room.*
import com.example.proyecto.data.local.entity.EstadoServicio
import com.example.proyecto.data.local.entity.OrdenServicio
import kotlinx.coroutines.flow.Flow

@Dao
interface OrdenServicioDao {

    @Query("SELECT * FROM ordenes_servicio WHERE equipoId = :equipoId ORDER BY fechaIngreso DESC")
    fun getOrdenesByEquipo(equipoId: Long): Flow<List<OrdenServicio>>

    @Query("SELECT * FROM ordenes_servicio WHERE id = :id")
    fun getOrdenById(id: Long): Flow<OrdenServicio?>

    @Query("SELECT * FROM ordenes_servicio WHERE estado = :estado ORDER BY fechaIngreso DESC")
    fun getOrdenesByEstado(estado: EstadoServicio): Flow<List<OrdenServicio>>

    @Query("SELECT COUNT(*) FROM ordenes_servicio WHERE estado = :estado")
    fun countByEstado(estado: EstadoServicio): Flow<Int>

    @Query("SELECT SUM(costo) FROM ordenes_servicio WHERE estado = 'FINALIZADO'")
    fun getTotalIngresos(): Flow<Double?>

    @Query("SELECT COUNT(*) FROM ordenes_servicio")
    fun getTotalOrdenes(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrden(orden: OrdenServicio): Long

    @Update
    suspend fun updateOrden(orden: OrdenServicio)

    @Delete
    suspend fun deleteOrden(orden: OrdenServicio)
}