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
	/** Número de profesores que pertenecen al departamento*/
	private int plantilla;
	/**	Número de horas necesarias para el departamento*/
	private int horasNecesarias;
	/**	Total de horas de los profesores del departamento*/
	private int totalHoras;
	/** Desfase de horas*/
	private int desfase;
	/** Si faltan,sobran horas o esta en equilibrio*/
	private String resultado;
}
