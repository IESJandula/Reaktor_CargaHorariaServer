package es.iesjandula.direccion_cargahoraria_server.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.web.multipart.MultipartFile;

import es.iesjandula.direccion_cargahoraria_server.exception.HorarioException;
import es.iesjandula.direccion_cargahoraria_server.models.Asignatura;
import es.iesjandula.direccion_cargahoraria_server.models.Curso;
import es.iesjandula.direccion_cargahoraria_server.models.Departamento;
import es.iesjandula.direccion_cargahoraria_server.models.Profesor;
import es.iesjandula.direccion_cargahoraria_server.models.Reduccion;
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

			Validations validations = new Validations();

			String contenido = validations.obtenerContenidoFichero(Constants.NOMBRE_METH_PARSE_DEPARTAMENTOS, csvFile);

			scanner = new Scanner(contenido);
			// Saltamos la cabecera
			scanner.nextLine();
			while (scanner.hasNext())
			{
				String linea = scanner.nextLine();

				// Separamos la linea por ,
				String[] lineaArray = linea.split(",");

				if (lineaArray.length != 1)
				{
					String errorString = Constants.ERR_LECTURA_FICHEROS_CSV_MSG
							+ Constants.NOMBRE_METH_PARSE_DEPARTAMENTOS + ". Concretamente aquí: " + linea;

					log.error(errorString);
					throw new HorarioException(Constants.ERR_LECTURA_FICHEROS_CSV_CODE, errorString);
				}

				listaDepartamentos.add(new Departamento(lineaArray[0]));
			}
			// mostramos la lista de departamentos en el log
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
	 * Método para parsear cursos
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
			Validations validations = new Validations();

			String contenido = validations.obtenerContenidoFichero(Constants.NOMBRE_METH_PARSE_CURSOS, csvFile);
			scanner = new Scanner(contenido);
			// Saltamos la cabecera
			scanner.nextLine();
			while (scanner.hasNext())
			{
				String linea = scanner.nextLine();

				// Separamos la linea por ,
				String[] lineaArray = linea.split(",");
				if (lineaArray.length != 3)
				{
					String errorString = Constants.ERR_LECTURA_FICHEROS_CSV_MSG + Constants.NOMBRE_METH_PARSE_CURSOS
							+ ". Concretamente aquí: " + linea;

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
	 * Método para parsear profesores
	 * 
	 * @param csvFile            Fichero csv
	 * @param listaDepartamentos Lista de departamentos para obtener si existe
	 * @return La lista de profesores
	 * @throws HorarioException  Excepción en la línea en la que ocurre el error
	 */
	public List<Profesor> parseProfesores(MultipartFile csvFile, List<Departamento> listaDepartamentos)
			throws HorarioException
	{
		Scanner scanner = null;
		try
		{
			List<Profesor> listaProfesores = new ArrayList<>();
			Validations validations = new Validations();

			String contenido = validations.obtenerContenidoFichero(Constants.NOMBRE_METH_PARSE_PROFESORES, csvFile);

			scanner = new Scanner(contenido);
			// Saltamos la cabecera
			scanner.nextLine();

			while (scanner.hasNext())
			{
				String linea = scanner.nextLine();

				// Separamos la linea por ,
				String[] lineaArray = linea.split(",");
				if (lineaArray.length != 3)
				{
					String errorString = Constants.ERR_LECTURA_FICHEROS_CSV_MSG + Constants.NOMBRE_METH_PARSE_PROFESORES
							+ ". Concretamente aquí: " + linea;

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
	 * Método para parsear asignaturas
	 * 
	 * @param csvFile            Fichero csv
	 * @param listaCursos        Lista de cursos
	 * @param listaDepartamentos Lista de departamentos
	 * @return La lista de asignaturas
	 * @throws HorarioException	Excepción en la línea en la que ocurre el error
	 */
	public List<Asignatura> parseAsignaturas(MultipartFile csvFile, List<Curso> listaCursos,
			List<Departamento> listaDepartamentos) throws HorarioException
	{
		Scanner scanner = null;
		try
		{
			List<Asignatura> listaAsignaturas = new ArrayList<>();

			Validations validations = new Validations();
			
			// Obtenemos el contenido del fichero
			String contenido = validations.obtenerContenidoFichero(Constants.NOMBRE_METH_PARSE_ASIGNATURAS, csvFile);
			
			scanner = new Scanner(contenido);
			
			// Saltamos la cabecera
			scanner.nextLine();
			
			while (scanner.hasNext())
			{
				String linea = scanner.nextLine();

				// Separamos la linea por ,
				String[] lineaArray = linea.split(",");

				Curso curso = new Curso(Integer.parseInt(lineaArray[1]), lineaArray[2], lineaArray[3]);
				
				// Validamos si el curso existe
				validations.validarExistenciaCurso(listaCursos, curso);

				// Generamos la asignatgura o tutoria
				Asignatura asignatura = this.generarAsignaturaOTutoria(listaDepartamentos, validations, linea, lineaArray);
				
				// Log para mostrar la lista de asignaturas
				log.info(listaAsignaturas);
				
				// Añadimos la asignatura a la lista de asignaturas
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
	 * Método para generar asignatura o tutoria
	 * 
	 * @param listaDepartamentos Lista de departamentos
	 * @param validations Objeto validations
	 * @param linea Linea que se parsea
	 * @param lineaArray Linea separada por comas
	 * @return Un Objeto asignatura
	 * @throws HorarioException Excepción en la línea en la que ocurre el error
	 */
	private Asignatura generarAsignaturaOTutoria(List<Departamento> listaDepartamentos, Validations validations, String linea, String[] lineaArray) throws HorarioException
	{
		Asignatura asignatura = null ;
		
		boolean esAsignatura = lineaArray.length == 6;
		boolean esTutoria = lineaArray.length == 5;

		if (!esAsignatura && !esTutoria)
		{
			String errorString = Constants.ERR_LECTURA_FICHEROS_CSV_MSG
					+ Constants.NOMBRE_METH_PARSE_ASIGNATURAS + ". Concretamente aquí: " + linea;

			log.error(errorString);
			throw new HorarioException(Constants.ERR_LECTURA_FICHEROS_CSV_CODE, errorString);
		}
		else if (esAsignatura)
		{
			Departamento departamento = new Departamento(lineaArray[5]);
			// Obtenemos el departamento
			validations.obtenerDepartamento(listaDepartamentos, departamento);
			
			// Creamos la asignatura
			asignatura = new Asignatura(lineaArray[0], Integer.parseInt(lineaArray[1]), lineaArray[2],
					lineaArray[3], Integer.valueOf(lineaArray[4]), lineaArray[5]);
		}
		else
		{
			// Creamos la asignatura
			asignatura = new Asignatura(lineaArray[0], Integer.parseInt(lineaArray[1]), lineaArray[2],
					lineaArray[3], Integer.valueOf(lineaArray[4]), null);
		}
		
		return asignatura ;
	}

	/**
	 * Método para parsear reducciones
	 * 
	 * @param csvFile     Fichero csv
	 * @param listaCursos Lista de cursos
	 * @return Lista de reducciones
	 * @throws HorarioException Controla que el fichero se pueda parsear
	 *                          correctamente
	 */
	public List<Reduccion> parseReducciones(MultipartFile csvFile, List<Curso> listaCursos) throws HorarioException 
	{
		Scanner scanner = null;
		try
		{
			List<Reduccion> listaReducciones = new ArrayList<>();
			Validations validations = new Validations();

			String contenido = validations.obtenerContenidoFichero(Constants.NOMBRE_METH_PARSE_REDUCCIONES, csvFile);
			scanner = new Scanner(contenido);
			// Saltamos la cabecera
			scanner.nextLine();
			while (scanner.hasNext())
			{
				String linea = scanner.nextLine();
				// Separamos la linea por ,
				String[] lineaArray = linea.split(",");

				boolean esReduccion = lineaArray.length == 3;

				if (!esReduccion)
				{
					String errorString = Constants.ERR_LECTURA_FICHEROS_CSV_MSG
							+ Constants.NOMBRE_METH_PARSE_REDUCCIONES + ". Concretamente aquí: " + linea;

					log.error(errorString);
					throw new HorarioException(Constants.ERR_LECTURA_FICHEROS_CSV_CODE, errorString);
				}
				
				listaReducciones.add(new Reduccion(lineaArray[0], lineaArray[1], Integer.valueOf(lineaArray[2])));
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
	 * Método para parsear el fichero matriculas curso
	 * 
	 * @param csvFile         Fichero csv
	 * @param curso           Número de curso
	 * @param etapa           Etapa del curso
	 * @param mapaAsignaturas Mapa de asignaturas
	 * @param session         Utilizado para guardas u obtener cosas en sesión
	 * @param mapaCursos	  Con el mapa de cursos
	 * @return La clave del mapa
	 * @throws IOException      Se lanzará esta excepción si hay un error con el
	 *                          fichero
	 * @throws HorarioException Se lanza si el contenido del fichero, la lista de cursos, validar curso etapa, obtener lista asignaturas
	 * 							o validar asignaturas da error
	 */
	public void parseCursosMap(MultipartFile csvFile, Integer curso, String etapa, HttpSession session, Map<String, Map<String, List<String>>> mapaCursos)
			throws IOException, HorarioException
	{
		Scanner scanner = null;
		try
		{
			Validations validations = new Validations();
			String contenido = validations.obtenerContenidoFichero(Constants.NOMBRE_METH_PARSE_CURSOSMAP, csvFile);
			
			scanner = new Scanner(contenido);
			
			// Obtenemos la lista de cursos
			List<Curso> listaCursos = validations.obtenerListaCursos(session);
			
			this.validarCursoEtapa(curso, etapa, listaCursos);
			
			String linea = scanner.nextLine();
			
			// Leemos las asignaturas y validamos si fueron creadas previamente
			List<String> listaPosiblesAsignaturas = this.parseCursosMapValidarAsignaturas(session, validations, linea);

			Map<String, List<String>> mapaAsignaturas = this.inicializarMapaAsignaturas(session);
			
			// Obtenemos la lista de apellidos y nombres de alumnos
			List<String> listaApellidosNombreAlumnos = validations.inicializarListaApellidosNombreAlumnos(session);
			
			this.parseMatriculaCursos(mapaAsignaturas, scanner, listaApellidosNombreAlumnos, listaPosiblesAsignaturas, session);

			session.setAttribute(Constants.SESION_LISTA_NOMBRES, listaApellidosNombreAlumnos);

			String clave = curso + etapa.toUpperCase();
			
			mapaCursos.put(clave, mapaAsignaturas);
			
			session.setAttribute(Constants.SESION_MAPA_CURSOS, mapaCursos);
			// Mostramos el mapa de cursos en el log
			log.info("mapaCursos: " + mapaCursos);
		}
		finally
		{
			if (scanner != null)
			{
				scanner.close();
			}
		}
	}

	private List<String> parseCursosMapValidarAsignaturas(HttpSession session, Validations validations, String linea)
			throws HorarioException
	{
		// Separamos la linea por ,
		String[] lineaArray = linea.split(",");
		
		List<String> listaPosiblesAsignaturas = new ArrayList<String>();
		// Añadimos el contenido de linea array en la lista de posibles asignaturas
		Collections.addAll(listaPosiblesAsignaturas, lineaArray);
		// Eliminamos el alumno
		listaPosiblesAsignaturas.remove(0);
		log.info(listaPosiblesAsignaturas);

		// Obtenemos la lista de asignaturas
		List<Asignatura> listaAsignatura = validations.obtenerListaAsignaturas(session);
		
		this.validarAsignaturasExisten(listaPosiblesAsignaturas, listaAsignatura);
		return listaPosiblesAsignaturas;
	}
	/**
	 * Método para inicializar el mapa asignaturas si esta vacio
	 * 
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 * @return Mapa de asignaturas
	 */
	@SuppressWarnings("unchecked")
	private Map<String, List<String>> inicializarMapaAsignaturas(HttpSession session) 
	{
		Map<String, List<String>> mapaAsignaturas = (Map<String, List<String>>) session.getAttribute(Constants.SESION_MAPA_ASIGNATURA_CURSOS);
		
		if (mapaAsignaturas == null)
		{
			mapaAsignaturas = new HashMap<String, List<String>>();
		}
		
		return mapaAsignaturas;
	}
	
	/**
	 * Método para validar asignaturas
	 * 
	 * @param listaPosiblesAsignaturas lista con las asignaturas del csv
	 * @param listaAsignatura Lista con las asignaturas 
	 * @throws HorarioException Se lanza si la posible asignatura no existes
	 */
	private void validarAsignaturasExisten(List<String> listaPosiblesAsignaturas, List<Asignatura> listaAsignatura)
			throws HorarioException
	{
		for (String posibleAsignatura : listaPosiblesAsignaturas) 
		{
		    boolean asignaturaEncontrada = false;
		    int i = 0;
		    while (i < listaAsignatura.size() && !asignaturaEncontrada) 
		    {
		        Asignatura asignatura = listaAsignatura.get(i);
		        if (asignatura.getNombreAsignatura().equals(posibleAsignatura)) 
		        {
		            asignaturaEncontrada = true;
		        }
		        i++;
		    }
		    if (!asignaturaEncontrada) 
		    {
		        String error = "La asignatura " + posibleAsignatura + " no existe";
		        
		        // Log con el error
		        log.error(error);
		        
		        throw new HorarioException(Constants.ERR_ASIGNATURA, error);
		    }
		}
	}
	
	/**
	 * Metodo para validar el curso y etapa
	 * 
	 * @param curso Número del curso
	 * @param etapa Etapa del curso
	 * @param listaCursos Lista de cursos
	 * @throws HorarioException Se lanza si el curso o etapa no existe no existe
	 */
	private void validarCursoEtapa(Integer curso, String etapa, List<Curso> listaCursos)
			throws HorarioException
	{
		int i = 0;
		boolean encontrado = false;
		while (i < listaCursos.size() && !encontrado)
		{
			encontrado = listaCursos.get(i).getCurso() == curso && listaCursos.get(i).getEtapa().equalsIgnoreCase(etapa) ;

			i++;
		}
		
		if (!encontrado)
		{
			String error = "El curso o etapa no existe";
			
			// Log con el error
			log.error(error);
			
			throw new HorarioException(Constants.ERR_VALIDATE_CURSO_ETAPA, error);
		}
	}

	/**
	 * Método para parsear el fichero matriculas cursos
	 * 
	 * @param mapaAsignaturas Mapa de asignaturas
	 * @param scanner Scaner para leer las lineas
	 * @param listaNombres Lista de nombres de alumnos
	 * @param listaPosiblesAsignaturas Lista con los nombres de las asignaturas del csv
	 * @param session Utilizado para guardas u obtener cosas en sesión
	 */
	private void parseMatriculaCursos(Map<String, List<String>> mapaAsignaturas, Scanner scanner,
									  List<String> listaNombres, List<String> listaPosiblesAsignaturas, HttpSession session)
	{
		while (scanner.hasNext())
		{
			List<String> listaAsignaturas = new ArrayList<>();
			String linea = scanner.nextLine();
			// Separamos la linea por ,
			String[] lineaArray = linea.split(",");
			// Guardamos apellidos y nombre y los añadimos a la lista
			String apellidos = lineaArray[0];
			String nombre = lineaArray[1];
			String nombreCompleto = apellidos + "," + nombre;
			listaNombres.add(nombreCompleto);
			// Creamos una lista para guardar las matriculas
			List<String> listaMatriculaciones = new ArrayList<>();
			Collections.addAll(listaMatriculaciones, lineaArray);
			// Eliminamos el alumno
			listaMatriculaciones.remove(0);
			listaMatriculaciones.remove(0);
			// Recorremos la lista matriculas para comprobar asignar las asignaturas al
			// alumno
			for (int i = 0; i < listaMatriculaciones.size(); i++)
			{
				if (listaMatriculaciones.get(i).equalsIgnoreCase("MATR"))
				{
					listaAsignaturas.add(listaPosiblesAsignaturas.get(i));
				}
			}
			mapaAsignaturas.put(nombreCompleto, listaAsignaturas);
		}
		session.setAttribute(Constants.SESION_MAPA_ASIGNATURA_CURSOS, mapaAsignaturas);
	}

}
