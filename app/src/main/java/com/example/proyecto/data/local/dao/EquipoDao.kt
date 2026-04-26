package com.example.proyecto.data.local.dao

import androidx.room.*
import com.example.proyecto.data.local.entity.Equipo
import kotlinx.coroutines.flow.Flow

@Dao
interface EquipoDao {

    @Query("SELECT * FROM equipos WHERE clienteId = :clienteId ORDER BY fechaRegistro DESC")
    fun getEquiposByCliente(clienteId: Long): Flow<List<Equipo>>

    @Query("SELECT * FROM equipos WHERE id = :id")
    fun getEquipoById(id: Long): Flow<Equipo?>

    @Query("SELECT COUNT(*) FROM equipos")
    fun getTotalEquipos(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquipo(equipo: Equipo): Long

    @Update
    suspend fun updateEquipo(equipo: Equipo)

    @Delete
    suspend fun deleteEquipo(equipo: Equipo)
}