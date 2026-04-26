package com.example.proyecto.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "techservice_prefs")

data class UserPreferences(
    val nombreTecnico: String,
    val temaOscuro: Boolean,
    val moneda: String,
    val notificaciones: Boolean,
    val ordenRegistros: String
)

class PreferencesManager(private val context: Context) {

    companion object {
        val NOMBRE_TECNICO  = stringPreferencesKey("nombre_tecnico")
        val TEMA_OSCURO     = booleanPreferencesKey("tema_oscuro")
        val MONEDA          = stringPreferencesKey("moneda")
        val NOTIFICACIONES  = booleanPreferencesKey("notificaciones")
        val ORDEN_REGISTROS = stringPreferencesKey("orden_registros")
    }

    val preferencias: Flow<UserPreferences> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { prefs ->
            UserPreferences(
                nombreTecnico  = prefs[NOMBRE_TECNICO]  ?: "Técnico",
                temaOscuro = prefs[TEMA_OSCURO] ?: false,
                moneda = prefs[MONEDA] ?: "USD",
                notificaciones = prefs[NOTIFICACIONES]  ?: true,
                ordenRegistros = prefs[ORDEN_REGISTROS] ?: "ASC"
            )
        }

    suspend fun setNombreTecnico(nombre: String) {
        context.dataStore.edit { it[NOMBRE_TECNICO] = nombre }
    }

    suspend fun setTemaOscuro(oscuro: Boolean) {
        context.dataStore.edit { it[TEMA_OSCURO] = oscuro }
    }

    suspend fun setMoneda(moneda: String) {
        context.dataStore.edit { it[MONEDA] = moneda }
    }

    suspend fun setNotificaciones(activo: Boolean) {
        context.dataStore.edit { it[NOTIFICACIONES] = activo }
    }

    suspend fun setOrdenRegistros(orden: String) {
        context.dataStore.edit { it[ORDEN_REGISTROS] = orden }
    }
}