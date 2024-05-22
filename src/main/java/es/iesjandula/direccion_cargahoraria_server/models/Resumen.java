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
	/**numero profesores departamento*/
	private int plantilla;
	/**numero horas de profesores en el departamento*/
	private int horasNecesarias;
	/**suma total de las horas asignadas a todos los profesores del departamento.*/
	private int totalHoras;
	/**total horas - horas necesarias.*/
	private int desfase;
	/**resultado del desfase*/
	private String resultado;
}
