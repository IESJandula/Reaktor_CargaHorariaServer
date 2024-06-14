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
	 * metodo para parsear departamentos
	 * 
	 * @param csvFile fichero csv 
	 * @return
	 * @throws HorarioException
	 */
	public List<Departamento> parseDepartamentos(MultipartFile csvFile) throws HorarioException 
	{
		Scanner scanner = null;
		try
		{
			List<Departamento> listaDepartamentos = new ArrayList<>();
			String contenido = new String(csvFile.getBytes());
			
			this.comprobarContenidoFichero(contenido);
			
			scanner = new Scanner(contenido);
			// saltamos la cabecera
			scanner.nextLine();
			while (scanner.hasNext())
			{
				// separamos la linea por ,
				String[] linea = scanner.nextLine().split(",");
				
				if (linea.length != 1)
				{
					throw new HorarioException(Constants.ERR_LECTURA_FICHEROS_CSV_CODE, Constants.ERR_LECTURA_FICHEROS_CSV_MSG + "parseDepartamentos");
				}
				Departamento departamento = new Departamento(linea[0]);
				listaDepartamentos.add(departamento);
			}
			log.info(listaDepartamentos);
			return listaDepartamentos;
		}
		catch (IOException ioException)
		{
			log.error(Constants.ERR_LECTURA_FICHEROS_CSV_MSG + "parseDepartamentos", ioException);
			
			throw new HorarioException(Constants.ERR_LECTURA_FICHEROS_CSV_CODE, 
									   Constants.ERR_LECTURA_FICHEROS_CSV_MSG + "parseDepartamentos", 
									   ioException);
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
	 * metodo para comprobar si el fichero esta vacio
	 * @param contenido contenido del ficherp
	 * @throws HorarioException
	 */
	private void comprobarContenidoFichero(String contenido) throws HorarioException
	{
		if (contenido == null || contenido.isEmpty())
		{
			throw new HorarioException(Constants.ERR_CONTENIDO_FICHEROS_CSV_CODE, Constants.ERR_CONTENIDO_FICHEROS_CSV_MSG);
		}
	}

	/**
	 * endpoint para parsear cursos
	 * 
	 * @param csvFile fichero csv
	 * @return
	 * @throws HorarioException
	 */
	public List<Curso> parseCursos(MultipartFile csvFile) throws HorarioException 
	{
		Scanner scanner = null;
		try
		{
			List<Curso> listaCursos = new ArrayList<>();
			String contenido = new String(csvFile.getBytes());
			this.comprobarContenidoFichero(contenido);
			scanner = new Scanner(contenido);
			// saltamos la cabecera
			scanner.nextLine();
			while (scanner.hasNext())
			{
				// separamos la linea por ,
				String[] linea = scanner.nextLine().split(",");
				if (linea.length != 3)
				{
					throw new HorarioException(Constants.ERR_LECTURA_FICHEROS_CSV_CODE, Constants.ERR_LECTURA_FICHEROS_CSV_MSG + "parseCursos");
				}
				Curso curso = new Curso(Integer.parseInt(linea[0]), linea[1], linea[2]);
				listaCursos.add(curso);
			}
			return listaCursos;
		}
		catch (IOException ioException)
		{
			log.error(Constants.ERR_LECTURA_FICHEROS_CSV_MSG + "parseCursos", ioException);
			
			throw new HorarioException(Constants.ERR_LECTURA_FICHEROS_CSV_CODE, 
									   Constants.ERR_LECTURA_FICHEROS_CSV_MSG + "parseCursos", 
									   ioException);
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
	 * @param csvFile fichero csv 
	 * @param listaDepartamentos lista de departamentos para comprobar si existe
	 * @return 
	 * @throws HorarioException
	 */
	public List<Profesor> parseProfesores(MultipartFile csvFile, List<Departamento> listaDepartamentos)
			throws HorarioException
	{
		Scanner scanner = null;
		try
		{
			List<Profesor> listaProfesores = new ArrayList<>();
			String contenido = new String(csvFile.getBytes());
			this.comprobarContenidoFichero(contenido);

			scanner = new Scanner(contenido);
			// saltamos la cabecera
			scanner.nextLine();

			while (scanner.hasNext())
			{
				// separamos la linea por ,
				String[] linea = scanner.nextLine().split(",");
				if (linea.length != 3)
				{
					throw new HorarioException(Constants.ERR_LECTURA_FICHEROS_CSV_CODE, Constants.ERR_LECTURA_FICHEROS_CSV_MSG + "parseProfesores");
				}
				Departamento departamento = new Departamento(linea[2]);
				// comprobamos que el departamento existe
				if (!listaDepartamentos.contains(departamento))
				{
					String error = "Departamento no encontrado";
					log.error(error);
					throw new HorarioException(12, error);
				}

				Profesor profesor = new Profesor(linea[0], linea[1], linea[2]);
				listaProfesores.add(profesor);
			}
			return listaProfesores;
		}
		catch (IOException ioException)
		{
			log.error(Constants.ERR_LECTURA_FICHEROS_CSV_MSG + "parseProfesores", ioException);
			
			throw new HorarioException(Constants.ERR_LECTURA_FICHEROS_CSV_CODE, 
									   Constants.ERR_LECTURA_FICHEROS_CSV_MSG + "parseProfesores", 
									   ioException);
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
	 * metodo para parsear asignaturas
	 * 
	 * @param csvFile fichero csv
	 * @param listaCursos
	 * @param listaDepartamentos
	 * @return
	 * @throws HorarioException
	 */
	public List<Asignatura> parseAsignaturas(MultipartFile csvFile, List<Curso> listaCursos,
			List<Departamento> listaDepartamentos) throws HorarioException
	{
		Scanner scanner = null;
		try
		{
			List<Asignatura> listaAsignaturas = new ArrayList<>();
			String contenido = new String(csvFile.getBytes());
			this.comprobarContenidoFichero(contenido);
			scanner = new Scanner(contenido);
			Asignatura asignatura = null;
			// saltamos la cabecera
			scanner.nextLine();
			while (scanner.hasNext())
			{
				// separamos la linea por ,
				String[] linea = scanner.nextLine().split(",");

				Curso curso = new Curso(Integer.parseInt(linea[1]), linea[2], linea[3]);
				// comprobamos que el curso existe
				if (!listaCursos.contains(curso))
				{
					String error = "Curso no encontrado: " + curso;
					log.error(error);
					throw new HorarioException(12, error);
				}
				if (linea.length == 6)
				{
					Departamento departamento = new Departamento(linea[5]);
					// comprobamos que el departamento exista
					asignatura = new Asignatura(linea[0], Integer.parseInt(linea[1]), linea[2], linea[3],
							Integer.valueOf(linea[4]), linea[5]);
					if (!listaDepartamentos.contains(departamento))
					{
						String error = "Departamento no encontrado";
						log.error(error);
						throw new HorarioException(12, error);
					}
				}
				else if(linea.length == 5)
				{
					asignatura = new Asignatura(linea[0], Integer.parseInt(linea[1]), linea[2], linea[3],
							Integer.valueOf(linea[4]), null);
				}
				else 
				{
					throw new HorarioException(Constants.ERR_LECTURA_FICHEROS_CSV_CODE, Constants.ERR_LECTURA_FICHEROS_CSV_MSG + "parseAsignaturas");
				}

				log.info(listaAsignaturas);
				listaAsignaturas.add(asignatura);
			}

			return listaAsignaturas;
		}
		catch (IOException ioException)
		{
			log.error(Constants.ERR_LECTURA_FICHEROS_CSV_MSG + "parseAsignaturas", ioException);
			
			throw new HorarioException(Constants.ERR_LECTURA_FICHEROS_CSV_CODE, 
									   Constants.ERR_LECTURA_FICHEROS_CSV_MSG + "parseAsignaturas", 
									   ioException);
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
	 * metodo para parsear reducciones
	 * 
	 * @param csvFile fichero csv
	 * @param listaCursos lista de cursos
	 * @return
	 * @throws HorarioException
	 */
	public List<Reduccion> parseReducciones(MultipartFile csvFile, List<Curso> listaCursos) throws HorarioException 
	{
		Scanner scanner = null;
		try
		{
			List<Reduccion> listaReducciones = new ArrayList<>();
			Reduccion reduccion;
			String contenido = new String(csvFile.getBytes());
			this.comprobarContenidoFichero(contenido);
			scanner = new Scanner(contenido);
			// saltamos la cabecera
			scanner.nextLine();
			while (scanner.hasNext())
			{
				// separamos la linea por ,
				String[] linea = scanner.nextLine().split(",");
				// comprobamos si es tutoria
				if ("tutoria".equalsIgnoreCase(linea[1]))
				{
					Curso curso = new Curso(Integer.parseInt(linea[3]), linea[4], linea[5]);
					// comprobamos si el curso existe
					if (!listaCursos.contains(curso))
					{
						String error = "Curso no encontrado";
						log.error(error);
						throw new HorarioException(12, error);
					}

					Reduccion reduccionObject = new Reduccion(linea[0], linea[1], Integer.parseInt(linea[2]),
							String.valueOf(curso.getCurso()), curso.getEtapa(), curso.getGrupo());
					listaReducciones.add(reduccionObject);
				}
				else
				{
					reduccion = new Reduccion(linea[0], linea[1], Integer.valueOf(linea[2]), null, null, null);
					listaReducciones.add(reduccion);
				}

			}
			return listaReducciones;
		}
		catch (IOException ioException)
		{
			log.error(Constants.ERR_LECTURA_FICHEROS_CSV_MSG + "parseReducciones", ioException);
			
			throw new HorarioException(Constants.ERR_LECTURA_FICHEROS_CSV_CODE, 
									   Constants.ERR_LECTURA_FICHEROS_CSV_MSG + "parseReducciones", 
									   ioException);
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
	 * metodo para obtener el resumen de un profesor
	 * 
	 * @param nombreDepartamento nombre del departamento
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @param numeroProfesorDepartamento numero de profesores en el departamento
	 * @param totalHoras total de horas 
	 * @return
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public Resumen resumenDepartamento(String nombreDepartamento, HttpSession session) throws HorarioException 
	{
		int numeroProfesorDepartamento = 0;
		int totalHoras = 0;
		Map<String, List<ReduccionHoras>> mapaReduccion = (Map<String, List<ReduccionHoras>>) session
				.getAttribute("mapaReduccion");
		Map<String, List<Asignatura>> mapaAsignatura = (Map<String, List<Asignatura>>) session
				.getAttribute("mapaAsignaturas");
		Map<String, Integer> mapaGuardias = (Map<String, Integer>) session.getAttribute("mapaGuardias");

		List<Profesor> listaProfesores = (List<Profesor>) session.getAttribute("listaProfesores");
		for (Profesor profesor : listaProfesores)
		{
			// comprobamos si el profesor pertenece a ese departamento
			if (profesor.getDepartamento().equalsIgnoreCase(nombreDepartamento))
			{
				numeroProfesorDepartamento++;
				String profesorId = profesor.getIdProfesor();
				// comprobamos que el mapa de asignaturas existe para sumar horas
				if (mapaAsignatura != null)
				{
					totalHoras = totalHoras + this.obtenerHorasAsignaturas(mapaAsignatura, profesorId);
				}
				// comprobamos que el mapa de reducciones existe para sumar horas
				if (mapaReduccion != null)
				{
					totalHoras = totalHoras + this.obtenerHorasReduccion(mapaReduccion, profesorId);
				}
				// comprobamos que el mapa de guardias no es nulo y contiene la id del profesor
				if (mapaGuardias != null && mapaGuardias.containsKey(profesorId))
				{
					totalHoras += mapaGuardias.get(profesorId);
				}
			}
		}

		int horasNecesarias = numeroProfesorDepartamento * 18;
		int desfase = totalHoras - horasNecesarias;

		String resultadoDesfase = "Cerrado";
		if (desfase > 0)
		{
			resultadoDesfase = "Sobran horas";
		} 
		else if (desfase < 0)
		{
			resultadoDesfase = "Faltan horas";
		}

		return new Resumen(numeroProfesorDepartamento, horasNecesarias, totalHoras, desfase, resultadoDesfase);
	}

	/**
	 * metodo para obtener las horas de reduccion
	 * 
	 * @param mapaReduccion mapa de reducciones
	 * @param profesorId id del profesor
	 * @return
	 */
	private int obtenerHorasReduccion(Map<String, List<ReduccionHoras>> mapaReduccion, String profesorId) 
	{
		int totalHoras = 0;

		List<ReduccionHoras> listaReducciones = mapaReduccion.get(profesorId);

		for (ReduccionHoras reduccionHoras : listaReducciones)
		{
			totalHoras += reduccionHoras.getNumHoras();
		}

		return totalHoras;
	}

	/**
	 * metodo para obtener las horas de las asignaturas
	 * 
	 * @param mapaAsignatura mapa de las asignaturas
	 * @param profesorId id del profesor
	 * @return
	 */
	private int obtenerHorasAsignaturas(Map<String, List<Asignatura>> mapaAsignatura, String profesorId) 
	{
		int totalHoras = 0;

		List<Asignatura> listaAsignaturas = mapaAsignatura.get(profesorId);

		for (Asignatura asignatura : listaAsignaturas)
		{
			totalHoras += asignatura.getNumeroHorasSemanales();
		}

		return totalHoras;
	}

	/**
	 * metodo para realizar una reduccion
	 * 
	 * @param idProfesor id del profesor
	 * @param idReduccion id de la reduccion
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @param listaReducciones lista de reducciones
	 * @param listaReduccionHoras lista de las horas de reduccion
	 * @return
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<ReduccionHoras>> realizarReduccion(String idProfesor, String idReduccion,
			HttpSession session, List<Reduccion> listaReducciones, List<ReduccionHoras> listaReduccionHoras)
			throws HorarioException
	{
		ReduccionHoras reduccionHoras = new ReduccionHoras();
		boolean reduccionEncontrada = false;
		Map<String, List<ReduccionHoras>> asignacionReduccion;
		int i = 0;
		while (i < listaReducciones.size() && !reduccionEncontrada)
		{
			if (listaReducciones.get(i).getIdReduccion().equalsIgnoreCase(idReduccion))
			{
				reduccionEncontrada = true;
				reduccionHoras.setIdReduccion(idReduccion);
				reduccionHoras.setNumHoras(listaReducciones.get(i).getNumeroHoras());
			}
			i++;
		}
		listaReduccionHoras.add(reduccionHoras);
		if (session.getAttribute("mapaReduccion") == null)
		{
			asignacionReduccion = new TreeMap<String, List<ReduccionHoras>>();
			asignacionReduccion.put(idProfesor, listaReduccionHoras);
			session.setAttribute("mapaReduccion", asignacionReduccion);
		}
		else
		{
			asignacionReduccion = (Map<String, List<ReduccionHoras>>) session.getAttribute("mapaReduccion");
			if (asignacionReduccion.containsKey(idProfesor))
			{
				List<ReduccionHoras> existingReduccionHoras = asignacionReduccion.get(idProfesor);
				boolean idReduccionExists = false;

				// comprobamos si la id de reduccion existe
				i = 0;
				while (i < existingReduccionHoras.size() && !idReduccionExists)
				{
					if (existingReduccionHoras.get(i).getIdReduccion().equalsIgnoreCase(idReduccion))
					{
						idReduccionExists = true;
					}
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
			session.setAttribute("mapaReduccion", asignacionReduccion);
		}
		return asignacionReduccion;
	}

	/**
	 * metodo para validar y crear el objeto
	 * 
	 * @param nombreAsignatura nombre de la asignatura
	 * @param curso número del curso
	 * @param etapa etapa del curso
	 * @param grupo grupo del curso
	 * @param datosAsignacion lista de asignaturas para asignar
	 * @param listaAsignaturas lista de asignaturas
	 * @param listaCursos lista de cursos
	 * @param asignaturaObject objeto de asignatura
	 * @param asignaturaEncontrada booleano para comprobar que existe la asignatura
	 * @throws HorarioException
	 */
	public String comprobacionCreacionObjeto(String nombreAsignatura, Integer curso, String etapa, String grupo,
			List<Asignatura> datosAsignacion, List<Asignatura> listaAsignaturas, List<Curso> listaCursos,
			Asignatura asignaturaObject) throws HorarioException
	{
		boolean asignaturaEncontrada = false;
		boolean cursoExiste = false;
		String result = "No se ha podido crear la asignacion";
		int i = 0;
		while (i < listaAsignaturas.size() && !cursoExiste)
		{
			if (listaAsignaturas.get(i).getCurso() == curso && listaAsignaturas.get(i).getEtapa().equals(etapa)
					&& listaAsignaturas.get(i).getGrupo().equals(grupo)
					&& listaAsignaturas.get(i).getNombreAsinatura().equalsIgnoreCase(nombreAsignatura))
			{
				cursoExiste = true;
				asignaturaEncontrada = true;
				asignaturaObject.setDepartamento(listaAsignaturas.get(i).getDepartamento());
				asignaturaObject.setNumeroHorasSemanales(listaAsignaturas.get(i).getNumeroHorasSemanales());
			}
			i++;
		}
		if (asignaturaEncontrada && cursoExiste)
		{
			asignaturaObject.setCurso(curso);
			asignaturaObject.setEtapa(etapa);
			asignaturaObject.setGrupo(grupo);
			result = "asignacion creada correctamente";
			datosAsignacion.add(asignaturaObject);
		}
		return result;
	}

	/**
	 * metodo para comprobar el curso
	 * 
	 * @param listaCursos lista de cursos
	 * @param cursoExiste booleano para comprobar si el curso existe
	 * @param cursoAsignacion Objeto curso
	 * @throws HorarioException 
	 */
	public void comprobarCurso(List<Curso> listaCursos, boolean cursoExiste, Curso cursoAsignacion)
			throws HorarioException
	{
		int i = 0;
		while (i < listaCursos.size() && !cursoExiste)
		{
			if (listaCursos.get(i).equals(cursoAsignacion))
			{
				cursoExiste = true;
			}
			i++;
		}
		if (!cursoExiste)
		{
			String error = "Curso no encontrado";
			log.info(error);
			throw new HorarioException(13, error);
		}
	}

	/**
	 * metodo para comprobar el id del profesor
	 * 
	 * @param idProfesor id del profesor
	 * @param listaProfesores lista de profesores
	 * @throws HorarioException
	 */
	public void comprobarIdProfesor(String idProfesor, List<Profesor> listaProfesores) throws HorarioException 
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
			log.info(error);
			throw new HorarioException(13, error);
		}
	}

	/**
	 * metodo para comprobar que la lista de departamentos existe
	 * 
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @param listaDepartamentos lista de departamentos
	 * @return
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public List<Departamento> comprobarListaDepartamentos(HttpSession session, List<Departamento> listaDepartamentos)
			throws HorarioException
	{
		if (session.getAttribute("listaDepartamentos") != null)
		{
			listaDepartamentos = (List<Departamento>) session.getAttribute("listaDepartamentos");
		}
		else
		{
			String error = "Los departamentos no han sido cargados en sesion todavía";
			throw new HorarioException(1, error);
		}
		return listaDepartamentos;
	}

	/**
	 * metodo para comprobar si la lista de cursos existe
	 * 
	 * @param session utilizado para guardas u obtener cosas en sesión 
	 * @param listaCursos lista de cursos
	 * @return
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public List<Curso> comprobarListaCursos(HttpSession session, List<Curso> listaCursos) throws HorarioException {
		if (session.getAttribute("listaCursos") != null)
		{
			listaCursos = (List<Curso>) session.getAttribute("listaCursos");
		} 
		else
		{
			String error = "Los cursos no han sido cargados en sesion todavía";
			throw new HorarioException(1, error);
		}
		return listaCursos;
	}

	/**
	 * metodo para comprobar la lista de profesores
	 * 
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @param listaProfesores lista de profesores
	 * @return
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public List<Profesor> comprobarListaProfesores(HttpSession session, List<Profesor> listaProfesores)
			throws HorarioException
	{
		if (session.getAttribute("listaProfesores") != null)
		{
			listaProfesores = (List<Profesor>) session.getAttribute("listaProfesores");
		}
		else
		{
			String error = "Los profesores no han sido cargados en sesion todavía";
			throw new HorarioException(1, error);
		}
		return listaProfesores;
	}

	/**
	 * metodo para comprobar la lista de asignaturas
	 * 
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @param listaAsignaturas lista de asignaturas
	 * @return
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public List<Asignatura> comprobarListaAsignaturas(HttpSession session, List<Asignatura> listaAsignaturas)
			throws HorarioException
	{
		if (session.getAttribute("listaAsignaturas") != null)
		{
			listaAsignaturas = (List<Asignatura>) session.getAttribute("listaAsignaturas");
		}
		else
		{
			String error = "Las asignatura no han sido cargados en sesion todavía";
			throw new HorarioException(1, error);
		}
		return listaAsignaturas;
	}

	/**
	 * metodo para comprobar la lista de reducciones
	 * 
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @param listaReducciones lista de reducciones
	 * @return
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public List<Reduccion> comprobarListaReducciones(HttpSession session, List<Reduccion> listaReducciones)
			throws HorarioException
	{
		if (session.getAttribute("listaReducciones") != null)
		{
			listaReducciones = (List<Reduccion>) session.getAttribute("listaReducciones");
		}
		else
		{
			String error = "Las reducciones no han sido cargados en sesion todavía";
			throw new HorarioException(1, error);
		}
		return listaReducciones;
	}

	/**
	 * metodo para comprobar si el departamento existe
	 * 
	 * @param listaDepartamentos lista de departamentos
	 * @param departamento objeto departamento
	 * @throws HorarioException
	 */
	public void comprobarDepartamentoExiste(List<Departamento> listaDepartamentos, Departamento departamento)
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
	 * metodo para comprobar si la id reduccion existe
	 * 
	 * @param idReduccion id de la reduccion
	 * @param listaReducciones lista de reduccionnes
	 * @throws HorarioException
	 */
	public void comprobarIdReduccionExiste(String idReduccion, List<Reduccion> listaReducciones)
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
	 * metodo para comprobar si el nombre de asignatura existe
	 * 
	 * @param nombreAsignatura nombre de la asignatura
	 * @param listaAsignaturas lista de asignaturas
	 * @param asignaturaExiste booleano para comprobar si la asignatura existe
	 * @throws HorarioException
	 */
	public void comprobarNombreAsignaturaExiste(String nombreAsignatura, List<Asignatura> listaAsignaturas)
			throws HorarioException
	{
		boolean asignaturaExiste = false;
		int i = 0;
		while (i < listaAsignaturas.size() && !asignaturaExiste)
		{
			if (listaAsignaturas.get(i).getNombreAsinatura().equalsIgnoreCase(nombreAsignatura))
			{
				asignaturaExiste = true;
			}
			i++;
		}
		if (!asignaturaExiste)
		{
			String error = "Asignatura no encontrada";
			log.info(error);
			throw new HorarioException(13, error);
		}
	}

	/**
	 * 
	 * @param idProfesor id del profesor
	 * @param mapaReduccion mapa de reducciones
	 * @param mapaAsignatura mapa de asignaturas
	 * @return
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
	 * metodo para comprobar si existe el mapa de reduccion
	 * 
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @param mapaReduccion mapa de reducción
	 * @return
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<ReduccionHoras>> comprobarMapaReduccion(HttpSession session,
			Map<String, List<ReduccionHoras>> mapaReduccion) throws HorarioException
	{
		if (session.getAttribute("mapaReduccion") != null)
		{
			mapaReduccion = (Map<String, List<ReduccionHoras>>) session.getAttribute("mapaReduccion");
		}
		else
		{
			String error = "No se ha realizado reducciones todavía";
			throw new HorarioException(1, error);
		}
		return mapaReduccion;
	}

	/**
	 * metodo para comprobar si existe el mapa asignaturas
	 * 
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @param mapaAsignatura mapa de asignaturas
	 * @return
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<Asignatura>> comprobarMapaAsignaturas(HttpSession session,
			Map<String, List<Asignatura>> mapaAsignatura) throws HorarioException
	{
		if (session.getAttribute("mapaAsignaturas") != null)
		{
			mapaAsignatura = (Map<String, List<Asignatura>>) session.getAttribute("mapaAsignaturas");
		}
		else
		{
			String error = "No se ha realizado asignacion de asignaturas todavía";
			throw new HorarioException(1, error);
		}
		return mapaAsignatura;
	}

	/**
	 * metodo para parsear el fichero matriculas curso
	 * 
	 * @param csvFile fichero csv
	 * @param curso número de curso
	 * @param etapa etapa del curso
	 * @param mapaAsignaturas mapa de asignaturas
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @return
	 * @throws IOException
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public String parseCursosMap(MultipartFile csvFile, Integer curso, String etapa,
			Map<String, List<String>> mapaAsignaturas, HttpSession session) throws IOException, HorarioException
	{
		Scanner scanner = null;
		try
		{
			boolean encontrado = false;
			Parse parse = new Parse();
			String contenido = new String(csvFile.getBytes());
			scanner = new Scanner(contenido);
			String clave = curso + etapa;
			String[] linea = scanner.nextLine().split(",");
			List<Asignatura> listaAsignatura = (List<Asignatura>) session.getAttribute("listaAsignaturas");
			parse.comprobarListaAsignaturas(session, listaAsignatura);
			List<String> listaNombres = new ArrayList<String>();
			listaNombres = inicializarListaNombres(session);
			comprobarListaAsignaturas(session, listaAsignatura);
			List<Curso> listaCursos = (List<Curso>) session.getAttribute("listaCursos");
			comprobarListaCursos(session, listaCursos);
			int i = 0;
			while (i < listaCursos.size() && !encontrado)
			{
				if (listaCursos.get(i).getCurso() == curso && listaCursos.get(i).getEtapa().equalsIgnoreCase(etapa))
				{
					encontrado = true;
				}
				i++;
			}

			String asignatura1 = "";
			String asignatura2 = "";
			String asignatura3 = "";
			if (encontrado)
			{
				for (Asignatura asignatura : listaAsignatura)
				{
					if (linea[1].equalsIgnoreCase(asignatura.getNombreAsinatura()))
					{
						asignatura1 = linea[1];
					}
					if (linea[2].equalsIgnoreCase(asignatura.getNombreAsinatura()))
					{
						asignatura2 = linea[2];
					}
					if (linea[3].equalsIgnoreCase(asignatura.getNombreAsinatura()))
					{
						asignatura3 = linea[3];
					}
				}
				parseMatriculaCursos(mapaAsignaturas, scanner, listaNombres, asignatura1, asignatura2, asignatura3);
				session.setAttribute("listaNombres", listaNombres);
				log.info(listaNombres);
				session.setAttribute("mapaAsignaturasCursos", mapaAsignaturas);
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
									   Constants.ERR_LECTURA_FICHEROS_CSV_MSG + "parseCursosMap", 
									   ioException);
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
	 * metodo para parsear el fichero matricula Cursos
	 * 
	 * @param mapaAsignaturas mapa de asignaturas
	 * @param scanner escaner para leer
	 * @param listaNombres lista de nombres
	 * @param asignatura1 asignatura
	 * @param asignatura2 asignatura
	 * @param asignatura3 asignatura
	 */
	public void parseMatriculaCursos(Map<String, List<String>> mapaAsignaturas, Scanner scanner,
			List<String> listaNombres, String asignatura1, String asignatura2, String asignatura3)
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
	}

	/**
	 * metodo para inicializar la lista de nombres
	 * 
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @return
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
	 * metodo para comprobar si existe el mapa cursos
	 * 
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @param mapaCursos mapa de cursos
	 * @return
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Map<String, List<String>>> comprobarMapaCursosExiste(HttpSession session,
			Map<String, Map<String, List<String>>> mapaCursos) throws HorarioException
	{
		if (session.getAttribute("mapaCursos") != null)
		{
			mapaCursos = (Map<String, Map<String, List<String>>>) session.getAttribute("mapaCursos");
		} 
		else
		{
			String error = "No se ha cargado el mapa de cursos";
			throw new HorarioException(1, error);
		}
		return mapaCursos;
	}

	/**
	 * metodo para comprobar si el mapa bloques existe
	 * 
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @param mapaBloques mapa de bloques
	 * @return
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<String>> comprobarMapaBloquesExiste(HttpSession session,
			Map<String, List<String>> mapaBloques) throws HorarioException
	{
		if (session.getAttribute("mapaBloques") != null)
		{
			mapaBloques = (Map<String, List<String>>) session.getAttribute("mapaBloques");
		}
		else
		{
			String error = "No se ha realizado asignacion de bloques todavía";
			throw new HorarioException(1, error);
		}
		return mapaBloques;
	}

	/**
	 * metodo para incializar el mapa de cursos
	 * 
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @return
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
	 * metodo para comprobar si la lista existe
	 * 
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @param listaNombres lista con el nombre y apellidos de los alumnos 
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public void comprobarListaNombresExiste(HttpSession session, List<String> listaNombres) throws HorarioException
	{
		if (session.getAttribute("listaNombres") != null)
		{
			listaNombres = (List<String>) session.getAttribute("listaNombres");
		}
		else
		{
			String error = "Los nombres no han sido cargados todavia";
			throw new HorarioException(1, error);
		}
	}

	/**
	 * metodo para realizar la asignacion de un alumno
	 * 
	 * @param alumno nombre y apellido del alumno
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @param cursoObject Objeto curso
	 * @param resultado string con el resultado
	 * @param clave clave para el mapa
	 * @param listaCursos lista de cursos
	 * @param listaNombres lista de nombres
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
							resultado = "no se ha podido añadir";
						}
						else
						{
							listaAlumnos.add(alumno);
							resultado = "se ha añadido correctamente";
							mapaAlumnos.put(clave, listaAlumnos);
							session.setAttribute("mapaAlumnos", mapaAlumnos);
						}
					} 
					else
					{
						listaAlumnos.add(alumno);
						resultado = "se ha añadido correctamente";
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
					resultado = "se ha añadido correctamente";
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
	 * metodo para comprobar si el mapa asignaturas cursos existe
	 * @param session utilizado para guardas u obtener cosas en sesión 
	 * @param mapaAsignaturas mapa de asignaturas
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public void comprobarMapaAsignaturasCursosExiste(HttpSession session,Map<String, List<String>> mapaAsignaturas) throws HorarioException 
	{
		if (session.getAttribute("mapaAsignaturasCursos") != null)
		{
			mapaAsignaturas = (Map<String, List<String>>) session.getAttribute("mapaAsignaturasCursos");
		}
		else
		{
			String error = "No se ha realizado la asignacion de asignatura a cursos";
			throw new HorarioException(1, error);
		}
	}
	/**
	 * 
	 * @param mapaAlumnos mapa de alumnos
	 * @param session utilizado para guardas u obtener cosas en sesión
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public void comprobarMapaAlumnoExiste(Map<String, List<String>> mapaAlumnos,HttpSession session) throws HorarioException
	{
		if (session.getAttribute("mapaAlumnos") != null)
		{
			mapaAlumnos = (Map<String, List<String>>) session.getAttribute("mapaAlumnos");
		} 
		else
		{
			String error = "El mapa de alumnos no ha sido cargado en sesion todavía";
			throw new HorarioException(1, error);
		}
	}
}
