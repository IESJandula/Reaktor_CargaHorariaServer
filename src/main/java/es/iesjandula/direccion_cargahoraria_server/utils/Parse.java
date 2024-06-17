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

			String contenido = validations.obtenerContenidoFichero("parseCursos", csvFile);
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

			String contenido = validations.obtenerContenidoFichero("parseCursos", csvFile);

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
			String contenido = validations.obtenerContenidoFichero("parseCursos", csvFile);
			scanner = new Scanner(contenido);
			Asignatura asignatura = null;
			// Saltamos la cabecera
			scanner.nextLine();
			while (scanner.hasNext())
			{
				String linea = scanner.nextLine();

				// Separamos la linea por ,
				String[] lineaArray = linea.split(",");

				Curso curso = new Curso(Integer.parseInt(lineaArray[1]), lineaArray[2], lineaArray[3]);
				validations.obtenerCurso(listaCursos, curso);

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
			Reduccion reduccion;
			Validations validations = new Validations();

			String contenido = validations.obtenerContenidoFichero("parseReducciones", csvFile);
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
				else
				{
					reduccion = new Reduccion(lineaArray[0], lineaArray[1], Integer.valueOf(lineaArray[2]), null, null,
							null);
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
	 * Método para parsear el fichero matriculas curso
	 * 
	 * @param csvFile         Fichero csv
	 * @param curso           Número de curso
	 * @param etapa           Etapa del curso
	 * @param mapaAsignaturas Mapa de asignaturas
	 * @param session         Utilizado para guardas u obtener cosas en sesión
	 * @return La clave del mapa
	 * @throws IOException      Se lanzará esta excepción si hay un error con el
	 *                          fichero
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public String parseCursosMap(MultipartFile csvFile, Integer curso, String etapa, HttpSession session)
			throws IOException, HorarioException
	{
		Scanner scanner = null;
		try
		{
			Validations validations = new Validations();
			boolean encontrado = false;
			String contenido = validations.obtenerContenidoFichero("parseCursosMap", csvFile);
			scanner = new Scanner(contenido);
			String clave = curso + etapa.toUpperCase();
			String linea = scanner.nextLine();
			// Separamos la linea por ,
			String[] lineaArray = linea.split(",");
			// Obtenemos la lista de asignaturas
			List<Asignatura> listaAsignatura = (List<Asignatura>) session.getAttribute("listaAsignaturas");
			validations.obtenerListaAsignaturas(session, listaAsignatura);
			// Obtenemos la lista de nombres
			List<String> listaNombres = validations.inicializarListaNombres(session);
			validations.obtenerListaAsignaturas(session, listaAsignatura);
			// Obtenemos la lista de cursos
			List<Curso> listaCursos = (List<Curso>) session.getAttribute("listaCursos");
			validations.obtenerListaCursos(session, listaCursos);
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
				List<String> listaPosiblesAsignaturas = new ArrayList<String>();
				// Añadimos el contenido de linea array en la lista de posibles asignaturas
				Collections.addAll(listaPosiblesAsignaturas, lineaArray);
				// Eliminamos el alumno
				listaPosiblesAsignaturas.remove(0);
				log.info(listaPosiblesAsignaturas);

				for (String posibleAsignatura : listaPosiblesAsignaturas)
				{
					boolean asignaturaEncontrada = false;
					for (Asignatura asignatura : listaAsignatura)
					{
						if (asignatura.getNombreAsignatura().equals(posibleAsignatura))
						{
							asignaturaEncontrada = true;
							break;
						}
					}
					if (!asignaturaEncontrada)
					{
						String error = "La asignatura " + posibleAsignatura + " no existe";
						throw new HorarioException(15, error);
					}
				}

				Map<String, List<String>> mapaAsignaturas = (Map<String, List<String>>) session
						.getAttribute("mapaAsignaturasCursos");
				if (mapaAsignaturas == null)
				{
					mapaAsignaturas = new HashMap<String, List<String>>();
				}
				this.parseMatriculaCursos(mapaAsignaturas, scanner, listaNombres, listaPosiblesAsignaturas, session);

				session.setAttribute("listaNombres", listaNombres);

				Map<String, Map<String, List<String>>> mapaCursos = (Map<String, Map<String, List<String>>>) session
						.getAttribute("mapaCursos");
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
	 * @param scanner         Escaner para leer
	 * @param listaNombres    Lista de nombres
	 * @param asignatura1     asignatura
	 * @param asignatura2     asignatura
	 * @param asignatura3     asignatura
	 */
	public void parseMatriculaCursos(Map<String, List<String>> mapaAsignaturas, Scanner scanner,
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
		session.setAttribute("mapaAsignaturasCursos", mapaAsignaturas);
	}

}
