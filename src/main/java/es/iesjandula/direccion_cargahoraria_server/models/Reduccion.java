package es.iesjandula.direccion_cargahoraria_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reduccion 
{

	private String idReduccion;
	private String nombreReduccion;
	private int numeroHoras;
	private String curso;
	private String etapa;
	private String grupo;
}
