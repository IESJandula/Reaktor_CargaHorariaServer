package es.iesjandula.direccion_cargahoraria_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reduccion 
{

	/**Id reduccion*/
	private String idReduccion;
	/*nombre reducccion*/
	private String nombreReduccion;
	/**numero horas*/
	private int numeroHoras;
	private String curso;
	private String etapa;
	private String grupo;
}
