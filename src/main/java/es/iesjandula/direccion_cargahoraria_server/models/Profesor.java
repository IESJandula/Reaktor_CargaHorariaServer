package es.iesjandula.direccion_cargahoraria_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profesor 
{
	/**Id del profesor*/
	private String idProfesor;
	/**nombre profesor*/
	private String nombreProfesor;
	/**nombre departamento*/
	private String departamento;
}
