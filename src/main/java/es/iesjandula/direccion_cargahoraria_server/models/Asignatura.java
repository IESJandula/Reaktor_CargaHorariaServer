package es.iesjandula.direccion_cargahoraria_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Asignatura 
{
	private String nombreAsignatura;
	private String curso;
	private String etapa;
	private String grupo;
	private int numeroHorasSemanales;
	private String departamento;
}
