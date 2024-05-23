package es.iesjandula.direccion_cargahoraria_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Clase Resumen Departamento
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumenDepartamento 
{
	/**numero de profesores que pertenecen al departamento*/
	private int plantilla;
	/**numero de horas necesarias para el departamento*/
	private int horasNecesarias;
	/**total de horas de los profesores del departamento*/
	private int totalHoras;
	/**desfase*/
	private int desfase;
	/**si faltan,sobran horas o esta en equilibrio*/
	private String resultado;
}
