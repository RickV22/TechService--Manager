package com.example.proyecto.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.proyecto.data.local.dao.ClienteDao
import com.example.proyecto.data.local.dao.EquipoDao
import com.example.proyecto.data.local.dao.OrdenServicioDao
import com.example.proyecto.data.local.entity.Cliente
import com.example.proyecto.data.local.entity.Equipo
import com.example.proyecto.data.local.entity.EstadoServicio
import com.example.proyecto.data.local.entity.OrdenServicio

class Converters {
    @TypeConverter
    fun fromEstado(estado: EstadoServicio): String = estado.name

    @TypeConverter
    fun toEstado(value: String): EstadoServicio = EstadoServicio.valueOf(value)
}

@Database(
    entities = [Cliente::class, Equipo::class, OrdenServicio::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun clienteDao(): ClienteDao
    abstract fun equipoDao(): EquipoDao
    abstract fun ordenServicioDao(): OrdenServicioDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "techservice_db"
                ).build().also { INSTANCE = it }
            }
    }
}