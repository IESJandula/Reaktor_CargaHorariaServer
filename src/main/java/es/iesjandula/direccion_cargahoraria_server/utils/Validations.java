package es.iesjandula.direccion_cargahoraria_server.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import es.iesjandula.direccion_cargahoraria_server.exception.HorarioException;
import es.iesjandula.direccion_cargahoraria_server.models.Asignatura;
import es.iesjandula.direccion_cargahoraria_server.models.Curso;
import es.iesjandula.direccion_cargahoraria_server.models.Departamento;
import es.iesjandula.direccion_cargahoraria_server.models.Profesor;
import es.iesjandula.direccion_cargahoraria_server.models.ReduccionHoras;
import es.iesjandula.direccion_cargahoraria_server.models.Resumen;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Validations
{
	/**
	 * método para comprobar si el fichero esta vacio
	 * 
	 * @param metodoLlamada método que llama a esta funcionalidad
	 * @param ficheroMultipart fichero Multipart
	 * @return 
	 * @throws HorarioException
	 */
	public String obtenerContenidoFichero(String metodoLlamada, MultipartFile ficheroMultipart) throws HorarioException
	{
		String contenido = null ;
		
		try
		{
			contenido = new String(ficheroMultipart.getBytes());
			
			if (contenido == null || contenido.isEmpty())
			{
				String parameterName = ficheroMultipart.getName() ;
				
				log.error(Constants.ERR_CONTENIDO_FICHEROS_CSV_MSG + parameterName) ;
				throw new HorarioException(Constants.ERR_CONTENIDO_FICHEROS_CSV_CODE, Constants.ERR_CONTENIDO_FICHEROS_CSV_MSG + parameterName);
			}
		}
		catch (IOException ioException)
		{
			log.error(Constants.ERR_LECTURA_FICHEROS_CSV_MSG + metodoLlamada, ioException);
			
			throw new HorarioException(Constants.ERR_LECTURA_FICHEROS_CSV_CODE, 
									   Constants.ERR_LECTURA_FICHEROS_CSV_MSG + metodoLlamada, 
									   ioException);
		}
		
		return contenido ;
	}
	
	public void obtenerDepartamento(List<Departamento> listaDepartamentos, Departamento departamento) throws HorarioException
	{
		// comprobamos que el departamento existe
		if (!listaDepartamentos.contains(departamento))
		{
			String error = "Departamento no encontrado";
			throw new HorarioException(12, error);
		}
	}
	
	public void obtenerCurso(List<Curso> listaCursos, Curso curso) throws HorarioException
	{
		// comprobamos que el curso existe
		if (!listaCursos.contains(curso))
		{
			String error = "Curso no encontrado: " + curso;
			log.error(error);
			throw new HorarioException(12, error);
		}
	}
}
