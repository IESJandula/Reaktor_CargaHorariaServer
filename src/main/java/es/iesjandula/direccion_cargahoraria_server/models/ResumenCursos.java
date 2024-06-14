package es.iesjandula.direccion_cargahoraria_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * clase resumen cursos
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumenCursos
{
	/** Número del curso*/
	private int curso;
	/** Grupo del curso*/
	private String grupo;
	/** Etapa del curso*/
	private String etapa;
	/** Numero de alumnos en el curso*/
	private int numAlumnos;
}
