package com.example.proyecto.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class EstadoServicio { PENDIENTE, EN_PROCESO, FINALIZADO }

@Entity(
    tableName = "ordenes_servicio",
    foreignKeys = [
        ForeignKey(
            entity = Equipo::class,
            parentColumns = ["id"],
            childColumns = ["equipoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("equipoId")]
)
data class OrdenServicio(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val equipoId: Long,
    val fechaIngreso: Long = System.currentTimeMillis(),
    val problemaReportado: String,
    val diagnosticoTecnico: String = "",
    val solucionAplicada: String = "",
    val costo: Double = 0.0,
    val estado: EstadoServicio = EstadoServicio.PENDIENTE,
    val fechaActualizacion: Long = System.currentTimeMillis()
)