package es.iesjandula.direccion_cargahoraria_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Clase Reduccion
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reduccion 
{
	/** Id reduccion*/
	private String idReduccion;
	/** Nombre de la reducccion*/
	private String nombreReduccion;
	/** Número de horas*/
	private int numeroHoras;
	/** Curso al que pertenece la reduccion*/
	private String curso;
	/** Etapa a la que pertenece la reduccion*/
	private String etapa;
	/** Grupo al que pertenece la reduccion*/
	private String grupo;
}
