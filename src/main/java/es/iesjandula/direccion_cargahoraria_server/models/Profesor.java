package es.iesjandula.direccion_cargahoraria_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Clase Profesor
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profesor 
{
	/** Id del profesor*/
	private String idProfesor;
	/** Nombre del profesor*/
	private String nombreProfesor;
	/** Nombre del departamento*/
	private String departamento;
}
