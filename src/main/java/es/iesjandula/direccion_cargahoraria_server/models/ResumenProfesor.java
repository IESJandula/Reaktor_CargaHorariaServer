package es.iesjandula.direccion_cargahoraria_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumenProfesor 
{
	private int horasAsignaturas;
	
	private int horasReduccion;
	
	private int horasTotales;
}
