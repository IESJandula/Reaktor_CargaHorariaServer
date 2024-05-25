package es.iesjandula.direccion_cargahoraria_server.models;

import java.util.List;

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
	private List<Asignatura> asignaturas;
	/**numero de horas de la reduccion*/
	private List<ReduccionHoras> reducciones;
	/**numero de horas totales*/
	private int horasTotales;
}
