package es.iesjandula.direccion_cargahoraria_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Clase Resumen Profesor
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumenProfesor 
{
	/**numero de horas de la asignatura*/
	private int horasAsignaturas;
	/**numero de horas de la reduccion*/
	private int horasReduccion;
	/**numero de horas totales*/
	private int horasTotales;
}
