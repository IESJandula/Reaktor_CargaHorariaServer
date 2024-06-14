package es.iesjandula.direccion_cargahoraria_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * clase resumen
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resumen
{
	/** Número profesores departamento*/
	private int plantilla;
	/** Número horas de profesores en el departamento*/
	private int horasNecesarias;
	/** Suma total de las horas asignadas a todos los profesores del departamento.*/
	private int totalHoras;
	/** Total horas - horas necesarias.*/
	private int desfase;
	/** Resultado del desfase*/
	private String resultado;
}
