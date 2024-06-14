package es.iesjandula.direccion_cargahoraria_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Clase Curso
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Curso 
{
	/** Número curso al que pertenece*/
	private int curso;
	/** Etapa que se cursa*/
	private String etapa;
	/** Grupo al que pertenece*/
	private String grupo;
}
