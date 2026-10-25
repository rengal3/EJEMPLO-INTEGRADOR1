package com.ejemplo.vaadin.service;

import com.ejemplo.vaadin.model.Curso;
import com.ejemplo.vaadin.model.Estudiante;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class BibliotecaService {

    // LOGBACK: Creación del Logger
    private static final Logger logger = LoggerFactory.getLogger(BibliotecaService.class);

    public void registrarNuevoEstudiante(Curso curso, String nombre, String email) {
        // LOGBACK: Registrar intento
        logger.info("Intentando registrar estudiante: {} en el curso: {}", nombre, curso.getNombre());

        // GUAVA: Validación rápida y limpia de precondiciones
        Preconditions.checkNotNull(curso, "El curso no puede ser nulo");
        
        // APACHE COMMONS: Verificación de Strings vacíos o nulos
        if (StringUtils.isBlank(nombre) || StringUtils.isBlank(email)) {
            logger.error("Error de seguridad/validación: Datos incompletos proporcionados.");
            throw new IllegalArgumentException("Nombre y Email son obligatorios.");
        }

        // Simulación de Lógica de Negocio
        Estudiante nuevoEstudiante = new Estudiante(StringUtils.capitalize(nombre.trim()), email.trim().toLowerCase());
        curso.addEstudiante(nuevoEstudiante);
        
        logger.debug("Estudiante procesado correctamente. Total en curso ahora: {}", curso.getEstudiantes().size());
    }

    public ByteArrayInputStream generarExcel(List<Curso> cursos) {
        logger.info("Iniciando generación de reporte Excel con POI");
        
        // APACHE POI: Crear libro de Excel
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            // GUAVA: Invertir el orden de la lista como ejemplo de manipulación de colecciones
            List<Curso> cursosReversos = Lists.reverse(cursos);

            Sheet sheet = workbook.createSheet("Reporte de Cursos");

            // Cabecera
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID Curso");
            headerRow.createCell(1).setCellValue("Nombre Curso");
            headerRow.createCell(2).setCellValue("Total Alumnos");

            int rowIdx = 1;
            for (Curso c : cursosReversos) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(c.getId());
                row.createCell(1).setCellValue(c.getNombre());
                row.createCell(2).setCellValue(c.getEstudiantes().size());
            }

            workbook.write(out);
            logger.info("Reporte Excel generado exitosamente.");
            return new ByteArrayInputStream(out.toByteArray());
            
        } catch (IOException e) {
            logger.error("Error crítico al generar el Excel", e);
            throw new RuntimeException("Fallo al crear Excel", e);
        }
    }
}
