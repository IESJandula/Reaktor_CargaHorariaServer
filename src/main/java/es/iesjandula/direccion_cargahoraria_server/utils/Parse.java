package es.iesjandula.direccion_cargahoraria_server.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

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
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

/**
 * clase Parse
 */
@Log4j2
public class Parse
{
	/**
	 * Método para parsear departamentos
	 * 
	 * @param csvFile Fichero csv 
	 * @return La lista de departamentos
	 * @throws HorarioException Excepción en la línea en la que ocurre el error
	 */
	public List<Departamento> parseDepartamentos(MultipartFile csvFile) throws HorarioException 
	{
		Scanner scanner = null;
		try
		{
			List<Departamento> listaDepartamentos = new ArrayList<>();
			
			Validations validations = new Validations() ;
			
			String contenido = validations.obtenerContenidoFichero(Constants.NOMBRE_METH_PARSE_DEPARTAMENTOS, csvFile) ;
			
			scanner = new Scanner(contenido);
			// Saltamos la cabecera
			scanner.nextLine();
			while (scanner.hasNext())
			{
				String linea = scanner.nextLine() ;
				
				// Separamos la linea por ,
				String[] lineaArray = linea.split(",");
				
				if (lineaArray.length != 1)
				{
					String errorString = Constants.ERR_LECTURA_FICHEROS_CSV_MSG + Constants.NOMBRE_METH_PARSE_DEPARTAMENTOS + ". Concretamente aquí: " + linea ;
					
					log.error(errorString);
					throw new HorarioException(Constants.ERR_LECTURA_FICHEROS_CSV_CODE, errorString);
				}
				
				listaDepartamentos.add(new Departamento(lineaArray[0]));
			}
			// Log para mostrar la lista de departamentos
			log.info(listaDepartamentos);
			
			return listaDepartamentos;
		}
		finally
		{
			if (scanner != null)
			{
				scanner.close();
			}
		}
	}

	/**
	 * métodoara parsear cursos
	 * 
	 * @param csvFile Fichero csv
	 * @return La lista de cursos
	 * @throws HorarioException Excepción en la línea en la que ocurre el error
	 */
	public List<Curso> parseCursos(MultipartFile csvFile) throws HorarioException 
	{
		Scanner scanner = null;
		try
		{
			List<Curso> listaCursos = new ArrayList<>();
			Validations validations = new Validations() ;
			
			String contenido = validations.obtenerContenidoFichero("parseCursos", csvFile) ;
			scanner = new Scanner(contenido);
			// Saltamos la cabecera
			scanner.nextLine();
			while (scanner.hasNext())
			{
				String linea = scanner.nextLine() ;
				
				// Separamos la linea por ,
				String[] lineaArray = linea.split(",");
				if (lineaArray.length != 3)
				{
					String errorString = Constants.ERR_LECTURA_FICHEROS_CSV_MSG + Constants.NOMBRE_METH_PARSE_CURSOS + ". Concretamente aquí: " + linea ;
					
					log.error(errorString);
					throw new HorarioException(Constants.ERR_LECTURA_FICHEROS_CSV_CODE, errorString);
				}
				Curso curso = new Curso(Integer.parseInt(lineaArray[0]), lineaArray[1], lineaArray[2]);
				listaCursos.add(curso);
			}
			return listaCursos;
		}
		finally
		{
			if (scanner != null)
			{
				scanner.close();
			}
		}
	}

	/**
	 * 
	 * @param csvFile Fichero csv 
	 * @param listaDepartamentos Lista de departamentos para obtener si existe
	 * @return La lista de profesores
	 * @throws HorarioException Excepción en la línea en la que ocurre el error
	 */
	public List<Profesor> parseProfesores(MultipartFile csvFile, List<Departamento> listaDepartamentos)
			throws HorarioException
	{
		Scanner scanner = null;
		try
		{
			List<Profesor> listaProfesores = new ArrayList<>();
			Validations validations = new Validations() ;
			
			String contenido = validations.obtenerContenidoFichero("parseCursos", csvFile) ;

			scanner = new Scanner(contenido);
			// Saltamos la cabecera
			scanner.nextLine();

			while (scanner.hasNext())
			{
				String linea = scanner.nextLine() ;
				
				// Separamos la linea por ,
				String[] lineaArray = linea.split(",");
				if (lineaArray.length != 3)
				{
					String errorString = Constants.ERR_LECTURA_FICHEROS_CSV_MSG + Constants.NOMBRE_METH_PARSE_PROFESORES + ". Concretamente aquí: " + linea ;
					
					log.error(errorString);
					throw new HorarioException(Constants.ERR_LECTURA_FICHEROS_CSV_CODE, errorString);
				}
				Departamento departamento = new Departamento(lineaArray[2]);
				// Método para obtener el departamento
				validations.obtenerDepartamento(listaDepartamentos, departamento);
				// Creamos el objeto profesor y lo añadimos a la lista
				Profesor profesor = new Profesor(lineaArray[0], lineaArray[1], lineaArray[2]);
				listaProfesores.add(profesor);
			}
			
			return listaProfesores;
		}
		finally
		{
			if (scanner != null)
			{
				scanner.close();
			}
		}
	}

	/**
	 * método para parsear asignaturas
	 * 
	 * @param csvFile Fichero csv
	 * @param listaCursos Lista de cursos
	 * @param listaDepartamentos Lista de departamentos
	 * @return La lista de asignaturas
	 * @throws HorarioException Excepción en la línea en la que ocurre el error
	 */
	public List<Asignatura> parseAsignaturas(MultipartFile csvFile, List<Curso> listaCursos,
			List<Departamento> listaDepartamentos) throws HorarioException
	{
		Scanner scanner = null;
		try
		{
			List<Asignatura> listaAsignaturas = new ArrayList<>();
			
			Validations validations = new Validations() ;
			// Obtenemos el contenido del fichero
			String contenido = validations.obtenerContenidoFichero("parseCursos", csvFile) ;
			scanner = new Scanner(contenido);
			Asignatura asignatura = null;
			// Saltamos la cabecera
			scanner.nextLine();
			while (scanner.hasNext())
			{
				String linea = scanner.nextLine() ;
				
				// Separamos la linea por ,
				String[] lineaArray = linea.split(",");

				Curso curso = new Curso(Integer.parseInt(lineaArray[1]), lineaArray[2], lineaArray[3]);
				validations.obtenerCurso(listaCursos, curso);
				
				boolean esAsignatura = lineaArray.length == 6 ;
				boolean esTutoria = lineaArray.length == 5 ;
				
				if (!esAsignatura && !esTutoria)
				{
					String errorString = Constants.ERR_LECTURA_FICHEROS_CSV_MSG + Constants.NOMBRE_METH_PARSE_ASIGNATURAS + ". Concretamente aquí: " + linea ;
					
					log.error(errorString);
					throw new HorarioException(Constants.ERR_LECTURA_FICHEROS_CSV_CODE, errorString);
				}
				else if (esAsignatura)
				{
					Departamento departamento = new Departamento(lineaArray[5]);
					// Obtenemos el departamento
					validations.obtenerDepartamento(listaDepartamentos, departamento);
					// Creamos la asignatura
					asignatura = new Asignatura(lineaArray[0], Integer.parseInt(lineaArray[1]), lineaArray[2], lineaArray[3], Integer.valueOf(lineaArray[4]), lineaArray[5]);
				}
				else
				{
					// Creamos la asignatura
					asignatura = new Asignatura(lineaArray[0], Integer.parseInt(lineaArray[1]), lineaArray[2], lineaArray[3], Integer.valueOf(lineaArray[4]), null);
				}
				// Log para mostrar la lista de asignaturas
				log.info(listaAsignaturas);
				listaAsignaturas.add(asignatura);
			}

			return listaAsignaturas;
		}
		finally
		{
			if (scanner != null)
			{
				scanner.close();
			}
		}
	}

	/**
	 * método para parsear reducciones
	 * 
	 * @param csvFile Fichero csv
	 * @param listaCursos Lista de cursos
	 * @return Lista de reducciones
	 * @throws HorarioException Controla que el fichero se pueda parsear correctamente
	 */
	public List<Reduccion> parseReducciones(MultipartFile csvFile, List<Curso> listaCursos) throws HorarioException 
	{
		Scanner scanner = null;
		try
		{
			List<Reduccion> listaReducciones = new ArrayList<>();
			Reduccion reduccion;
			Validations validations = new Validations() ;
			
			String contenido = validations.obtenerContenidoFichero("parseReducciones", csvFile) ;
			scanner = new Scanner(contenido);
			// Saltamos la cabecera
			scanner.nextLine();
			while (scanner.hasNext())
			{
				String linea = scanner.nextLine() ;
				
				// Separamos la linea por ,
				String[] lineaArray = linea.split(",");

				boolean esReduccion = lineaArray.length == 3;
				
				if(!esReduccion)
				{
					String errorString = Constants.ERR_LECTURA_FICHEROS_CSV_MSG + Constants.NOMBRE_METH_PARSE_REDUCCIONES + ". Concretamente aquí: " + linea ;
					
					log.error(errorString);
					throw new HorarioException(Constants.ERR_LECTURA_FICHEROS_CSV_CODE, errorString);
				}
				else 
				{
					reduccion = new Reduccion(lineaArray[0], lineaArray[1], Integer.valueOf(lineaArray[2]), null, null, null);
					listaReducciones.add(reduccion);
				}
			}
			return listaReducciones;
		}

		finally
		{
			if (scanner != null)
			{
				scanner.close();
			}
		}
	}


	/**
	 * método para realizar una reduccion
	 * 
	 * @param idProfesor Id del profesor
	 * @param idReduccion Id de la reduccion
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @param listaReducciones Lista de reducciones
	 * @param listaReduccionHoras Lista de las horas de reduccion
	 * @return Un mapa de reducciones
	 * @throws HorarioException 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<ReduccionHoras>> realizarReduccion(String idProfesor, String idReduccion,
			HttpSession session, List<Reduccion> listaReducciones, List<ReduccionHoras> listaReduccionHoras)
			throws HorarioException
	{
		ReduccionHoras reduccionHoras = new ReduccionHoras();
		boolean reduccionEncontrada = false;
		
		this.obtenerReduccionId(idReduccion, listaReducciones, listaReduccionHoras, reduccionHoras, reduccionEncontrada);
		
		Map<String, List<ReduccionHoras>> asignacionReduccion = null;
		if (session.getAttribute("mapaReduccion") == null)
		{
			asignacionReduccion = new TreeMap<String, List<ReduccionHoras>>();
			asignacionReduccion.put(idProfesor, listaReduccionHoras);
			session.setAttribute("mapaReduccion", asignacionReduccion);
		}
		else
		{
			asignacionReduccion = (Map<String, List<ReduccionHoras>>) session.getAttribute("mapaReduccion");
			this.asignarReduccion(idProfesor, idReduccion, listaReduccionHoras, asignacionReduccion);
			
			session.setAttribute("mapaReduccion", asignacionReduccion);
		}
		return asignacionReduccion;
	}
	/**
	 * Metodo para asignar una reducción
	 * 
	 * @param idProfesor Id del profesor
	 * @param idReduccion Id de la reducción
	 * @param listaReduccionHoras Lista de las horas de reducción
	 * @param asignacionReduccion Mapa de reducciones
	 * @throws HorarioException 
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
				throw new HorarioException(1, error);
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
	 * @param asignaturaEncontrada Booleano para obtener que existe la asignatura
	 * @throws HorarioException
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
			
			log.error(errorString);
			throw new HorarioException(2, errorString);
		}
		

		asignaturaObject.setCurso(curso);
		asignaturaObject.setEtapa(etapa);
		asignaturaObject.setGrupo(grupo);
		datosAsignacion.add(asignaturaObject);
		
	}

	/**
	 * Método para obtener el curso
	 * 
	 * @param listaCursos Lista de cursos
	 * @param cursoExiste Booleano para obtener si el curso existe
	 * @param cursoAsignacion Objeto curso
	 * @throws HorarioException 
	 */

	/**
	 * método para obtener el id del profesor
	 * 
	 * @param idProfesor Id del profesor
	 * @param listaProfesores Lista de profesores
	 * @throws HorarioException
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
			throw new HorarioException(13, error);
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
		List<Departamento> listaDepartamentos = (List<Departamento>) session.getAttribute("listaDepartamentos") ;

		if (listaDepartamentos == null)
		{
			String error = "Los departamentos no han sido cargados en sesion todavía";
			throw new HorarioException(1, error);
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
	public List<Curso> obtenerListaCursos(HttpSession session, List<Curso> listaCursos) throws HorarioException
	{
		
		if (listaCursos == null)
		{
			String error = "Los cursos no han sido cargados en sesion todavía";
			throw new HorarioException(1, error);
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
	public List<Profesor> obtenerListaProfesores(HttpSession session, List<Profesor> listaProfesores)
			throws HorarioException
	{
		if (listaProfesores == null)
		{
			String error = "Los profesores no han sido cargados en sesion todavía";
			throw new HorarioException(1, error);
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
	public List<Asignatura> obtenerListaAsignaturas(HttpSession session, List<Asignatura> listaAsignaturas)
			throws HorarioException
	{
		if (listaAsignaturas == null)
		{
			String error = "Las asignaturas no han sido cargadas en sesion todavía";
			throw new HorarioException(1, error);
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
	public List<Reduccion> obtenerListaReducciones(HttpSession session, List<Reduccion> listaReducciones)
			throws HorarioException
	{
		if (listaReducciones == null)
		{
			String error = "Las reducciones no han sido cargadas en sesion todavía";
			throw new HorarioException(1, error);
		}
		return listaReducciones;
	}

	/**
	 * Método para obtener si el departamento existe
	 * 
	 * @param listaDepartamentos Lista de departamentos
	 * @param departamento Objeto departamento
	 * @throws HorarioException se lanzará esta excepción si no existe el departamento
	 */
	public void obtenerDepartamentoExiste(List<Departamento> listaDepartamentos, Departamento departamento)
			throws HorarioException
	{
		if (!listaDepartamentos.contains(departamento))
		{
			String error = "Departamento no encontrado";
			log.info(error);
			throw new HorarioException(13, error);
		}
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
			log.info(error);
			throw new HorarioException(13, error);
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
	public void obtenerNombreAsignaturaExiste(String nombreAsignatura, List<Asignatura> listaAsignaturas)
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
			throw new HorarioException(13, error);
		}
	}

	/**
	 * Método para obtener las horas de un profesor
	 * 
	 * @param idProfesor Id del profesor
	 * @param mapaReduccion Mapa de reducciones
	 * @param mapaAsignatura Mapa de asignaturas
	 * @return Objeto resumen de profesor
	 */
	public ResumenProfesor obtencionHorasProfesor(String idProfesor, Map<String, List<ReduccionHoras>> mapaReduccion,
			Map<String, List<Asignatura>> mapaAsignatura)
	{
		int totalHoras = 0;
		List<Asignatura> listaAsignaturaProfesor = new ArrayList<Asignatura>();
		List<ReduccionHoras> listaReduccionHoras = new ArrayList<ReduccionHoras>();
		if (mapaAsignatura.containsKey(idProfesor))
		{
			List<Asignatura> listaAsignaturas = mapaAsignatura.get(idProfesor);
			for (Asignatura asignatura : listaAsignaturas)
			{
				Asignatura asignatura2 = asignatura;
				listaAsignaturaProfesor.add(asignatura2);
				totalHoras += asignatura.getNumeroHorasSemanales();
			}
		}
		if (mapaReduccion.containsKey(idProfesor))
		{
			List<ReduccionHoras> listaReducciones = mapaReduccion.get(idProfesor);

			for (ReduccionHoras reduccionHoras : listaReducciones)
			{
				ReduccionHoras reduccionHoras2 = reduccionHoras;
				listaReduccionHoras.add(reduccionHoras2);
				totalHoras += reduccionHoras.getNumHoras();
			}
		}

		ResumenProfesor resumen = new ResumenProfesor(listaAsignaturaProfesor, listaReduccionHoras, totalHoras);
		return resumen;
	}

	/**
	 * Método para obtener si existe el mapa de reduccion
	 * 
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @param mapaReduccion Mapa de reducción
	 * @return Mapa de reduccion
	 * @throws HorarioException Se lanzará esta excepción si el mapa es nulo
	 */
	public Map<String, List<ReduccionHoras>> obtenerMapaReduccion(HttpSession session,
			Map<String, List<ReduccionHoras>> mapaReduccion) throws HorarioException
	{
		if (mapaReduccion == null)
		{
			String error = "El mapa reduccion no han sido cargado en sesion todavía";
			throw new HorarioException(1, error);
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
	public Map<String, List<Asignatura>> obtenerMapaAsignaturas(HttpSession session,
			Map<String, List<Asignatura>> mapaAsignatura) throws HorarioException
	{
		if (mapaAsignatura == null)
		{
			String error = "El mapa asignaturas no han sido cargado en sesion todavía";
			throw new HorarioException(1, error);
		}
		return mapaAsignatura;
	}

	/**
	 * Método para parsear el fichero matriculas curso
	 * 
	 * @param csvFile Fichero csv
	 * @param curso Número de curso
	 * @param etapa Etapa del curso
	 * @param mapaAsignaturas Mapa de asignaturas
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @return La clave del mapa
	 * @throws IOException Se lanzará esta excepción si hay un error con el fichero
	 * @throws HorarioException 
	 */
	@SuppressWarnings("unchecked")
	public String parseCursosMap(MultipartFile csvFile, Integer curso, String etapa,
	        HttpSession session) throws IOException, HorarioException 
	{
	    Scanner scanner = null;
	    try 
	    {
	        boolean encontrado = false;
	        String contenido = new String(csvFile.getBytes());
	        scanner = new Scanner(contenido);
	        String clave = curso + etapa.toUpperCase();
	        String[] linea = scanner.nextLine().split(",");
	        List<Asignatura> listaAsignatura = (List<Asignatura>) session.getAttribute("listaAsignaturas");
	        this.obtenerListaAsignaturas(session, listaAsignatura);
	        List<String> listaNombres = inicializarListaNombres(session);
	        this.obtenerListaAsignaturas(session, listaAsignatura);
	        List<Curso> listaCursos = (List<Curso>) session.getAttribute("listaCursos");
	        this.obtenerListaCursos(session, listaCursos);
	        int i = 0;
	        while (i < listaCursos.size() && !encontrado) 
	        {
	            if (listaCursos.get(i).getCurso() == curso && listaCursos.get(i).getEtapa().equalsIgnoreCase(etapa)) 
	            {
	                encontrado = true;
	            }
	            i++;
	        }
	        if (encontrado) 
	        {
	            String asignatura1 = "";
	            String asignatura2 = "";
	            String asignatura3 = "";
	            for (Asignatura asignatura : listaAsignatura) 
	            {
	                if (linea[1].equalsIgnoreCase(asignatura.getNombreAsignatura())) 
	                {
	                    asignatura1 = linea[1];
	                }
	                if (linea[2].equalsIgnoreCase(asignatura.getNombreAsignatura())) 
	                {
	                    asignatura2 = linea[2];
	                }
	                if (linea[3].equalsIgnoreCase(asignatura.getNombreAsignatura())) 
	                {
	                    asignatura3 = linea[3];
	                }
	            }

	            Map<String, List<String>> mapaAsignaturas = (Map<String, List<String>>) session.getAttribute("mapaAsignaturasCursos");
	            if(mapaAsignaturas==null) 
	            {
	            	mapaAsignaturas = new HashMap<String, List<String>>();
	            }
	            this.parseMatriculaCursos(mapaAsignaturas, scanner, listaNombres, asignatura1, asignatura2, asignatura3,session);
	            
                session.setAttribute("listaNombres", listaNombres);

                Map<String, Map<String, List<String>>> mapaCursos = (Map<String, Map<String, List<String>>>) session.getAttribute("mapaCursos");
                if (mapaCursos == null) 
                {
                    mapaCursos = new HashMap<>();
                }
                mapaCursos.put(clave, mapaAsignaturas);
                session.setAttribute("mapaCursos", mapaCursos);

                log.info("mapaCursos: " + mapaCursos);
	            
	            return clave;
	        }
	        else 
	        {
	            String error = "El curso o etapa no existe";
	            throw new HorarioException(1, error);
	        }
	    }
	    catch (IOException ioException) 
	    {
	        log.error(Constants.ERR_LECTURA_FICHEROS_CSV_MSG + "parseCursosMap", ioException);
	        throw new HorarioException(Constants.ERR_LECTURA_FICHEROS_CSV_CODE,
	                Constants.ERR_LECTURA_FICHEROS_CSV_MSG + "parseCursosMap", ioException);
	    }
	    finally 
	    {
	        if (scanner != null) 
	        {
	            scanner.close();
	       
	        }
	    }
	}

	/**
	 * Método para parsear el fichero matricula Cursos
	 * 
	 * @param mapaAsignaturas Mapa de asignaturas
	 * @param scanner Escaner para leer
	 * @param listaNombres Lista de nombres
	 * @param asignatura1 asignatura
	 * @param asignatura2 asignatura
	 * @param asignatura3 asignatura
	 */
	public void parseMatriculaCursos(Map<String, List<String>> mapaAsignaturas, Scanner scanner,
			List<String> listaNombres, String asignatura1, String asignatura2, String asignatura3,HttpSession session)
	{
		String[] linea;
		while (scanner.hasNext())
		{
			List<String> listaAsignaturas = new ArrayList<>();
			// Separamos la línea por comas
			linea = scanner.nextLine().split(",");

			String apellidos = linea[0];
			String nombre = linea[1];
			String nombreCompleto = apellidos + "," + nombre;
			listaNombres.add(nombreCompleto);

			String uno = linea[2];
			String dos = linea[3];
			String tres = linea[4];

			if (uno.equalsIgnoreCase("MATR") && !asignatura1.isEmpty())
			{
				listaAsignaturas.add(asignatura1);
			}
			if (dos.equalsIgnoreCase("MATR") && !asignatura2.isEmpty())
			{
				listaAsignaturas.add(asignatura2);
			}
			if (tres.equalsIgnoreCase("MATR") && !asignatura3.isEmpty())
			{
				listaAsignaturas.add(asignatura3);
			}

			mapaAsignaturas.put(nombreCompleto, listaAsignaturas);
		}
		session.setAttribute("mapaAsignaturasCursos", mapaAsignaturas);
	}

	/**
	 * Método para inicializar la lista de nombres
	 * 
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @return Lista con nombres de alumnos
	 */
	@SuppressWarnings("unchecked")
	public List<String> inicializarListaNombres(HttpSession session) 
	{
		List<String> listaNombres;
		if (session.getAttribute("listaNombres") != null)
		{
			listaNombres = (List<String>) session.getAttribute("listaNombres");
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
	 * @throws HorarioException
	 */
	public Map<String, Map<String, List<String>>> obtenerMapaCursosExiste(HttpSession session,
			Map<String, Map<String, List<String>>> mapaCursos) throws HorarioException
	{
		if (mapaCursos == null)
		{
			String error = "El mapa cursos no han sido cargado en sesion todavía";
			throw new HorarioException(1, error);
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
	public Map<String, List<String>> obtenerMapaBloquesExiste(HttpSession session,
			Map<String, List<String>> mapaBloques) throws HorarioException
	{
		if (mapaBloques == null)
		{
			String error = "El mapa bloques no han sido cargado en sesion todavía";
			throw new HorarioException(1, error);
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
		if (session.getAttribute("mapaAsignaturasCursos") != null)
		{
			mapaCursos = (Map<String, Map<String, List<String>>>) session.getAttribute("mapaAsignaturasCursos");
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
	 * @throws HorarioException
	 */
	public void obtenerListaNombresExiste(HttpSession session, List<String> listaNombres) throws HorarioException
	{
		if (listaNombres == null)
		{
			String error = "La lista de nombres no han sido cargada en sesion todavía";
			throw new HorarioException(1, error);
		}
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
	 * @return 
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public String realizarAsignacionAlumno(String alumno, HttpSession session, Curso cursoObject, String resultado,
			String clave, List<Curso> listaCursos, List<String> listaNombres) throws HorarioException
	{
		Map<String, List<String>> mapaAlumnos;
		if (listaNombres.contains(alumno))
		{
			if (listaCursos.contains(cursoObject))
			{
				List<String> listaAlumnos = new ArrayList<String>();
				if (session.getAttribute("mapaAlumnos") != null)
				{
					mapaAlumnos = (Map<String, List<String>>) session.getAttribute("mapaAlumnos");
					if (mapaAlumnos.containsKey(clave))
					{
						listaAlumnos = mapaAlumnos.get(clave);
						if (listaAlumnos.contains(alumno))
						{
							String error = "El alumno ya ha sido asignado a ese curso";
							throw new HorarioException(1, error);
						}
						else
						{
							listaAlumnos.add(alumno);
							mapaAlumnos.put(clave, listaAlumnos);
							session.setAttribute("mapaAlumnos", mapaAlumnos);
						}
					} 
					else
					{
						listaAlumnos.add(alumno);
						mapaAlumnos.put(clave, listaAlumnos);
						session.setAttribute("mapaAlumnos", mapaAlumnos);
					}
				} 
				else
				{
					mapaAlumnos = new HashMap<String, List<String>>();
					List<String> listaAlumnos2 = new ArrayList<String>();
					listaAlumnos2.add(alumno);
					mapaAlumnos.put(clave, listaAlumnos2);
					session.setAttribute("mapaAlumnos", mapaAlumnos);
				}
				log.info(mapaAlumnos);
			}
			else
			{
				String error = "El curso no existe";
				throw new HorarioException(1, error);
			}
		}
		else
		{
			String error = "El alumno no existe";
			throw new HorarioException(1, error);
		}
		return resultado;
	}
	/**
	 * Método para obtener si el mapa asignaturas cursos existe
	 * 
	 * @param session Utilizado para guardas u obtener cosas en sesión 
	 * @param mapaAsignaturas Mapa de asignaturas
	 * @throws HorarioException se lanzara si el mapa es nulo
	 */
	public void obtenerMapaAsignaturasCursosExiste(HttpSession session,Map<String, List<String>> mapaAsignaturas) throws HorarioException 
	{
		if (mapaAsignaturas == null)
		{
			String error = "El mapa asignaturas no han sido cargado en sesion todavía";
			throw new HorarioException(1, error);
		}
	}
	/**
	 * metodo para obtener si el mapa de alumnos existe
	 * 
	 * @param mapaAlumnos Mapa de alumnos
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @throws HorarioException Se lanzara si el mapa alumnos es nulo
	 */
	public void obtenerMapaAlumnoExiste(Map<String, List<String>> mapaAlumnos,HttpSession session) throws HorarioException
	{
		if (mapaAlumnos == null)
		{
			String error = "El mapa alumnos no han sido cargado en sesion todavía";
			throw new HorarioException(1, error);
		}
	}
}
