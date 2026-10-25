package com.ejemplo.vaadin.view;

import com.ejemplo.vaadin.model.Curso;
import com.ejemplo.vaadin.model.Estudiante;
import com.ejemplo.vaadin.repository.CursoRepository;
import com.ejemplo.vaadin.service.BibliotecaService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import java.util.List;

@Route("")
public class MainView extends VerticalLayout {

    private final CursoRepository cursoRepository;
    private final BibliotecaService bibliotecaService;

    public MainView(CursoRepository cursoRepository, BibliotecaService bibliotecaService) {
        this.cursoRepository = cursoRepository;
        this.bibliotecaService = bibliotecaService;
        
        List<Curso> listaCursos = cursoRepository.findAll();

        H1 titulo = new H1("Panel Académico Avanzado");
        
        // Botón de descarga Excel
        Button btnExportar = new Button("Exportar a Excel (POI)");
        StreamResource resource = new StreamResource("reporte_cursos.xlsx", () -> bibliotecaService.generarExcel(listaCursos));
        Anchor anchorDescarga = new Anchor(resource, "");
        anchorDescarga.getElement().setAttribute("download", true);
        anchorDescarga.add(btnExportar);
        
        add(titulo, anchorDescarga);

        for (Curso curso : listaCursos) {
            VerticalLayout cursoLayout = new VerticalLayout();
            cursoLayout.getStyle().set("border", "1px solid #ddd");
            cursoLayout.getStyle().set("padding", "15px");
            cursoLayout.getStyle().set("border-radius", "5px");
            cursoLayout.getStyle().set("margin-bottom", "20px");
            
            cursoLayout.add(new H2(curso.getNombre()));
            cursoLayout.add(new Paragraph(curso.getDescripcion()));
            
            Grid<Estudiante> grid = new Grid<>(Estudiante.class, false);
            grid.addColumn(Estudiante::getId).setHeader("ID");
            grid.addColumn(Estudiante::getNombre).setHeader("Nombre");
            grid.addColumn(Estudiante::getEmail).setHeader("Email");
            grid.setItems(curso.getEstudiantes());
            grid.setAllRowsVisible(true);
            
            // Demostración de uso del servicio (Simulado aquí con campos de texto)
            TextField txtNombre = new TextField("Nuevo Alumno");
            TextField txtEmail = new TextField("Email");
            Button btnAdd = new Button("Matricular", e -> {
                try {
                    bibliotecaService.registrarNuevoEstudiante(curso, txtNombre.getValue(), txtEmail.getValue());
                    cursoRepository.save(curso);
                    grid.setItems(curso.getEstudiantes()); // Refrescar grilla
                    Notification.show("Estudiante guardado (Ver Logs)");
                    txtNombre.clear(); txtEmail.clear();
                } catch (Exception ex) {
                    Notification.show("Error: " + ex.getMessage());
                }
            });
            
            HorizontalLayout form = new HorizontalLayout(txtNombre, txtEmail, btnAdd);
            form.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

            cursoLayout.add(grid, form);
            add(cursoLayout);
        }
    }
}
