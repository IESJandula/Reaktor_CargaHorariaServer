package es.iesjandula.direccion_cargahoraria_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Clase Asignatura
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Asignatura 
{
	/** Nombre de la asignatura */
	private String nombreAsignatura;
	/** Curso de la asignatura */
	private int curso;
	/** Etapa de la asignatura */
	private String etapa;
	/** Grupo que cursa la asignatura*/
	private String grupo;
	/** Número de horas semanales de la asignatura*/
	private int numeroHorasSemanales;
	/**	Departamento al que pertenece la asignatura*/
	private String departamento;
}
