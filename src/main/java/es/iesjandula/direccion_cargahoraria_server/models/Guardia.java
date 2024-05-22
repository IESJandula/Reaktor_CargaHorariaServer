package es.iesjandula.direccion_cargahoraria_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Guardia
{

	/**Id del profesor*/
	private String idProfesor;
	/**numero horas guardia*/
	private Integer horasAsignadas;
}
