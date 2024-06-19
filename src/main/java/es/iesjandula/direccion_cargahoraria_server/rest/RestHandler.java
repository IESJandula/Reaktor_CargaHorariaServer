package es.iesjandula.direccion_cargahoraria_server.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import es.iesjandula.direccion_cargahoraria_server.models.Departamento;
import es.iesjandula.direccion_cargahoraria_server.models.Profesor;
import es.iesjandula.direccion_cargahoraria_server.models.Reduccion;
import es.iesjandula.direccion_cargahoraria_server.models.ReduccionHoras;
import es.iesjandula.direccion_cargahoraria_server.models.Resumen;
import es.iesjandula.direccion_cargahoraria_server.models.ResumenProfesor;
import es.iesjandula.direccion_cargahoraria_server.utils.Parse;
import es.iesjandula.direccion_cargahoraria_server.utils.Validations;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

import es.iesjandula.direccion_cargahoraria_server.utils.Constants;
import es.iesjandula.direccion_cargahoraria_server.utils.Overviews;
/**
 * clase RestHandler
 */
@RequestMapping(value="/carga_horaria")
@RestController
@Log4j2
public class RestHandler 
{
	/**
	 * Endpoint para subir los departamentos
	 * 
	 * @param csvFile Fichero csv a parsear
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return 200 Si todo ha ido bien
	 */
	@RequestMapping(method=RequestMethod.POST,value="/departamentos",consumes="multipart/form-data")
	public ResponseEntity<?> uploadDepartamentos(@RequestParam(value="csv",required=true)MultipartFile csvFile,HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			
			//parseamos el csv con el endpoint
			List<Departamento> listaDepartamentos = parse.parseDepartamentos(csvFile);
			
			//guardamos la lista en session
			session.setAttribute(Constants.SESION_LISTA_DEPARTAMENTOS, listaDepartamentos);
			
			// Pintamos en los logs en modo info la lista de departamentos
			log.info(listaDepartamentos);
			
			// Devolvemos un 200 ya que todo ha ido bien
			return ResponseEntity.ok().build();
		}
		catch(HorarioException horarioException)
		{
			return ResponseEntity.status(400).body(horarioException.getBodyExceptionMessage());
		}
		catch(Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
										 Constants.ERR_GENERIC_EXCEPTION_MSG + "uploadDepartamentos",
										 exception);
			
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "uploadDepartamentos", horarioException);
			
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}
	
	/**
	 * Endpoint para obtener los departamentos
	 * 
	 * @param session utilizado para guardar u obtener cosas en sesión
	 * @return lista de departamentos
	 */
	@RequestMapping(method=RequestMethod.GET,value="/departamentos")
	public ResponseEntity<?> consultaDepartamentos(HttpSession session)
	{
		try
		{
			Validations validations = new Validations();
			
			// Obtenemos la lista de departamentos
			List<Departamento> listaDepartamentos = validations.obtenerListaDepartamentos(session);
			
			// Pintamos en los logs en modo info
			log.info(listaDepartamentos);
			
			return ResponseEntity.ok(listaDepartamentos);		
		}
		catch(HorarioException horarioException)
		{
			return ResponseEntity.status(400).body(horarioException.getBodyExceptionMessage());
		}
		catch(Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
										 Constants.ERR_GENERIC_EXCEPTION_MSG + "consultaDepartamentos",
										 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "consultaDepartamentos", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}
	
	/**
	 * Endpoint para subir los cursos
	 * 
	 * @param csvFile Fichero csv a parsear
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return 200 si todo ha ido bien
	 */
	@RequestMapping(method=RequestMethod.POST,value="/cursos",consumes="multipart/form-data")
	public ResponseEntity<?> uploadCursos(@RequestParam(value="csv",required=true) MultipartFile csvFile, HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			//parseamos el csv con el endpoint
			List<Curso> listaCursos = parse.parseCursos(csvFile);
			
			//guardamos la lista en session
			session.setAttribute(Constants.SESION_LISTA_CURSOS, listaCursos);
			
			// Log para mostrar la lista
			log.info(listaCursos);
			
			// Devolvemos un 200 ya que todo ha ido bien
			return ResponseEntity.ok().build();
		}
		catch(HorarioException horarioException)
		{
			return ResponseEntity.status(400).body(horarioException.getBodyExceptionMessage());
		}
		catch(Exception exception)
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
	 * Endpoint para obtener lista de cursos
	 * 
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return La lista de cursos
	 */
	@RequestMapping(method=RequestMethod.GET,value="/cursos")
	public ResponseEntity<?> consultaCursos(HttpSession session)
	{
		try
		{
			Validations validations = new Validations();
			
			// Obtenemos la lista 
			List<Curso> listaCursos = validations.obtenerListaCursos(session);
			
			// Pintamos en los logs en modo info
			log.info(listaCursos);
			
			return ResponseEntity.ok().body(listaCursos);
		}
		catch(HorarioException horarioException)
		{
			return ResponseEntity.status(400).body(horarioException.getBodyExceptionMessage());
			
		}
		catch(Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
										 Constants.ERR_GENERIC_EXCEPTION_MSG + "consultaCursos",
										 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "consultaCursos", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}
	
	/**
	 * Endpoint para subir los profesores
	 * 
	 * @param csvFile Fichero csv a parsear
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return 200 si todo ha ido bien
	 */
	@RequestMapping(method=RequestMethod.POST,value="/profesores",consumes="multipart/form-data")
	public ResponseEntity<?> uploadProfesores(@RequestParam(value="csv",required=true)MultipartFile csvFile,HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			Validations validations = new Validations();
			// Obtenemos la lista de departamentos guardada en session
			List<Departamento> listaDepartamentos = validations.obtenerListaDepartamentos(session);
			
			//parseamos el csv con el endpoint
			List<Profesor> listaProfesores = parse.parseProfesores(csvFile,listaDepartamentos);
			
			//guardamos la lista en session
			session.setAttribute(Constants.SESION_LISTA_PROFESORES, listaProfesores);
			
			// Pintamos en los logs en modo info
			log.info(listaProfesores);
			
			return ResponseEntity.ok().build();
		}
		catch(HorarioException horarioException)
		{
			return ResponseEntity.status(400).body(horarioException.getBodyExceptionMessage());
		}
		catch(Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
										 Constants.ERR_GENERIC_EXCEPTION_MSG + "uploadProfesores",
										 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "uploadProfesores", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}
	
	/**
	 * Endpoint para obtener la lista profesores
	 * 
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return La lista de profesores
	 */
	@RequestMapping(method=RequestMethod.GET,value="/profesores")
	public ResponseEntity<?> consultaProfesores(HttpSession session)
	{
		try
		{
			Validations validations = new Validations();
			
			// Obtenemos la lista profesores
			List<Profesor> listaProfesores = validations.obtenerListaProfesores(session);
			
			// Pintamos en los logs en modo info
			log.info(listaProfesores);
			
			return ResponseEntity.ok().body(listaProfesores);
		}
		catch(HorarioException horarioException)
		{
			return ResponseEntity.status(400).body(horarioException.getBodyExceptionMessage());		
		}
		catch(Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
										 Constants.ERR_GENERIC_EXCEPTION_MSG + "consultaProfesores",
										 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "consultaProfesores", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}
	
	/**
	 * Endpoint para subir las asignaturas
	 * 
	 * @param csvFile Fichero csv a parsear
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return 200 si todo ha ido bien
	 */
	@RequestMapping(method=RequestMethod.POST,value="/asignaturas",consumes="multipart/form-data")
	public ResponseEntity<?> uploadAsignaturas(@RequestParam(value="csv",required=true)MultipartFile csvFile, HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			Validations validations = new Validations();
			
			// Obtenemos la lista de departamentos guardada en session
			List<Departamento> listaDepartamentos = validations.obtenerListaDepartamentos(session);
			
			// Comprobamos si existe la lista cursos
			List<Curso> listaCursos = validations.obtenerListaCursos(session);
			
			// Parseamos el csv con el endpoint
			List<Asignatura> listaAsignaturas = parse.parseAsignaturas(csvFile,listaCursos,listaDepartamentos);
			
			// Guardamos la lista en session
			session.setAttribute(Constants.SESION_LISTA_ASIGNATURAS, listaAsignaturas);
			
			// Pintamos en los logs en modo info
			log.info(listaAsignaturas);
			
			return ResponseEntity.ok().build();
		}
		catch(HorarioException horarioException)
		{
			return ResponseEntity.status(400).body(horarioException.getBodyExceptionMessage());
		}
		catch(Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
										 Constants.ERR_GENERIC_EXCEPTION_MSG + "uploadAsignaturas",
										 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "uploadAsignaturas", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}
	
	/**
	 * Endpoint para obtener las asignaturas
	 * 
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return Lista de asignaturas
	 */
	@RequestMapping(method=RequestMethod.GET,value="/asignaturas")
	public ResponseEntity<?> consultaAsignaturas(HttpSession session)
	{
		try
		{
			Validations validations = new Validations();
			// Obtenemos la lista asignaturas
			List<Asignatura> listaAsignaturas = validations.obtenerListaAsignaturas(session);
			
			// Pintamos en los logs en modo info
			log.info(listaAsignaturas);
			
			return ResponseEntity.ok().body(listaAsignaturas);
		}
		catch(HorarioException horarioException)
		{
			return ResponseEntity.status(400).body(horarioException.getBodyExceptionMessage());
			
		}
		catch(Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
										 Constants.ERR_GENERIC_EXCEPTION_MSG + "consultaAsignaturas",
										 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "consultaAsignaturas", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}
	
	/**
	 * Endpoint para asignar una asignatura a un profesor
	 * 
	 * @param idProfesor Id del profesor al que queremos asignar la asignatura
	 * @param nombreAsignatura Nombre de la asignatura
	 * @param curso Curso al que pertenece la asignatura
	 * @param etapa Etapa a la que pertenece la asignatura
	 * @param grupo Grupo al que pertenece la asignatura
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return Asignación del profesor a una asignatura
	 */
	@RequestMapping(method=RequestMethod.PUT,value="/asignaturas")
	public ResponseEntity<?> asignacionAsignaturas(@RequestHeader(value="idProfesor",required=true)String idProfesor,
			@RequestHeader(value="nombreAsignatura",required=true)String nombreAsignatura,
			@RequestHeader(value="curso",required=true)Integer curso,
			@RequestHeader(value="etapa",required=true)String etapa,
			@RequestHeader(value="grupo",required=true)String grupo, HttpSession session)
	{
		try
		{
			Validations validations = new Validations();
			
			// Obtenemos la lista de profesores
			List<Profesor> listaProfesores = validations.obtenerListaProfesores(session);
			
			// Obtenemos la lista de asignaturas
			List<Asignatura> listaAsignaturas = validations.obtenerListaAsignaturas(session);
			
			// Obtenemos la lista de cursos
			List<Curso> listaCursos = validations.obtenerListaCursos(session);
			
			// Método para validar el id de profesor
			validations.validarSiExisteProfesor(idProfesor, listaProfesores);
			
			// Método para obtener si el nombre de la asignatura existe
			validations.validarSiExisteNombreAsignatura(nombreAsignatura, listaAsignaturas);
			
			// Método para obtener si el curso existe y creacion del objeto asignatura
			Asignatura asignatura = validations.creaAsignaturaParaAsignarAProfesor(nombreAsignatura, curso, etapa, grupo, listaAsignaturas, listaCursos);

			// Creamos la lista de asignaturas asignadas al profesor y la añadimos
			List<Asignatura> listaAsignaturasAsociadasAProfesor = new ArrayList<Asignatura>();
			listaAsignaturasAsociadasAProfesor.add(asignatura);
			
			//  Asignanación al mapa de asignaturas
			validations.asignacionMapaAsignaturas(idProfesor, session, listaAsignaturasAsociadasAProfesor, asignatura);
			
			return ResponseEntity.ok().build();
		}
		catch(HorarioException horarioException)
		{
			return ResponseEntity.status(400).body(horarioException.getBodyExceptionMessage());
		}
		catch(Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
							 Constants.ERR_GENERIC_EXCEPTION_MSG + "asignacionAsignaturas",
							 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "asignacionAsignaturas", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}
	
	/**
	 * Endpoint para eliminar asignaturas
	 * 
	 * @param idProfesor Id del profesor al que queremos eliminar la asignatura
	 * @param nombreAsignatura Nombre de la asignatura
	 * @param curso Nurso al que pertenece la asignatura
	 * @param etapa Etapa a la que pertenece la asignatura
	 * @param grupo Grupo al que pertenece la asignatura
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return 200 si todo ha ido bien
	 */
	@RequestMapping(method=RequestMethod.DELETE,value="/asignaturas")
	public ResponseEntity<?> borrarAsignaturas(@RequestHeader(value="idProfesor",required=true)String idProfesor,
			@RequestHeader(value="nombreAsignatura",required=true)String nombreAsignatura,
			@RequestHeader(value="curso",required=true)Integer curso,
			@RequestHeader(value="etapa",required=true)String etapa,
			@RequestHeader(value="grupo",required=true)String grupo,HttpSession session)
	{
		try
		{
			Validations validations = new Validations();
			// Obtenemos el mapa de asignaturas
			Map<String,List<Asignatura>> asignacion = validations.obtenerMapaAsignaturas(session);
			
			// Obtenemos la lista de asignaturas del profesor
			List<Asignatura> listaMapaAsignatura = asignacion.get(idProfesor);
			
			int i = 0;
			boolean encontrado = false;
			
			while(i < listaMapaAsignatura.size() && !encontrado) 
			{
				encontrado = listaMapaAsignatura.get(i).getNombreAsignatura().equalsIgnoreCase(nombreAsignatura) && listaMapaAsignatura.get(i).getCurso()==curso && listaMapaAsignatura.get(i).getEtapa().equalsIgnoreCase(etapa) && listaMapaAsignatura.get(i).getGrupo().equalsIgnoreCase(grupo) ;
				if (encontrado)
				{
					listaMapaAsignatura.remove(i);
				}
				i++;
			}
			if(!encontrado) 
			{
				String error = "No existe esa asignación de asignaturas: " + curso + ", etapa " + etapa + ", grupo " + grupo + " o asignatura " + nombreAsignatura;
				
				
				// Log con el error
				log.error(error);
				
				throw new HorarioException(Constants.ERR_ASIGNACION_ASIGNATURA, error);
			}
			
			session.setAttribute(Constants.SESION_MAPA_ASIGNATURAS, asignacion);
			
			return ResponseEntity.ok().build();
		}
		catch(HorarioException horarioException)
		{
			return ResponseEntity.status(400).body(horarioException.getBodyExceptionMessage());
		}
		catch(Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
							 Constants.ERR_GENERIC_EXCEPTION_MSG + "borrarAsignaturas",
							 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "borrarAsignaturas", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}	
	
	/**
	 * Endpoint para subir las reducciones
	 * 
	 * @param csvFile Fichero csv a parsear
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return 200 si todo ha ido bien
	 */
	@RequestMapping(method=RequestMethod.POST,value="/reducciones",consumes="multipart/form-data")
	public ResponseEntity<?> uploadReducciones(@RequestParam(value="csv",required=true)MultipartFile csvFile,HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			Validations validations = new Validations();
			
			// Obtenemos la lista de cursos
			List<Curso> listaCursos = validations.obtenerListaCursos(session);
			
			// Llamamos el metodo para parsear reducciones
			List<Reduccion> listaReducciones = parse.parseReducciones(csvFile, listaCursos);
			
			// Guardamos la lista en session
			session.setAttribute(Constants.SESION_LISTA_REDUCCIONES, listaReducciones);
			
			// Pintamos en los logs en modo info
			log.info(listaReducciones);
			
			return ResponseEntity.ok().build();
		}
		catch(HorarioException horarioException)
		{
			return ResponseEntity.status(400).body(horarioException.getBodyExceptionMessage());
		}
		catch(Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
							 Constants.ERR_GENERIC_EXCEPTION_MSG + "uploadReducciones",
							 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "uploadReducciones", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}
	
	/**
	 * Endpoint para obtener lista de reducciones
	 * 
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return Lista de reducciones
	 */

	@RequestMapping(method=RequestMethod.GET,value="/reducciones")
	public ResponseEntity<?> consultaReducciones(HttpSession session)
	{
		try
		{
			Validations validations = new Validations();
			
			// Obtenemos la lista de reducciones
			List<Reduccion> listaReducciones = validations.obtenerListaReducciones(session);
			
			// Pintamos en los logs en modo info
			log.info(listaReducciones);
			
			return ResponseEntity.ok().body(listaReducciones);
		}
		catch(HorarioException horarioException)
		{
			return ResponseEntity.status(400).body(horarioException.getBodyExceptionMessage());
		}
		catch(Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
							 Constants.ERR_GENERIC_EXCEPTION_MSG + "consultaReducciones",
							 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "consultaReducciones", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}
	
	/**
	 * Endpoint para asignar reducciones a un profesor
	 * 
	 * @param idProfesor Id del profesor al que asignar la reducción
	 * @param idReduccion Id de la reducción 
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return 200 si todo ha ido bien
	 */
	@RequestMapping(method=RequestMethod.PUT,value="/reducciones")
	public ResponseEntity<?> asignacionReducciones(@RequestHeader(value="idProfesor",required=true)String idProfesor,
			@RequestHeader(value="idReduccion",required=true)String idReduccion,HttpSession session)
	{
		try
		{
			Validations validations = new Validations();
			
			// Obtenemos la lista de profesores
			List<Profesor> listaProfesores =validations.obtenerListaProfesores(session);
			
			// Obtenemos la lista de reducciones			
			List<Reduccion> listaReducciones = validations.obtenerListaReducciones(session);
			
			// Obtener id del profesor
			validations.validarSiExisteProfesor(idProfesor, listaProfesores);
			
			// Obtener la id de reduccion si existe
			validations.validarSiIdReduccionExiste(idReduccion, listaReducciones);
			
			// Realizar reducción
			validations.realizarReduccion(idProfesor, idReduccion, session, listaReducciones);
			
			return ResponseEntity.ok().build();
		}
		catch(HorarioException horarioException)
		{
			return ResponseEntity.status(400).body(horarioException.getBodyExceptionMessage());
		}
		catch(Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
							 Constants.ERR_GENERIC_EXCEPTION_MSG + "asignacionReducciones",
							 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "asignacionReducciones", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}
	/**
	 * Endpoint para borrar reducciones
	 * 
	 * @param idProfesor Id del profesor al que borrar la reducción
	 * @param idReduccion Id de la reducción 
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return 200 si todo ha ido bien
	 */
	@RequestMapping(method=RequestMethod.DELETE,value="/reducciones")
	public ResponseEntity<?> borrarReducciones(@RequestHeader(value="idProfesor",required=true)String idProfesor,
			@RequestHeader(value="idReduccion",required=true)String idReduccion,HttpSession session)
	{
		try
		{
			Validations validations = new Validations();
			
			// Obtenemos el mapa de reducciones	
			Map<String,List<ReduccionHoras>> asignacionReduccion = validations.obtenerMapaReduccion(session);
			
			// Obntenemos la lista de reducciones del profesor
			List<ReduccionHoras> listaMapaReducciones = asignacionReduccion.get(idProfesor);
			
			int i = 0;
			boolean encontrado = false;
			
			while (i < listaMapaReducciones.size() && !encontrado) 
			{
				encontrado = listaMapaReducciones.get(i).getIdReduccion().equalsIgnoreCase(idReduccion) ;
				{
					listaMapaReducciones.remove(i);
				}
				
				i++;
			}
			if(!encontrado) 
			{
				String error = "El profesor " + idProfesor + " no tiene la reduccion " + idReduccion;
				
				// Log con el error
				log.error(error);
				
				throw new HorarioException(Constants.ERR_REDUCCION_EXIS, error);
			}
			
			session.setAttribute(Constants.SESION_MAPA_REDUCCIONES, asignacionReduccion);
			
			return ResponseEntity.ok().build();
		}
		catch(HorarioException horarioException)
		{
			return ResponseEntity.status(400).body(horarioException.getBodyExceptionMessage());
		}
		catch(Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
							 Constants.ERR_GENERIC_EXCEPTION_MSG + "borrarReducciones",
							 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "borrarReducciones", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}
	
	/**
	 * Endpoint para asignar horas de guardias a un profesor
	 * 
	 * @param idProfesor Id del profesor al que asignar horas
	 * @param horasAsignadas Número de horas asignadas al profesor
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return 200 si todo ha ido bien
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.POST,value="/guardias")
	public ResponseEntity<?> uploadGuardias(@RequestHeader(value="idProfesor",required=true)String idProfesor,
			@RequestHeader(value="horasAsignadas",required=true)Integer horasAsignadas,HttpSession session)
	{
		try
		{
			Validations validations = new Validations();
			
			List<Profesor> listaProfesores =validations.obtenerListaProfesores(session);
			// Obtener id del profesor
			validations.validarSiExisteProfesor(idProfesor, listaProfesores);
			
			Map<String,Integer> mapaGuardias = (Map<String, Integer>) session.getAttribute(Constants.SESION_MAPA_GUARDIAS);
			// Comprobamos si se ha creado el mapa de guardias
			if(mapaGuardias == null)
			{
				mapaGuardias = new TreeMap<String, Integer>();
				mapaGuardias.put(idProfesor, horasAsignadas);
				session.setAttribute(Constants.SESION_MAPA_GUARDIAS, mapaGuardias);
			}
			else
			{
				mapaGuardias.put(idProfesor, horasAsignadas);
				session.setAttribute(Constants.SESION_MAPA_GUARDIAS, mapaGuardias);
			}
			
			// Pintamos en los logs en modo info
			log.info(mapaGuardias);
			
			return ResponseEntity.ok().build();
		}
		catch(HorarioException horarioException)
		{
			return ResponseEntity.status(400).body(horarioException.getBodyExceptionMessage());
		}
		catch(Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
							 Constants.ERR_GENERIC_EXCEPTION_MSG + "uploadGuardias",
							 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "uploadGuardias", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}
	
	/**
	 * Endpoint para obtener un resemun de un profesor
	 * 
	 * @param idProfesor Id del profesor sobre el que hacer el resumen
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return Un objeto resumen de profesor
	 */
	@RequestMapping(method=RequestMethod.GET,value="/profesores/resumen")
	public ResponseEntity<?> resumenProfesores(@RequestHeader(value="idProfesor",required=true)String idProfesor,HttpSession session)
	{
		try
		{
			Validations validations = new Validations();
			
			// Obtenemos la lista de profesores
			List<Profesor> listaProfesores = validations.obtenerListaProfesores(session);
			
			// Obtenemos el id del profesor
			validations.validarSiExisteProfesor(idProfesor, listaProfesores);
			
			// Obtenemos mapa de reduccion
			Map<String,List<ReduccionHoras>> mapaReduccion = validations.obtenerMapaReduccion(session);
			
			// Obtenemos el mapa de asignaturas
			Map<String, List<Asignatura>> mapaAsignatura = validations.obtenerMapaAsignaturas(session);
			
			// Obtenemos el resumen del profesor 	
			Overviews overviews = new Overviews();
			
			ResumenProfesor resumen = overviews.obtencionHorasProfesor(idProfesor, mapaReduccion, mapaAsignatura);

			return ResponseEntity.ok().body(resumen);
		}
		catch(HorarioException horarioException)
		{
			return ResponseEntity.status(400).body(horarioException.getBodyExceptionMessage());
		}
		catch(Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
							 Constants.ERR_GENERIC_EXCEPTION_MSG + "resumenProfesores",
							 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "resumenProfesores", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}
	
	/**
	 * Endpoint para obtener un resumen por departamento
	 * 
	 * @param nombreDepartamento Nombre del departamento para hacer resumen
	 * @param session Utilizado para guardar u obtener cosas en sesión
	 * @return Objeto resumen departamento
	 */
	@RequestMapping(method=RequestMethod.GET,value="/departamentos/resumen")
	public ResponseEntity<?> resumenDepartamento(@RequestHeader(value="nombreDepartamento",required=true)String nombreDepartamento,HttpSession session)
	{
		try
		{
			Overviews overviews = new Overviews();
			Validations validations = new Validations();
			
			// Obtenemos la lista departamentos en session
			List<Departamento> listaDepartamentos = validations.obtenerListaDepartamentos(session);
			Departamento departamento = new Departamento(nombreDepartamento);
			
			// Obtenemos el departamento
			validations.obtenerDepartamento(listaDepartamentos, departamento);
			
			Resumen resumen = overviews.resumenDepartamento(nombreDepartamento, session);	
			
			return ResponseEntity.ok().body(resumen);
		}
		catch(HorarioException horarioException)
		{
			return ResponseEntity.status(400).body(horarioException.getMessage());
		}
		catch(Exception exception)
		{
			HorarioException horarioException = 
					new HorarioException(Constants.ERR_GENERIC_EXCEPTION_CODE, 
							 Constants.ERR_GENERIC_EXCEPTION_MSG + "resumenDepartamento",
							 exception);
			log.error(Constants.ERR_GENERIC_EXCEPTION_MSG + "resumenDepartamento", horarioException);
			return ResponseEntity.status(500).body(horarioException.getBodyExceptionMessage());
		}
	}
}