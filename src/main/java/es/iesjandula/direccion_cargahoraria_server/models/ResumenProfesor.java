package es.iesjandula.direccion_cargahoraria_server.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumenProfesor 
{
	private List<Asignatura> listaAsignaturas;
	private List<Reduccion> listaReduccion;
	private int horasTotales;
}
