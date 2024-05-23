package es.iesjandula.direccion_cargahoraria_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
/**
 * Clase Main
 */
@ComponentScan(value="es.iesjandula.direccion_cargahoraria_server")
@SpringBootApplication
public class DireccionCargaHorariaServerApplication 
{
	public static void main(String[] args) 
	{
		SpringApplication.run(DireccionCargaHorariaServerApplication.class, args);
	}
}
