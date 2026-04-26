package com.example.proyecto.data.repository

import com.example.proyecto.data.local.dao.ClienteDao
import com.example.proyecto.data.local.entity.Cliente
import kotlinx.coroutines.flow.Flow

class ClienteRepository(private val dao: ClienteDao){

    fun getAllClientes(): Flow<List<Cliente>> = dao.getAllClientes()

    fun getClienteById(id: Long): Flow<Cliente?> = dao.getClienteById(id)

    fun searchClientes(query: String): Flow<List<Cliente>> = dao.searchClientes(query)

    fun getTotalClientes(): Flow<Int> = dao.getTotalClientes()

    suspend fun insertCliente(cliente: Cliente): Long = dao.insertCliente(cliente)

    suspend fun updateCliente(cliente: Cliente) = dao.updateCliente(cliente)

    suspend fun deleteCliente(cliente: Cliente) = dao.deleteCliente(cliente)
}