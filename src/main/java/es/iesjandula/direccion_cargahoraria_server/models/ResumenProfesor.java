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
	/** Número de horas de la asignatura*/
	private List<Asignatura> asignaturas;
	/** Número de horas de la reduccion*/
	private List<ReduccionHoras> reducciones;
	/** Número de horas totales*/
	private int horasTotales;
}
