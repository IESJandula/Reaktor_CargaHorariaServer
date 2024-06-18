package es.iesjandula.direccion_cargahoraria_server.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.web.multipart.MultipartFile;

import es.iesjandula.direccion_cargahoraria_server.exception.HorarioException;
import es.iesjandula.direccion_cargahoraria_server.models.Asignatura;
import es.iesjandula.direccion_cargahoraria_server.models.Curso;
import es.iesjandula.direccion_cargahoraria_server.models.Departamento;
import es.iesjandula.direccion_cargahoraria_server.models.Profesor;
import es.iesjandula.direccion_cargahoraria_server.models.Reduccion;
import es.iesjandula.direccion_cargahoraria_server.models.ReduccionHoras;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Validations
{
	/**
	 * Método para comprobar si el fichero esta vacio
	 * 
	 * @param metodoLlamada método que llama a esta funcionalidad
	 * @param ficheroMultipart fichero Multipart
	 * @return El contenido del fichero
	 * @throws HorarioException Se lanza si el contenido del fichero da algun error
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
	/**
	 * Método para obtener el departamento
	 * 
	 * @param listaDepartamentos Lista de departamentos
	 * @param departamento Objeto departamento
	 * @throws HorarioException Se lanzara si no se encuentra el departamento
	 */
	public void obtenerDepartamento(List<Departamento> listaDepartamentos, Departamento departamento) throws HorarioException
	{
		// comprobamos que el departamento existe
		if (!listaDepartamentos.contains(departamento))
		{
			String error = "Departamento no encontrado";
			
			// Log con el error
			log.error(error);
			
			throw new HorarioException(Constants.ERR_DEPART_DONT_EXIS, error);
		}
	}
	/**
	 * Método para comprobar si el curso existe
	 * 
	 * @param listaCursos Lista de cursos
	 * @param curso Objeto curso
	 * @throws HorarioException Se lanzara si el curso no existe
	 */
	public void validarExistenciaCurso(List<Curso> listaCursos, Curso curso) throws HorarioException
	{
		// comprobamos que el curso existe
		if (!listaCursos.contains(curso))
		{
			String error = "Curso no encontrado: " + curso;
			
			// Log con el error
			log.error(error);
			
			throw new HorarioException(Constants.ERR_CURSO_EXIS, error);
		}
	}
	
	/**
	 * Método para validar y crear el objeto
	 * 
	 * @param nombreAsignatura Nombre de la asignatura
	 * @param curso Número del curso
	 * @param etapa Etapa del curso
	 * @param grupo Grupo del curso
	 * @param datosAsignacion Lista de asignaturas para asignar
	 * @param listaAsignaturas Lista de asignaturas
	 * @param listaCursos Lista de cursos
	 * @param asignaturaObject Objeto de asignatura
	 * @throws HorarioException Se lanzara si no encuentra la asignatura
	 */
	public void comprobacionCreacionObjeto(String nombreAsignatura, Integer curso, String etapa, String grupo,
			List<Asignatura> datosAsignacion, List<Asignatura> listaAsignaturas, List<Curso> listaCursos,
			Asignatura asignaturaObject) throws HorarioException
	{
		boolean encontrado = false;
		int i = 0;
		while (i < listaAsignaturas.size() && !encontrado)
		{
			encontrado = listaAsignaturas.get(i).getCurso() == curso && listaAsignaturas.get(i).getEtapa().equals(etapa) && 
						 listaAsignaturas.get(i).getGrupo().equals(grupo) && 
						 listaAsignaturas.get(i).getNombreAsignatura().equalsIgnoreCase(nombreAsignatura) ;
			if (encontrado)
			{
				asignaturaObject.setDepartamento(listaAsignaturas.get(i).getDepartamento());
				asignaturaObject.setNumeroHorasSemanales(listaAsignaturas.get(i).getNumeroHorasSemanales());
			}
			
			i++;
		}
		if(!encontrado) 
		{
			String errorString = "No se ha encontrado la asignatura";
			
			// Log con el error
			log.error(errorString);
			
			throw new HorarioException(Constants.ERR_ASIGNATURA, errorString);
		}
		
		asignaturaObject.setCurso(curso);
		asignaturaObject.setEtapa(etapa);
		asignaturaObject.setGrupo(grupo);
		datosAsignacion.add(asignaturaObject);
	}
	
	/**
	 * Método para obtener el id del profesor
	 * 
	 * @param idProfesor Id del profesor
	 * @param listaProfesores Lista de profesores
	 * @throws HorarioException Se lanzara si no encuentra el profesor
	 */
	public void obtenerIdProfesor(String idProfesor, List<Profesor> listaProfesores) throws HorarioException 
	{
		boolean idProfesorExiste = false;
		int i = 0;
		while (i < listaProfesores.size() && !idProfesorExiste)
		{
			if (listaProfesores.get(i).getIdProfesor().equals(idProfesor))
			{
				idProfesorExiste = true;
			}
			i++;
		}
		if (!idProfesorExiste)
		{
			String error = "Profesor no encontrado";
			
			// Log con el error
			log.error(error);
			
			throw new HorarioException(Constants.ERR_PROFESOR_ID, error);
		}
	}

	/**
	 * Método para obtener que la lista de departamentos existe
	 * 
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @return Lista de departamentos
	 * @throws HorarioException se lanzará esta excepción si la lista de departamentos es nula
	 */
	@SuppressWarnings("unchecked")
	public List<Departamento> obtenerListaDepartamentos(HttpSession session)
			throws HorarioException
	{
		List<Departamento> listaDepartamentos = (List<Departamento>) session.getAttribute(Constants.SESION_LISTA_DEPARTAMENTOS) ;

		if (listaDepartamentos == null)
		{
			String error = "Los departamentos no han sido cargados en sesion todavía";
			
			// Log con el error
			log.error(error);
			
			throw new HorarioException(Constants.ERR_LIST_NULL_CODE, error);
			
		}

		return listaDepartamentos;
	}

	/**
	 * Método para obtener si la lista de cursos existe
	 * 
	 * @param session Utilizado para guardas u obtener cosas en sesión 
	 * @param listaCursos Lista de cursos
	 * @return Lista de cursos
	 * @throws HorarioException se lanzará esta excepción si la lista es nula
	 */
	@SuppressWarnings("unchecked")
	public List<Curso> obtenerListaCursos(HttpSession session) throws HorarioException
	{
		List<Curso> listaCursos = (List<Curso>) session.getAttribute(Constants.SESION_LISTA_CURSOS);
		
		if (listaCursos == null)
		{
			String error = "Los cursos no han sido cargados en sesion todavía";
			
			// Log con el error
			log.error(error);
			
			throw new HorarioException(Constants.ERR_LIST_NULL_CODE, error);
		}
		
		return listaCursos;
	}

	/**
	 * Método para obtener la lista de profesores
	 * 
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @param listaProfesores Lista de profesores
	 * @return Lista de profesores
	 * @throws HorarioException se lanzará esta excepción si la lista es nula
	 */
	@SuppressWarnings("unchecked")
	public List<Profesor> obtenerListaProfesores(HttpSession session)
			throws HorarioException
	{
		List<Profesor> listaProfesores =(List<Profesor>) session.getAttribute(Constants.SESION_LISTA_PROFESORES);
		
		if (listaProfesores == null)
		{
			String error = "Los profesores no han sido cargados en sesion todavía";
			
			// Log con el error
			log.error(error);
			
			throw new HorarioException(Constants.ERR_LIST_NULL_CODE, error);
			
		}
		return listaProfesores;
	}

	/**
	 * Método para obtener la lista de asignaturas
	 * 
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @param listaAsignaturas Lista de asignaturas
	 * @return Lista de asignaturas
	 * @throws HorarioException se lanzará esta excepción si la lista es nula
	 */
	@SuppressWarnings("unchecked")
	public List<Asignatura> obtenerListaAsignaturas(HttpSession session)
			throws HorarioException
	{
		List<Asignatura> listaAsignaturas =(List<Asignatura>) session.getAttribute(Constants.SESION_LISTA_ASIGNATURAS);
		
		if (listaAsignaturas == null)
		{
			String error = "Las asignaturas no han sido cargadas en sesion todavía";
			
			// Log con el error
			log.error(error);
			
			throw new HorarioException(Constants.ERR_LIST_NULL_CODE, error);
		}
		return listaAsignaturas;
	}

	/**
	 * Método para obtener la lista de reducciones
	 * 
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @param listaReducciones Lista de reducciones
	 * @return Lista de reducciones
	 * @throws HorarioException se lanzará esta excepción si la lista es nula
	 */
	@SuppressWarnings("unchecked")
	public List<Reduccion> obtenerListaReducciones(HttpSession session)
			throws HorarioException
	{
		List<Reduccion> listaReducciones = (List<Reduccion>) session.getAttribute(Constants.SESION_LISTA_REDUCCIONES);
		
		if (listaReducciones == null)
		{
			String error = "Las reducciones no han sido cargadas en sesion todavía";
			
			// Log con el error
			log.error(error);
			
			throw new HorarioException(Constants.ERR_LIST_NULL_CODE, error);
		}
		return listaReducciones;
	}


	/**
	 * Método para obtener si la id reduccion existe
	 * 
	 * @param idReduccion Id de la reduccion
	 * @param listaReducciones Lista de reduccionnes
	 * @throws HorarioException se lanzará esta excepción si la reduccion no existe
	 */
	public void obtenerIdReduccionExiste(String idReduccion, List<Reduccion> listaReducciones)
			throws HorarioException
	{
		boolean idReduccionExiste = false;
		int i = 0;
		while (i < listaReducciones.size() && !idReduccionExiste)
		{
			if (listaReducciones.get(i).getIdReduccion().equals(idReduccion))
			{
				idReduccionExiste = true;
			}
			i++;
		}
		if (!idReduccionExiste)
		{
			String error = "Reduccion no encontrada";
			
			// Log con el error
			log.error(error);
			
			throw new HorarioException(Constants.ERR_ID_REDUCCION, error);
		}
	}

	/**
	 * Método para obtener si el nombre de asignatura existe
	 * 
	 * @param nombreAsignatura Nombre de la asignatura
	 * @param listaAsignaturas Lista de asignaturas
	 * @param asignaturaExiste Booleano para obtener si la asignatura existe
	 * @throws HorarioException Se lanzará esta excepción si la asignatura no existe
	 */
	public boolean obtenerNombreAsignaturaExiste(String nombreAsignatura, List<Asignatura> listaAsignaturas)
			throws HorarioException
	{
		boolean asignaturaExiste = false;
		int i = 0;
		while (i < listaAsignaturas.size() && !asignaturaExiste)
		{
			if (listaAsignaturas.get(i).getNombreAsignatura().equalsIgnoreCase(nombreAsignatura))
			{
				asignaturaExiste = true;
			}
			i++;
		}
		if (!asignaturaExiste)
		{
			String error = "Asignatura no encontrada";
			
			// Log con el error
			log.error(error);
			
			throw new HorarioException(Constants.ERR_NOMBRE_ASIGNATURA, error);
		}
		return asignaturaExiste;
	}
	
	/**
	 * Método para obtener la id de la reducción
	 * 
	 * @param idReduccion Id de la reducción
	 * @param listaReducciones Lista de Reducciones
	 * @param listaReduccionHoras Lista de horas de reducción
	 * @param reduccionHoras Objeto de reducción horas
	 * @param reduccionEncontrada boolean para comprobar si existe
	 */
	public void obtenerReduccionId(String idReduccion, List<Reduccion> listaReducciones,
			List<ReduccionHoras> listaReduccionHoras, ReduccionHoras reduccionHoras, boolean reduccionEncontrada)
	{
		int i = 0;
		while (i < listaReducciones.size() && !reduccionEncontrada)
		{
			reduccionEncontrada = listaReducciones.get(i).getIdReduccion().equalsIgnoreCase(idReduccion) ;
			if (reduccionEncontrada)
			{
				reduccionHoras.setIdReduccion(idReduccion);
				reduccionHoras.setNumHoras(listaReducciones.get(i).getNumeroHoras());
			}
			
			i++;
		}
		listaReduccionHoras.add(reduccionHoras);
	}
	
	/**
	 * Método para realizar una reduccion
	 * 
	 * @param idProfesor Id del profesor
	 * @param idReduccion Id de la reduccion
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @param listaReducciones Lista de reducciones
	 * @param listaReduccionHoras Lista de las horas de reducción
	 * @return Un mapa de reducciones
	 * @throws HorarioException Se lanzara si no se puede realizar la reducción
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<ReduccionHoras>> realizarReduccion(String idProfesor, String idReduccion,
			HttpSession session, List<Reduccion> listaReducciones, List<ReduccionHoras> listaReduccionHoras)
			throws HorarioException
	{
		ReduccionHoras reduccionHoras = new ReduccionHoras();
		boolean reduccionEncontrada = false;
		
		// Obtenemos la reduccion id
		Validations validations = new Validations();
		validations.obtenerReduccionId(idReduccion, listaReducciones, listaReduccionHoras, reduccionHoras, reduccionEncontrada);
		
		Map<String, List<ReduccionHoras>> asignacionReduccion = null;
		if (session.getAttribute(Constants.SESION_MAPA_REDUCCIONES) == null)
		{
			asignacionReduccion = new TreeMap<String, List<ReduccionHoras>>();
			asignacionReduccion.put(idProfesor, listaReduccionHoras);
			session.setAttribute(Constants.SESION_MAPA_REDUCCIONES, asignacionReduccion);
		}
		else
		{
			asignacionReduccion = (Map<String, List<ReduccionHoras>>) session.getAttribute(Constants.SESION_MAPA_REDUCCIONES);
			this.asignarReduccion(idProfesor, idReduccion, listaReduccionHoras, asignacionReduccion);
			
			session.setAttribute(Constants.SESION_MAPA_REDUCCIONES, asignacionReduccion);
		}
		return asignacionReduccion;
	}
	/**
	 * Método para asignar una reducción
	 * 
	 * @param idProfesor Id del profesor
	 * @param idReduccion Id de la reducción
	 * @param listaReduccionHoras Lista de las horas de reducción
	 * @param asignacionReduccion Mapa de reducciones
	 * @throws HorarioException Se lanzara si la reducción ya existe 
	 */
	public void asignarReduccion(String idProfesor, String idReduccion, List<ReduccionHoras> listaReduccionHoras,
			Map<String, List<ReduccionHoras>> asignacionReduccion) throws HorarioException
	{
		if (asignacionReduccion.containsKey(idProfesor))
		{
			List<ReduccionHoras> existingReduccionHoras = asignacionReduccion.get(idProfesor);
			boolean idReduccionExists = false;

			// Comprobamos si la id de reduccion existe
			int i = 0;
			while (i < existingReduccionHoras.size() && !idReduccionExists)
			{
				idReduccionExists = existingReduccionHoras.get(i).getIdReduccion().equalsIgnoreCase(idReduccion) ;
				i++;
			}
			
			if (idReduccionExists)
			{
				String error = "La reducción ya existe";
				
				// Log con el error
				log.error(error);
				
				throw new HorarioException(Constants.ERR_REDUCCION_EXIS, error);
			} 
			else
			{
				existingReduccionHoras.addAll(listaReduccionHoras);
			}
		}
		else
		{
			asignacionReduccion.put(idProfesor, listaReduccionHoras);
		}
	}
	
	/**
	 * Método para obtener si existe el mapa de reduccion
	 * 
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @param mapaReduccion Mapa de reducción
	 * @return Mapa de reduccion
	 * @throws HorarioException Se lanzará esta excepción si el mapa es nulo
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<ReduccionHoras>> obtenerMapaReduccion(HttpSession session) throws HorarioException
	{
		Map<String,List<ReduccionHoras>> mapaReduccion = (Map<String, List<ReduccionHoras>>) session.getAttribute(Constants.SESION_MAPA_REDUCCIONES);
		
		if (mapaReduccion == null)
		{
			String error = "El mapa reduccion no han sido cargado en sesion todavía";
			
			// Log con el error
			log.error(error);
			
			throw new HorarioException(Constants.ERR_MAP_NULL, error);
		}
		return mapaReduccion;
	}

	/**
	 * Método para obtener si existe el mapa asignaturas
	 * 
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @param mapaAsignatura mapa de asignaturas
	 * @return Mapa de asignaturas
	 * @throws HorarioException Se lanzará esta excepción si el mapa es nulo
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<Asignatura>> obtenerMapaAsignaturas(HttpSession session) throws HorarioException
	{
		Map<String,List<Asignatura>> mapaAsignatura = (Map<String, List<Asignatura>>) session.getAttribute(Constants.SESION_MAPA_ASIGNATURAS);
		
		if (mapaAsignatura == null)
		{
			String error = "El mapa asignaturas no han sido cargado en sesion todavía";
			
			// Log con el error
			log.error(error);
			
			throw new HorarioException(Constants.ERR_MAP_NULL, error);
		}
		return mapaAsignatura;
	}
	
	/**
	 * Método para inicializar la lista de nombres
	 * 
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @return Lista con nombres de alumnos
	 */
	@SuppressWarnings("unchecked")
	public List<String> inicializarListaApellidosNombreAlumnos(HttpSession session) 
	{
		List<String> listaNombres;
		if (session.getAttribute(Constants.SESION_LISTA_NOMBRES) != null)
		{
			listaNombres = (List<String>) session.getAttribute(Constants.SESION_LISTA_NOMBRES);
		}
		else
		{
			listaNombres = new ArrayList<String>();
		}
		return listaNombres;
	}

	/**
	 * Método para obtener si existe el mapa cursos
	 * 
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @param mapaCursos Mapa de cursos
	 * @return Mapa cursos
	 * @throws HorarioException Se lanzara si no se ha cargado el mapa de cursos
	 */
	public Map<String, Map<String, List<String>>> obtenerMapaCursos(HttpSession session,
			Map<String, Map<String, List<String>>> mapaCursos) throws HorarioException
	{
		if (mapaCursos == null)
		{
			String error = "El mapa cursos no han sido cargado en sesion todavía";
			
			// Log con el error
			log.error(error);
			
			throw new HorarioException(Constants.ERR_MAP_NULL, error);
		}
		return mapaCursos;
	}

	/**
	 * Método para obtener si el mapa bloques existe
	 * 
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @param mapaBloques Mapa de bloques
	 * @return mapa de los bloques
	 * @throws HorarioException Se lanzara si el mapa es nulo
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<String>> obtenerMapaBloques(HttpSession session) throws HorarioException
	{
		Map<String, List<String>> mapaBloques = (Map<String, List<String>>) session.getAttribute(Constants.SESION_MAPA_BLOQUES);
		
		if (mapaBloques == null)
		{
			String error = "El mapa bloques no han sido cargado en sesion todavía";
			
			// Log con el error
			log.error(error);
			
			throw new HorarioException(Constants.ERR_MAP_NULL, error);
		}
		return mapaBloques;
	}

	/**
	 * Método para incializar el mapa de cursos
	 * 
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @return Mapa de cursos
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Map<String, List<String>>> inicializarMapaCursos(HttpSession session) 
	{
		Map<String, Map<String, List<String>>> mapaCursos;
		if (session.getAttribute(Constants.SESION_MAPA_CURSOS) != null)
		{
			mapaCursos = (Map<String, Map<String, List<String>>>) session.getAttribute(Constants.SESION_MAPA_CURSOS);
		}
		else
		{
			mapaCursos = new HashMap<String, Map<String, List<String>>>();
		}
		
		return mapaCursos;
	}

	/**
	 * Método para obtener si la lista existe
	 * 
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @param listaNombres Lista con el nombre y apellidos de los alumnos 
	 * @return Lista de alumnos
	 * @throws HorarioException Se lanzara si la lista no tiene alumnos
	 */
	@SuppressWarnings("unchecked")
	public List<String> obtenerListaNombresExiste(HttpSession session) throws HorarioException
	{
		List<String> listaNombres = (List<String>) session.getAttribute(Constants.SESION_LISTA_NOMBRES);
		
		if (listaNombres == null)
		{
			String error = "La lista de nombres no han sido cargada en sesion todavía";
			
			// Log con el error
			log.error(error);
			
			throw new HorarioException(Constants.ERR_LIST_NULL_CODE, error);
		}
		return listaNombres;
	}

	/**
	 * Método para realizar la asignacion de un alumno
	 * 
	 * @param alumno Nombre y apellido del alumno
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @param cursoObject Objeto curso
	 * @param resultado String con el resultado
	 * @param clave Clave para el mapa
	 * @param listaCursos Lista de cursos
	 * @param listaNombres Lista de nombres
	 * @return Mapa de alumnos 
	 * @throws HorarioException Se lanzara si el alumno o curso no existe
	 */
	public void realizarAsignacionAlumno(String alumno, HttpSession session, Curso cursoObject,
			String clave, List<Curso> listaCursos, List<String> listaNombres) throws HorarioException
	{
		Map<String, List<String>> mapaAlumnos;
		if (listaNombres.contains(alumno))
		{
			if (listaCursos.contains(cursoObject))
			{
				List<String> listaAlumnos = new ArrayList<String>();
				
				mapaAlumnos = this.añadirAlumnoAlMapa(alumno, session, clave, listaAlumnos);
				log.info(mapaAlumnos);
			}
			else
			{
				String error = "El curso no existe";
				
				// Log con el error
				log.error(error);
				
				throw new HorarioException(Constants.ERR_CURSO_EXIS, error);
			}
		}
		else
		{
			String error = "El alumno no existe";
			
			// Log con el error
			log.error(error);
			
			throw new HorarioException(Constants.ERR_ALUM_EXIS, error);
		}
	}
	
	/**
	 * Método para añadir el alumno al mapa
	 * 
	 * @param alumno Nombre y apellidos del alumno
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @param clave Clave para obtener la lista del mapa
	 * @param listaAlumnos Lista de alumnos
	 * @return Mapa de alumnos
	 * @throws HorarioException Se lanzara si el alumno ya ha sido asignado a ese curso
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<String>> añadirAlumnoAlMapa(String alumno, HttpSession session, String clave,
			List<String> listaAlumnos) throws HorarioException
	{
		Map<String, List<String>> mapaAlumnos;
		// Si el mapa es nulo lo crea y añade el mapa alumnos si no lo obtiene
		if (session.getAttribute(Constants.SESION_MAPA_ALUMNOS) != null)
		{
			mapaAlumnos = (Map<String, List<String>>) session.getAttribute(Constants.SESION_MAPA_ALUMNOS);
			
			// Si el mapa alumnos contiene la clave obtenemos la lista si no agregamos el alumno a una lista y guardamos el mapa
			if (mapaAlumnos.containsKey(clave))
			{
				listaAlumnos = mapaAlumnos.get(clave);
				
				// Si la lista contiene el alumno da un error si no lo añade a la lista
				if (listaAlumnos.contains(alumno))
				{
					String error = "El alumno ya ha sido asignado a ese curso";
					
					// Log con el error
					log.error(error);
					
					throw new HorarioException(Constants.ERR_ALUM_ASIG, error);
				}
				else
				{
					listaAlumnos.add(alumno);
					mapaAlumnos.put(clave, listaAlumnos);
					session.setAttribute(Constants.SESION_MAPA_ALUMNOS, mapaAlumnos);
				}
			} 
			else
			{
				listaAlumnos.add(alumno);
				mapaAlumnos.put(clave, listaAlumnos);
				session.setAttribute(Constants.SESION_MAPA_ALUMNOS, mapaAlumnos);
			}
		} 
		else
		{
			mapaAlumnos = new HashMap<String, List<String>>();
			List<String> listaAlumnos2 = new ArrayList<String>();
			listaAlumnos2.add(alumno);
			mapaAlumnos.put(clave, listaAlumnos2);
			session.setAttribute(Constants.SESION_MAPA_ALUMNOS, mapaAlumnos);
		}
		return mapaAlumnos;
	}
	/**
	 * Método para obtener si el mapa asignaturas cursos existe
	 * 
	 * @param session Utilizado para guardas u obtener cosas en sesión 
	 * @param mapaAsignaturas Mapa de asignaturas
	 * @return  Mapa de asignaturas
	 * @throws HorarioException se lanzara si el mapa es nulo
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<String>> obtenerMapaAsignaturasCursos(HttpSession session) throws HorarioException 
	{
		Map<String, List<String>> mapaAsignaturas = (Map<String, List<String>>) session.getAttribute(Constants.SESION_MAPA_ASIGNATURA_CURSOS);
		
		if (mapaAsignaturas == null)
		{
			String error = "El mapa asignaturas no han sido cargado en sesion todavía";
			
			// Log con el error
			log.error(error);
			
			throw new HorarioException(Constants.ERR_MAP_NULL, error);
		}
		 
		return mapaAsignaturas;
	}
	/**
	 * Método para obtener si el mapa de alumnos existe
	 * 
	 * @param mapaAlumnos Mapa de alumnos
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @return  Mapa de alumnos
	 * @throws HorarioException Se lanzara si el mapa alumnos es nulo
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<String>> obtenerMapaAlumno(HttpSession session) throws HorarioException
	{
		Map<String, List<String>> mapaAlumnos = (Map<String, List<String>>) session.getAttribute(Constants.SESION_MAPA_ALUMNOS);
		
		if (mapaAlumnos == null)
		{
			String error = "El mapa alumnos no han sido cargado en sesion todavía";
			
			// Log con el error
			log.error(error);
			
			throw new HorarioException(Constants.ERR_MAP_NULL, error);
		}
		
		return mapaAlumnos;
	}
	/**
	 * Método para asignar una asignatura al mapa asignaturas
	 * 
	 * @param idProfesor Id del profesor
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @param datosAsignacion Lista de asignaturas
	 * @param asignaturaObject Objeto asignatura
	 * @return La asignación
	 * @throws HorarioException Se lanzara si ocurre un error en la asignación
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<Asignatura>> asignacionMapaAsignaturas(String idProfesor, HttpSession session,
			List<Asignatura> datosAsignacion, Asignatura asignaturaObject) throws HorarioException
	{
		Map<String, List<Asignatura>> asignacion;
		if (session.getAttribute(Constants.SESION_MAPA_ASIGNATURAS) == null) 
		{
		    asignacion = new TreeMap<String, List<Asignatura>>();
		    asignacion.put(idProfesor, datosAsignacion);
		    session.setAttribute(Constants.SESION_MAPA_ASIGNATURAS, asignacion);
		}
		else 
		{
		    asignacion = (Map<String, List<Asignatura>>) session.getAttribute(Constants.SESION_MAPA_ASIGNATURAS);
		    // Comprobamos si existe el idProfesor en el mapa
		    if (asignacion.containsKey(idProfesor)) 
		    {
		        List<Asignatura> existingAsignaturas = asignacion.get(idProfesor);
		        if(existingAsignaturas.contains(asignaturaObject)) 
		        {
		        	String errorString = "Esta asignación ya existe";
					
		        	// Log con el error
					log.error(errorString);
					
					throw new HorarioException(Constants.ERR_ASIGNACION_ASIGNATURA, errorString);
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
		    session.setAttribute(Constants.SESION_MAPA_ASIGNATURAS, asignacion);
		}
		return asignacion;
	}
	/**
	 * Método para inicialiar el mapa de bloques si esta vacio 
	 *  
	 * @param mapaBloques Mapa de bloques
	 * @return Mapa de bloques
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<String>> inicializarMapaBloques(HttpSession session) 
	{
		Map<String, List<String>> mapaBloques = (Map<String, List<String>>) session.getAttribute(Constants.SESION_MAPA_BLOQUES);
		
		if (mapaBloques == null)
		{
			mapaBloques = new HashMap<>();
		}
		
		return mapaBloques;
	}
	
	/**
	 * Método para validar la existencia del alumno
	 * 
	 * @param alumno Alumno con apellidos y nombre
	 * @param listaAlumnos lista de alumnos 
	 * @throws HorarioException Se lanzarasi el alumno no existe
	 */
	public void validarExistenciaAlumno(String alumno, List<String> listaAlumnos) throws HorarioException 
	{
		boolean encontrado = false;
		int i = 0;
		while (i < listaAlumnos.size() && !encontrado)
		{
			if (listaAlumnos.get(i).contains(alumno))
			{
				listaAlumnos.remove(i);
				encontrado = true;
			}
			i++;
		}
		if(!encontrado) 
		{
			String error = "El alumno no existe";
			
			// Log con el error
			log.error(error);
			
			throw new HorarioException(Constants.ERR_ALUM_EXIS, error);
		}
	}
	
	/**
	 * Método para asignar asignaturas al mapa bloques
	 * 
	 * @param curso Número de curso
	 * @param etapa Etapa del curso
	 * @param nombreAsignatura Nombre de la asignatura
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @param mapaBloques Mapa de bloques
	 * @param encontrado boolean para saber si se ha encontrado o no
	 * @throws HorarioException Se lanzara si el curso o etapa no son correctos o si la asignatura ya esta registrada
	 */
	public void asignarAsignaturasMapaBloques(Integer curso, String etapa, String nombreAsignatura, HttpSession session,
			Map<String, List<String>> mapaBloques, boolean encontrado) throws HorarioException
	{
		if (encontrado)
		{
			String clave = curso + etapa.toUpperCase();
			
			// Obtenemos la lista en caso de que la clave exista u obtenemos un nueva lista
			// de asignaturas
			List<String> listaNombreAsignatura = mapaBloques.getOrDefault(clave, new ArrayList<String>());

			if (listaNombreAsignatura.contains(nombreAsignatura))
			{
				String error = "Esa asignatura ya esta registrada";
				
				// Log con el error
				log.error(error);
				
				throw new HorarioException(Constants.ERR_ASIG_REGIS, error);
			}
			else
			{
				listaNombreAsignatura.add(nombreAsignatura);
				mapaBloques.put(clave, listaNombreAsignatura);
				session.setAttribute(Constants.SESION_MAPA_BLOQUES, mapaBloques);
			}
		}
		else
		{
			String error = "alguno de los parametros mandados no existe";
			
			// Log con el error
			log.error(error);
			
			throw new HorarioException(Constants.ERR_VALIDATE_CURSO_ETAPA, error);
		}
	}
}
