package es.iesjandula.direccion_cargahoraria_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Clase Guardia
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Guardia
{
	/** Id del profesor*/
	private String idProfesor;
	/** Número horas guardias del profesor*/
	private Integer horasAsignadas;
}
