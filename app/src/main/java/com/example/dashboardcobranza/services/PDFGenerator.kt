package com.example.dashboardcobranza.services

import android.content.Context
import com.example.dashboardcobranza.data.model.Incidencia
import com.example.dashboardcobranza.ui.viewmodel.Kpi
import com.example.dashboardcobranza.ui.viewmodel.KpiStatus
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PDFGenerator @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun generateIncidentsReport(incidencias: List<Incidencia>): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val writer = PdfWriter(outputStream)
        val pdf = PdfDocument(writer)
        val document = Document(pdf)
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val fechaReporte = sdf.format(Date())

        document.add(Paragraph("Reporte de Incidencias").setBold().setFontSize(20f).setTextAlignment(TextAlignment.CENTER))
        document.add(Paragraph("Generado el: $fechaReporte").setTextAlignment(TextAlignment.CENTER).setFontSize(10f))
        document.add(Paragraph("\n"))

        val table = Table(UnitValue.createPercentArray(floatArrayOf(1f, 3f, 2f, 2f))).useAllAvailableWidth()
        table.addHeaderCell(Paragraph("ID").setBold())
        table.addHeaderCell(Paragraph("Título").setBold())
        table.addHeaderCell(Paragraph("Estado").setBold())
        table.addHeaderCell(Paragraph("Prioridad").setBold())

        for (incidencia in incidencias) {
            table.addCell(incidencia.id.toString())
            table.addCell(incidencia.titulo)
            table.addCell(incidencia.estado)
            table.addCell(incidencia.prioridad)
        }

        document.add(table)
        document.close()
        return outputStream.toByteArray()
    }

    fun generateDashboardReport(kpis: List<Kpi>): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val writer = PdfWriter(outputStream)
        val pdf = PdfDocument(writer)
        val document = Document(pdf)
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val fechaReporte = sdf.format(Date())

        document.add(Paragraph("Reporte de KPIs del Dashboard").setBold().setFontSize(20f).setTextAlignment(TextAlignment.CENTER))
        document.add(Paragraph("Generado el: $fechaReporte").setTextAlignment(TextAlignment.CENTER).setFontSize(10f))
        document.add(Paragraph("\n"))

        val table = Table(UnitValue.createPercentArray(floatArrayOf(3f, 1.5f, 1.5f))).useAllAvailableWidth()
        table.addHeaderCell(Paragraph("Indicador").setBold())
        table.addHeaderCell(Paragraph("Valor Actual").setBold())
        table.addHeaderCell(Paragraph("Estado").setBold())

        for (kpi in kpis) {
            table.addCell(kpi.name)
            table.addCell(kpi.value)
            val estadoEnEspanol = when (kpi.status) {
                KpiStatus.GOOD -> "Bueno"
                KpiStatus.WARNING -> "Advertencia"
                KpiStatus.BAD -> "Malo"
            }
            table.addCell(estadoEnEspanol)
        }

        document.add(table)
        document.close()
        return outputStream.toByteArray()
    }
}
