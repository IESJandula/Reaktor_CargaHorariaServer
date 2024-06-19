package es.iesjandula.direccion_cargahoraria_server.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.iesjandula.direccion_cargahoraria_server.exception.HorarioException;
import es.iesjandula.direccion_cargahoraria_server.models.Asignatura;
import es.iesjandula.direccion_cargahoraria_server.models.Curso;
import es.iesjandula.direccion_cargahoraria_server.models.ResumenCursos;
import es.iesjandula.direccion_cargahoraria_server.utils.Constants;
import es.iesjandula.direccion_cargahoraria_server.utils.Parse;
import es.iesjandula.direccion_cargahoraria_server.utils.Validations;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
/**
 * clase RestHandlerMatriculas
 */
@RequestMapping(value = "/matriculas")
@RestController
@Log4j2
public class RestHandlerMatriculas
{
	/**
	 * Endpoint para cargar los cursos
	 * @param csvFile Fichero csv a parsear
	 * @param curso Curso sobre el cual se asigna
	 * @param etapa Etapa sobre la cual se asigna
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return 200 si todo ha ido bien
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/cursos", consumes = "multipart/form-data")
	public ResponseEntity<?> uploadCursos(@RequestParam(value = "csv", required = true) MultipartFile csvFile,
			@RequestHeader(value = "curso", required = true) Integer curso,
			@RequestHeader(value = "etapa", required = true) String etapa, HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			Validations validations = new Validations();
			
			// Obtenemos el mapa cursos
			Map<String, Map<String, List<String>>> mapaCursos = validations.inicializarMapaCursos(session); 
			
			// Parseamos el fichero csv
			parse.parseCursosMap(csvFile, curso, etapa, session, mapaCursos);
			
			return ResponseEntity.ok().build();
		}
		catch (Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
							 Constants.ERR_GENERIC_EXCEPTION_MSG + "uploadCursos",
							 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "uploadCursos", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}

	/**
	 * Endpoint para obtener cursos
	 * 
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return Lista de cursos
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/cursos")
	public ResponseEntity<?> getCursos(HttpSession session) 
	{
		try
		{
			Validations validations = new Validations();
			
			// Obtenemos el mapa cursos
			Map<String, Map<String, List<String>>> mapaCursos = validations.obtenerMapaCursos(session);
			
			return ResponseEntity.ok().body(mapaCursos.keySet());
		}
		catch (Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
							 Constants.ERR_GENERIC_EXCEPTION_MSG + "getCursos",
							 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "getCursos", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}

	/**
	 * Endpoint para subir bloques
	 * 
	 * @param curso Número curso 
	 * @param etapa Etapa del curso
	 * @param nombreAsignatura Nombre de la asignatura
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return 200 si todo ha ido bien
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/bloques")
	public ResponseEntity<?> uploadBloques(@RequestHeader(value = "curso", required = true) Integer curso,
			@RequestHeader(value = "etapa", required = true) String etapa,
			@RequestHeader(value = "nombreAsignatura", required = true) String nombreAsignatura, HttpSession session)
	{
		try
		{
			Validations validations = new Validations();
			
			// Obtenemos el mapa de bloques
			Map<String, List<String>> mapaBloques = validations.inicializarMapaBloques(session);
			
			// Obtenemos la lista de asignaturas
			List<Asignatura> listaAsignaturas = validations.obtenerListaAsignaturas(session);

			boolean encontrado = false;
			int i = 0;
			
			// Comprobamos que los parametros recibidos son correctos
			while (i < listaAsignaturas.size() && !encontrado)
			{
				encontrado = listaAsignaturas.get(i).getNombreAsignatura().equalsIgnoreCase(nombreAsignatura) && 
							 listaAsignaturas.get(i).getCurso() == (curso) 									  && 
							 listaAsignaturas.get(i).getEtapa().equalsIgnoreCase(etapa) ;
				i++;
			}
			
			if (!encontrado)
			{
				String error = "El curso " + curso + ", etapa " + etapa + " o asignatura " + nombreAsignatura + " no existe" ;
				
				// Log con el error
				log.error(error);
				
				throw new HorarioException(Constants.ERR_VALIDATE_CURSO_ETAPA, error);
			}
			
			// Asignar asignaturas al mapa bloques
			validations.asignarAsignaturasMapaBloques(curso, etapa, nombreAsignatura, session, mapaBloques);
			
			return ResponseEntity.ok().build();
		}
		catch (HorarioException horarioException)
		{
			return ResponseEntity.status(400).body(horarioException.getBodyExceptionMessage());
		}
		catch (Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
							 Constants.ERR_GENERIC_EXCEPTION_MSG + "uploadBloques",
							 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "uploadBloques", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}

	/**
	 * Endpoint para obtener los bloques
	 * 
	 * @param curso Número curso 
	 * @param etapa Etapa del curso
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return Lista de asignaturas de un bloque
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/bloques")
	public ResponseEntity<?> getBloques(@RequestHeader(value = "curso", required = true) Integer curso,
			@RequestHeader(value = "etapa", required = true) String etapa, HttpSession session)
	{
		try
		{
			Validations validations = new Validations();		
			// Obtenemos el mapa de bloques
			Map<String, List<String>> mapaBloques = validations.obtenerMapaBloques(session);
			
			// Obtenemos lista cursos
			List<Curso> listaCursos = validations.obtenerListaCursos(session);
			
			int i = 0;
			boolean encontrado = false;
			while(i < listaCursos.size() && !encontrado)
			{
				encontrado = listaCursos.get(i).getCurso() == curso && listaCursos.get(i).getEtapa().equals(etapa.toUpperCase()) ; 
				i++;
			}
			
			if(!encontrado) 
			{
				String error = "El curso " + curso + " o etapa " + etapa + " no existe" ;
				
				// Log con el error
				log.error(error);
				
	            throw new HorarioException(Constants.ERR_VALIDATE_CURSO_ETAPA, error);
			}	
			
			String clave = curso + etapa.toUpperCase();
			List<String> listaAsignatura = mapaBloques.get(clave);
			
			if(listaAsignatura == null) 
			{
				String error = "No se ha asignado ninguna asignatura al curso " + clave ;
				
				// Log con el error
				log.error(error);
				
	            throw new HorarioException(Constants.ERR_ASIG_CURSO, error);
			}	
			return ResponseEntity.ok().body(listaAsignatura);
		}
		catch (HorarioException horarioException)
		{
			return ResponseEntity.status(400).body(horarioException.getBodyExceptionMessage());
		}
		catch (Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
							 Constants.ERR_GENERIC_EXCEPTION_MSG + "getBloques",
							 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "getBloques", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}

	/**
	 * Endpoint para cargar alumnos
	 * 
	 * @param alumno Apellidos y nombre del alumno(Nuñez Nuñez,Juanito)
	 * @param curso Número curso 
	 * @param etapa Etapa del curso
	 * @param grupo Grupo del curso
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return 200 si todo ha ido bien
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/alumnos")
	public ResponseEntity<?> uploadAlumno(@RequestHeader(value = "alumno", required = true) String alumno,
			@RequestHeader(value = "curso", required = true) Integer curso,
			@RequestHeader(value = "etapa", required = true) String etapa,
			@RequestHeader(value = "grupo", required = true) String grupo, HttpSession session)
	{
		try
		{
			Validations validations = new Validations();
			
			Curso cursoObject = new Curso(curso, etapa.toUpperCase(), grupo.toUpperCase());
			String clave = curso + etapa.toUpperCase() + grupo.toUpperCase();
			
			// Obtenemos listaCursos
			List<Curso> listaCursos = validations.obtenerListaCursos(session);
			
			// Obtenemos lista nombres
			List<String> listaNombres = validations.obtenerListaNombresExiste(session);
			
			// Realizar asignacion del alumno
			validations.realizarAsignacionAlumno(alumno, session, cursoObject, clave, listaCursos,listaNombres);

			return ResponseEntity.ok().build();
		}
		catch (Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
							 Constants.ERR_GENERIC_EXCEPTION_MSG + "uploadAlumno",
							 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "uploadAlumno", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}
	
	/**
	 * Endpoint para obtener los alumnos
	 * 
	 * @param curso Número del curso 
	 * @param etapa Etapa del curso
	 * @param grupo Grupo del curso
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return Lista de alumnos
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/alumnos")
	public ResponseEntity<?> getAlumno(@RequestHeader(value = "curso", required = true) Integer curso,
			@RequestHeader(value = "etapa", required = true) String etapa,
			@RequestHeader(value = "grupo", required = true) String grupo, HttpSession session)
	{
		try
		{
			Validations validations = new Validations();
			List<String> listaAlumnos = null;
			
			// Obtenemos lista de cursos
			List<Curso> listaCursos = validations.obtenerListaCursos(session);
			
			Curso cursoObject = new Curso(curso, etapa.toUpperCase(), grupo.toUpperCase());
			
			String clave = curso + etapa.toUpperCase()+ grupo.toUpperCase();
			if (listaCursos.contains(cursoObject))
			{
				
				// Obtenemos el mapa Alumnos
				Map<String, List<String>> mapaAlumnos = validations.obtenerMapaAlumno(session);
				
				listaAlumnos = mapaAlumnos.get(clave);			
			}
			else
			{
				String error = "El curso " + clave + " no existe";
				
				// Log con el error
				log.error(error);
				
				throw new HorarioException(Constants.ERR_CURSO_EXIS, error);
			}
			return ResponseEntity.ok().body(listaAlumnos);
		}
		catch (HorarioException horarioException)
		{
			return ResponseEntity.status(400).body(horarioException.getBodyExceptionMessage());
		}
		catch (Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
							 Constants.ERR_GENERIC_EXCEPTION_MSG + "getAlumno",
							 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "getAlumno", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}
	
	/**
	 * Endpoint para aliminar alumnos
	 * 
	 * @param alumno Apellidos y nombre del alumno(Nuñez Nuñez,Juanito)
	 * @param curso Número de curso 
	 * @param etapa Etapa del curso
	 * @param grupo Grupo del curso
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return 200 si todo ha ido bien
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/alumnos")
	public ResponseEntity<?> borrarAlumno(@RequestHeader(value = "alumno", required = true) String alumno,
			@RequestHeader(value = "curso", required = true) Integer curso,
			@RequestHeader(value = "etapa", required = true) String etapa,
			@RequestHeader(value = "grupo", required = true) String grupo, HttpSession session)
	{
		try
		{
			Validations validations = new Validations();
			
			// Obtenemos la lista de cursos
			List<Curso> listaCursos = validations.obtenerListaCursos(session);
			
			Curso cursoObject = new Curso(curso, etapa.toUpperCase(), grupo.toUpperCase());
			
			if (!listaCursos.contains(cursoObject))
			{
				String error = "El curso " + cursoObject + " no existe";
				
				// Log con el error
				log.error(error);
				
				throw new HorarioException(Constants.ERR_CURSO_EXIS, error);
			}
			
			String clave = curso + etapa.toUpperCase() + grupo.toUpperCase();
			
			// Obtenemos el mapa alumnos
			Map<String, List<String>> mapaAlumnos = validations.obtenerMapaAlumno(session);
			
			// Obtenemos la lista de alumnos
			List<String> listaAlumnos = mapaAlumnos.get(clave);
			log.info(listaAlumnos);
			
			// Validamos la existencia del objeto curso
			validations.validarExistenciaCurso(listaCursos, cursoObject);
			
			// Validamos la existencia del alumno
			validations.validarExistenciaAlumno(alumno, listaAlumnos);
			
			return ResponseEntity.ok().build();
		}
		catch (Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
							 Constants.ERR_GENERIC_EXCEPTION_MSG + "borrarAlumno",
							 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "borrarAlumno", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}

	/**
	 * Endpoint papra obtener el resumen de una asignatura
	 * 
	 * @param nombreAsignatura Nombre de la asignatura para resumen
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return Número de alumnos en la asignatura
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/asignaturas/resumen")
	public ResponseEntity<?> getAsignaturasResumen(@RequestHeader(value = "nombreAsignatura", required = true) String nombreAsignatura, HttpSession session)
	{
		try
		{
			Validations validations = new Validations();
			
			// Obtenemos lista de nombres
			List<String> listaNombres = validations.obtenerListaNombresExiste(session);
			
			// Obtenemos mapa de asignaturas
			Map<String, List<String>> mapaAsignaturas = validations.obtenerMapaAsignaturasCursos(session);
			
			// Obtenemos lista de asignaturas
			List<Asignatura> listaAsignatura = validations.obtenerListaAsignaturas(session);
			
			int contadorAlumno=0;
			boolean encontrado = false;
			// Obtenemos si el nombre de la asignatura existe
			validations.validarSiExisteNombreAsignatura(nombreAsignatura, listaAsignatura);
			
			for(String nombre : listaNombres) 
			{
				List<String> asignaturasAlumno = mapaAsignaturas.get(nombre);
				if(asignaturasAlumno.contains(nombreAsignatura)) 
				{
					contadorAlumno ++;
				}
			}

			return ResponseEntity.ok().body("Número de alumnos: " + contadorAlumno);
		}
		catch (Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
							 Constants.ERR_GENERIC_EXCEPTION_MSG + "getAsignaturasResumen",
							 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "getAsignaturasResumen", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}
	
	/**
	 * Endpoint para obtener el resumen por cursos
	 * 
	 * @param curso Número del curso
	 * @param etapa Etapa del curso
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return Lista de resumen por cursos
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/cursos/resumen")
	public ResponseEntity<?> getCursosResumen(@RequestHeader(value = "curso", required = true) int curso,
											  @RequestHeader(value = "etapa", required = true) String etapa,HttpSession session)
	{
		try
		{
			Validations validations = new Validations();
			boolean encontrado = false;
			
			// Obtenemos lista cursos
			List<Curso> listaCursos = validations.obtenerListaCursos(session); 
			
			// Obtenemos mapa alumnos
			Map<String, List<String>> mapaAlumnos = validations.obtenerMapaAlumno(session);
			
			ResumenCursos resumenCursos = null;
			List<ResumenCursos> listaResumenCursos = new ArrayList<ResumenCursos>();
			
			int i = 0;
			while(i < listaCursos.size() && !encontrado) 
			{
				encontrado = listaCursos.get(i).getCurso() == curso && listaCursos.get(i).getEtapa().equalsIgnoreCase(etapa) ;
				if (encontrado) 
				{
					String claveMapaAlumnos = curso+etapa.toUpperCase()+listaCursos.get(i).getGrupo();
					// Obtenemos la lista de alumnos
					List<String> listaAlumnos = mapaAlumnos.get(claveMapaAlumnos);
					
					resumenCursos = new ResumenCursos(curso, listaCursos.get(i).getGrupo(), etapa.toUpperCase(), listaAlumnos.size());
					listaResumenCursos.add(resumenCursos);
				}
				i++;
			}
			return ResponseEntity.ok().body(resumenCursos);
		}
		catch (Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
							 Constants.ERR_GENERIC_EXCEPTION_MSG + "getCursosResumen",
							 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "getCursosResumen", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}
}
