package com.ejemplo.vaadin;

import com.ejemplo.vaadin.model.Curso;
import com.ejemplo.vaadin.model.Estudiante;
import com.ejemplo.vaadin.repository.CursoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VaadinDemoApplication {
    
    
    private static final Logger log = LoggerFactory.getLogger(VaadinDemoApplication.class);

    
	public static void main(String[] args) {
		SpringApplication.run(VaadinDemoApplication.class, args);
                
	}

    @Bean
    public CommandLineRunner initData(CursoRepository cursoRepository) {
        return args -> {
            log.info("Inicializando datos de prueba...");
            Curso curso1 = new Curso("Ingeniería de Sistemas", "Diseño de Patrones y Arquitectura de Software");
            curso1.addEstudiante(new Estudiante("Ana García", "ana@correo.com"));
            curso1.addEstudiante(new Estudiante("Luis Perez", "luis@correo.com"));

            Curso curso2 = new Curso("Servicios Cloud", "Despliegue con Docker y Kubernetes");
            curso2.addEstudiante(new Estudiante("Carlos Ruiz", "carlos@correo.com"));
            curso2.addEstudiante(new Estudiante("Maria Gomez", "maria@correo.com"));

            cursoRepository.save(curso1);
            cursoRepository.save(curso2);
            log.info("Datos listos. Vaadin escuchando en el puerto 8080");
        };
    }
}
