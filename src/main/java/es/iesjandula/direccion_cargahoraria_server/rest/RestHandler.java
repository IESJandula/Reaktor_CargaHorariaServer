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
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

import es.iesjandula.direccion_cargahoraria_server.utils.Constants;
/**
 * clase RestHandler
 */
@RequestMapping(value="/carga_horaria")
@RestController
@Log4j2
public class RestHandler 
{
	/**
	 * endpoint para subir los departamentos
	 * 
	 * @param csvFile fichero csv a parsear
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @return
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
			session.setAttribute("listaDepartamentos", listaDepartamentos);
			log.info(listaDepartamentos);
			
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
	 * endpoint para obtener los departamentos
	 * 
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @return lista de departamentos
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,value="/departamentos")
	public ResponseEntity<?> consultaDepartamentos(HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			List<Departamento> listaDepartamentos = (List<Departamento>) session.getAttribute("listaDepartamentos");
			//comprobamos si la lista existe
			parse.comprobarListaDepartamentos(session, listaDepartamentos);
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
	 * endpoint para subir los cursos
	 * 
	 * @param csvFile fichero csv a parsear
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST,value="/cursos",consumes="multipart/form-data")
	public ResponseEntity<?> uploadCursos(@RequestParam(value="csv",required=true)MultipartFile csvFile,HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			//parseamos el csv con el endpoint
			List<Curso> listaCursos = parse.parseCursos(csvFile);
			//guardamos la lista en session
			session.setAttribute("listaCursos", listaCursos);
			log.info(listaCursos);
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
	 * endpoint para obtener lista de cursos
	 * 
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,value="/cursos")
	public ResponseEntity<?> consultaCursos(HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			List<Curso> listaCursos = (List<Curso>) session.getAttribute("listaCursos");
			//comprobamos si la lista existe
			parse.comprobarListaCursos(session, listaCursos);
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
	 * endpoint para subir los profesores
	 * 
	 * @param csvFile fichero csv a parsear
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.POST,value="/profesores",consumes="multipart/form-data")
	public ResponseEntity<?> uploadProfesores(@RequestParam(value="csv",required=true)MultipartFile csvFile,HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			//obtenemos la lista de departamentos guardada en session
			List<Departamento> listaDepartamentos = (List<Departamento>) session.getAttribute("listaDepartamentos");
			//comprobamos si existe la lista de departamentos
			parse.comprobarListaDepartamentos(session, listaDepartamentos);
			//parseamos el csv con el endpoint
			List<Profesor> listaProfesores = parse.parseProfesores(csvFile,listaDepartamentos);
			//guardamos la lista en session
			session.setAttribute("listaProfesores", listaProfesores);
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
	 * endpoint para obtener la lista profesores
	 * 
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,value="/profesores")
	public ResponseEntity<?> consultaProfesores(HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			List<Profesor> listaProfesores =(List<Profesor>) session.getAttribute("listaProfesores");
			//comprobamos si existe la lista profesores
			parse.comprobarListaProfesores(session, listaProfesores);
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
	 * endpoint para subir las asignaturas
	 * 
	 * @param csvFile fichero csv a parsear
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.POST,value="/asignaturas",consumes="multipart/form-data")
	public ResponseEntity<?> uploadAsignaturas(@RequestParam(value="csv",required=true)MultipartFile csvFile,HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			//comprobamos si existe la lista de departamentos
			List<Departamento> listaDepartamentos = (List<Departamento>) session.getAttribute("listaDepartamentos");
			parse.comprobarListaDepartamentos(session, listaDepartamentos);
			//comprobamos si existe la lista cursos
			List<Curso> listaCursos = (List<Curso>) session.getAttribute("listaCursos");
			parse.comprobarListaCursos(session, listaCursos);
			//parseamos el csv con el endpoint
			List<Asignatura> listaAsignaturas = parse.parseAsignaturas(csvFile,listaCursos,listaDepartamentos);
			//guardamos la lista en session
			session.setAttribute("listaAsignaturas", listaAsignaturas);
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
	 * endpoint para obtener las asignaturas
	 * 
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,value="/asignaturas")
	public ResponseEntity<?> consultaAsignaturas(HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			List<Asignatura> listaAsignaturas =(List<Asignatura>) session.getAttribute("listaAsignaturas");
			//comprobamos si existe la lista asignaturas
			parse.comprobarListaAsignaturas(session, listaAsignaturas);
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
	 * endpoint para asignar una asignatura a un profesor
	 * 
	 * @param idProfesor id del profesor al que queremos asignar la asignatura
	 * @param nombreAsignatura nombre de la asignatura
	 * @param curso curso al que pertenece la asignatura
	 * @param etapa etapa a la que pertenece la asignatura
	 * @param grupo grupo al que pertenece la asignatura
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @return asignación del profesor a una asignatura
	 */
	@SuppressWarnings({ "unchecked"})
	@RequestMapping(method=RequestMethod.PUT,value="/asignaturas")
	public ResponseEntity<?> asignacionAsignaturas(@RequestHeader(value="idProfesor",required=true)String idProfesor,
			@RequestHeader(value="nombreAsignatura",required=true)String nombreAsignatura,
			@RequestHeader(value="curso",required=true)Integer curso,
			@RequestHeader(value="etapa",required=true)String etapa,
			@RequestHeader(value="grupo",required=true)String grupo,HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			Map<String,List<Asignatura>> asignacion = (Map<String, List<Asignatura>>) session.getAttribute("mapaAsignaturas");
			List<Asignatura> datosAsignacion = new ArrayList<Asignatura>();
			List<Profesor> listaProfesores = (List<Profesor>) session.getAttribute("listaProfesores");
			parse.comprobarListaProfesores(session, listaProfesores);
			List<Asignatura> listaAsignaturas = (List<Asignatura>) session.getAttribute("listaAsignaturas");
			parse.comprobarListaAsignaturas(session, listaAsignaturas);
			List<Curso> listaCursos = (List<Curso>) session.getAttribute("listaCursos");
			parse.comprobarListaCursos(session, listaCursos);
			Asignatura asignaturaObject = new Asignatura();
			
			String resultado="Asignacion creada correctamente";
			//metodo para validar el id de profesor
			parse.comprobarIdProfesor(idProfesor, listaProfesores);
			//metodo para comprobar si el nombre de la asignatura existe
			parse.comprobarNombreAsignaturaExiste(nombreAsignatura, listaAsignaturas);
			//asignamos el nombre de la asignatura
			asignaturaObject.setNombreAsinatura(nombreAsignatura);
			//metodo para comprobar si el curso existe y creacion del objeto asignatura
			resultado=parse.comprobacionCreacionObjeto(nombreAsignatura, curso, etapa, grupo, datosAsignacion, listaAsignaturas,listaCursos, asignaturaObject);
			//comprobamos si el mapa existe
			if (session.getAttribute("mapaAsignaturas") == null) 
			{
			    asignacion = new TreeMap<String, List<Asignatura>>();
			    asignacion.put(idProfesor, datosAsignacion);
			    session.setAttribute("mapaAsignaturas", asignacion);
			}
			else 
			{
			    asignacion = (Map<String, List<Asignatura>>) session.getAttribute("mapaAsignaturas");
			    //comprobamos si existe el idProfesor en el mapa
			    if (asignacion.containsKey(idProfesor)) 
			    {
			        List<Asignatura> existingAsignaturas = asignacion.get(idProfesor);
			        if(existingAsignaturas.contains(asignaturaObject)) 
			        {
			        	resultado="la asignacion ya existe";
			        }
			        else 
			        {
			        	existingAsignaturas.addAll(datosAsignacion);
			        }
			    }
			    else
			    {
			        asignacion.put(idProfesor, datosAsignacion);
			    }
			    session.setAttribute("mapaAsignaturas", asignacion);
			}
			log.info(asignacion);
			return ResponseEntity.ok().body(resultado);
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
	 * endpoint para eliminar asignaturas
	 * 
	 * @param idProfesor id del profesor al que queremos eliminar la asignatura
	 * @param nombreAsignatura nombre de la asignatura
	 * @param curso curso al que pertenece la asignatura
	 * @param etapa etapa a la que pertenece la asignatura
	 * @param grupo grupo al que pertenece la asignatura
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @return 
	 */
	@SuppressWarnings({ "unchecked"})
	@RequestMapping(method=RequestMethod.DELETE,value="/asignaturas")
	public ResponseEntity<?> borrarAsignaturas(@RequestHeader(value="idProfesor",required=true)String idProfesor,
			@RequestHeader(value="nombreAsignatura",required=true)String nombreAsignatura,
			@RequestHeader(value="curso",required=true)Integer curso,
			@RequestHeader(value="etapa",required=true)String etapa,
			@RequestHeader(value="grupo",required=true)String grupo,HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			Map<String,List<Asignatura>> asignacion = (Map<String, List<Asignatura>>) session.getAttribute("mapaAsignaturas");
			parse.comprobarMapaAsignaturas(session, asignacion);
			List<Asignatura> listaMapaAsignatura = asignacion.get(idProfesor);
			int i = 0;
			boolean encontrado = false;
			String resultado= "No existe la asignacion";
			while(i < listaMapaAsignatura.size() && !encontrado) 
			{
				if(listaMapaAsignatura.get(i).getNombreAsinatura().equalsIgnoreCase(nombreAsignatura) && listaMapaAsignatura.get(i).getCurso()==curso && listaMapaAsignatura.get(i).getEtapa().equalsIgnoreCase(etapa) && listaMapaAsignatura.get(i).getGrupo().equalsIgnoreCase(grupo)) 
				{
					encontrado = true;
					listaMapaAsignatura.remove(i);
					resultado="asignacion borrada correctamente";
				}
				i++;
			}
			asignacion.get(idProfesor).addAll(listaMapaAsignatura);
			session.setAttribute("mapaAsinaturas", asignacion);
			return ResponseEntity.ok().body(resultado);
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
	 * endpoint para subir las reducciones
	 * 
	 * @param csvFile fichero csv a parsear
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.POST,value="/reducciones",consumes="multipart/form-data")
	public ResponseEntity<?> uploadReducciones(@RequestParam(value="csv",required=true)MultipartFile csvFile,HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			List<Curso> listaCursos = (List<Curso>) session.getAttribute("listaCursos");
			//comprobamos si la lsita de cursos existe
			parse.comprobarListaCursos(session, listaCursos);
			//llamamos el metodo para parsear reducciones
			List<Reduccion> listaReducciones = parse.parseReducciones(csvFile,listaCursos);
			//guardamos la lista en session
			session.setAttribute("listaReducciones", listaReducciones);
			log.info(listaReducciones);
			return ResponseEntity.ok().body("Reducciones subidas correctamente");
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
	 * endpoint para obtener lista de reducciones
	 * 
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,value="/reducciones")
	public ResponseEntity<?> consultaReducciones(HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			List<Reduccion> listaReducciones = (List<Reduccion>) session.getAttribute("listaReducciones");
			//comprobamos si la lista reducciones existe
			parse.comprobarListaReducciones(session, listaReducciones);
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
	 * endpoint para asignar reducciones a un profesor
	 * 
	 * @param idProfesor id del profesor al que asignar la reducción
	 * @param idReduccion id de la reducción 
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	@RequestMapping(method=RequestMethod.PUT,value="/reducciones")
	public ResponseEntity<?> asignacionReducciones(@RequestHeader(value="idProfesor",required=true)String idProfesor,
			@RequestHeader(value="idReduccion",required=true)String idReduccion,HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			Map<String,List<ReduccionHoras>> asignacionReduccion = (Map<String, List<ReduccionHoras>>) session.getAttribute("mapaReduccion");	
			
			List<Profesor> listaProfesores = (List<Profesor>) session.getAttribute("listaProfesores");
			parse.comprobarListaProfesores(session, listaProfesores);
			List<Reduccion> listaReducciones = (List<Reduccion>) session.getAttribute("listaReducciones");
			parse.comprobarListaReducciones(session, listaReducciones);
			List<ReduccionHoras> listaReduccionHoras = new ArrayList<ReduccionHoras>();

			//recorremos la lista de profesores para comprobar si existe el idprofesor que recibimos
			parse.comprobarIdProfesor(idProfesor, listaProfesores);
			//recorremos la lista reducciones para comprobar si existe el idreduccion que recibimos
			parse.comprobarIdReduccionExiste(idReduccion, listaReducciones);
			
			//llamamos al metodo realizarReduccion
			asignacionReduccion = parse.realizarReduccion(idProfesor, idReduccion, session, listaReducciones, listaReduccionHoras);
			
			log.info(asignacionReduccion);
			return ResponseEntity.ok().body("Asignacion de reduccion creada correctamente");
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
	 * endpoint para borrar reducciones
	 * 
	 * @param idProfesor id del profesor al que borrar la reducción
	 * @param idReduccion id de la reducción 
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	@RequestMapping(method=RequestMethod.DELETE,value="/reducciones")
	public ResponseEntity<?> borrarReducciones(@RequestHeader(value="idProfesor",required=true)String idProfesor,
			@RequestHeader(value="idReduccion",required=true)String idReduccion,HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			Map<String,List<ReduccionHoras>> asignacionReduccion = (Map<String, List<ReduccionHoras>>) session.getAttribute("mapaReduccion");	
			parse.comprobarMapaReduccion(session, asignacionReduccion);
			List<ReduccionHoras> listaMapaReducciones = asignacionReduccion.get(idProfesor);
			int i = 0;
			boolean encontrado = false;
			String resultado= "No existe la reduccion";
			while(i < listaMapaReducciones.size() && !encontrado) 
			{
				if(listaMapaReducciones.get(i).getIdReduccion().equalsIgnoreCase(idReduccion))
				{
					encontrado = true;
					listaMapaReducciones.remove(i);
					resultado="reduccion borrada correctamente";
				}
				i++;
			}
			asignacionReduccion.get(idProfesor).addAll(listaMapaReducciones);
			session.setAttribute("mapaAsinaturas", asignacionReduccion);
			return ResponseEntity.ok().body(resultado);
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
	 * endpoint para asignar horas de guardias a un profesor
	 * 
	 * @param idProfesor id del profesor al que asignar horas
	 * @param horasAsignadas número de horas asignadas al profesor
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.POST,value="/guardias")
	public ResponseEntity<?> uploadGuardias(@RequestHeader(value="idProfesor",required=true)String idProfesor,
			@RequestHeader(value="horasAsignadas",required=true)Integer horasAsignadas,HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			List<Profesor> listaProfesores = (List<Profesor>) session.getAttribute("listaProfesores");
			parse.comprobarListaProfesores(session, listaProfesores);
			//bucle para comprobar si existe el idProfesor recibido
			parse.comprobarIdProfesor(idProfesor, listaProfesores);
			
			Map<String,Integer> mapaGuardias;
			//comprobamos si se ha creado el mapa de guardias
			if(session.getAttribute("mapaGuardias")==null)
			{
				mapaGuardias = new TreeMap<String, Integer>();
				mapaGuardias.put(idProfesor, horasAsignadas);
				session.setAttribute("mapaGuardias", mapaGuardias);
			}
			else
			{
				mapaGuardias=(Map<String, Integer>) session.getAttribute("mapaGuardias");
				mapaGuardias.put(idProfesor, horasAsignadas);
				session.setAttribute("mapaGuardias", mapaGuardias);
			}
			
			log.info(mapaGuardias);
			return ResponseEntity.ok().body("Guardia subida correctamente");
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
	 * endpoint para obtener un resemun de un profesor
	 * 
	 * @param idProfesor id del profesor sobre el que hacer el resumen
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,value="/profesores/resumen")
	public ResponseEntity<?> resumenProfesores(@RequestHeader(value="idProfesor",required=true)String idProfesor,HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			List<Profesor> listaProfesores = (List<Profesor>) session.getAttribute("listaProfesores");
			parse.comprobarListaProfesores(session, listaProfesores);
			//bucle para comprobar si existe el idProfesor
			parse.comprobarIdProfesor(idProfesor, listaProfesores);
			Map <String,List<ReduccionHoras>> mapaReduccion = (Map<String, List<ReduccionHoras>>) session.getAttribute("mapaReduccion");
			mapaReduccion = parse.comprobarMapaReduccion(session, mapaReduccion);
			Map <String, List<Asignatura>> mapaAsignatura = (Map<String, List<Asignatura>>) session.getAttribute("mapaAsignaturas");
			mapaAsignatura = parse.comprobarMapaAsignaturas(session, mapaAsignatura);
			//obtenemos el resumen mediante el metodo 	
			ResumenProfesor resumen = parse.obtencionHorasProfesor(idProfesor, mapaReduccion, mapaAsignatura);

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
	 * endpoint para obtener un resumen por departamento
	 * 
	 * @param nombreDepartamento nombre del departamento para hacer resumen
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,value="/departamentos/resumen")
	public ResponseEntity<?> resumenDepartamento(@RequestHeader(value="nombreDepartamento",required=true)String nombreDepartamento,HttpSession session)
	{
		try
		{
			//obtenemos la lista departamentos en session
			List<Departamento> listaDepartamentos = (List<Departamento>) session.getAttribute("listaDepartamentos");
			Parse parse = new Parse();
			parse.comprobarListaDepartamentos(session, listaDepartamentos);
			Departamento departamento = new Departamento(nombreDepartamento);
			//comprobamos si existe el departamento
			parse.comprobarDepartamentoExiste(listaDepartamentos, departamento);
			
			Resumen resumen = parse.resumenDepartamento(nombreDepartamento, session);	
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