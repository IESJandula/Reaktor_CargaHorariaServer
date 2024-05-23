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
	/**Id reduccion*/
	private String idReduccion;
	/**nombre reducccion*/
	private String nombreReduccion;
	/**numero horas*/
	private int numeroHoras;
	/**curso al que pertenece la reduccion*/
	private String curso;
	/**etapa a la que pertenece la reduccion*/
	private String etapa;
	/**grupo al que pertenece la reduccion*/
	private String grupo;
}
