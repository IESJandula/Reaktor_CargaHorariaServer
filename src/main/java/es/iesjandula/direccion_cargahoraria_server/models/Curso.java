package es.iesjandula.direccion_cargahoraria_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Curso 
{
	/**curso*/
	private int curso;
	/**etapa*/
	private String etapa;
	/**grupo*/
	private String grupo;
}
