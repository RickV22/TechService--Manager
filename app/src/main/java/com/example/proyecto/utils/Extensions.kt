package com.example.proyecto.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.proyecto.data.local.entity.EstadoServicio
import com.example.proyecto.data.local.entity.OrdenServicio
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.net.toUri

fun Context.llamarCliente(telefono: String) {
    val intent = Intent(Intent.ACTION_DIAL, "tel:$telefono".toUri())
    startActivity(intent)
}

fun Context.enviarEmail(email: String, asunto: String, cuerpo: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:".toUri()
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        putExtra(Intent.EXTRA_SUBJECT, asunto)
        putExtra(Intent.EXTRA_TEXT, cuerpo)
    }
    startActivity(Intent.createChooser(intent, "Enviar reporte"))
}

fun Context.compartirOrden(resumen: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, resumen)
    }
    startActivity(Intent.createChooser(intent, "Compartir orden de servicio"))
}

@SuppressLint("QueryPermissionsNeeded")
fun Context.abrirMaps(direccion: String) {
    val uri = "geo:0,0?q=${Uri.encode(direccion)}".toUri()
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        setPackage("com.google.android.apps.maps")
    }
    if (intent.resolveActivity(packageManager) != null) startActivity(intent)
    else startActivity(
        Intent(Intent.ACTION_VIEW, "https://maps.google.com/?q=${Uri.encode(direccion)}".toUri())
    )
}

fun Long.toFechaLegible(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(this))
}

fun Double.formatearMoneda(simbolo: String = "USD"): String {
    val locale = if (simbolo == "USD") Locale.US else Locale("es", "CO")
    val nf = NumberFormat.getCurrencyInstance(locale)
    return nf.format(this)
}

fun EstadoServicio.etiqueta(): String = when (this) {
    EstadoServicio.PENDIENTE  -> "⏳ Pendiente"
    EstadoServicio.EN_PROCESO -> "🔄 En proceso"
    EstadoServicio.FINALIZADO -> "✅ Finalizado"
}

fun OrdenServicio.generarResumen(
    nombreCliente: String,
    tipoEquipo: String,
    moneda: String = "USD"
): String = """
            🔧 ORDEN DE SERVICIO #$id
            Cliente : $nombreCliente
            Equipo  : $tipoEquipo
            Problema: $problemaReportado
            Diagnóstico: ${diagnosticoTecnico.ifEmpty { "Pendiente" }}
            Solución: ${solucionAplicada.ifEmpty { "Pendiente" }}
            Costo   : ${costo.formatearMoneda(moneda)}
            Estado  : ${estado.etiqueta()}
            Fecha   : ${fechaIngreso.toFechaLegible()}
            """.trimIndent()