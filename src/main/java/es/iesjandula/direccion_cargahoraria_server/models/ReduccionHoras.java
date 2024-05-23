package es.iesjandula.direccion_cargahoraria_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Clase ReduccionHoras
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReduccionHoras
{
	/**id de la reduccion*/
	private String idReduccion;
	/**numero de horas de la reduccion*/
	private int numHoras;
}
