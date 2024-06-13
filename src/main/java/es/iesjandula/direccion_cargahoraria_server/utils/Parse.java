package es.iesjandula.direccion_cargahoraria_server.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Arrays;
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
	 * @param csvFile
	 * @return
	 * @throws HorarioException
	 */
	public List<Departamento> parseDepartamentos(MultipartFile csvFile) throws HorarioException
	{
		Scanner scanner=null;
		try
		{
			List<Departamento> listaDepartamentos = new ArrayList<>();
			String contenido = new String(csvFile.getBytes());
			scanner = new Scanner(contenido);
			//saltamos la cabecera
			scanner.nextLine();
			while(scanner.hasNext())
			{
				//separamos la linea por ,
				String[] linea = scanner.nextLine().split(",");
				Departamento departamento = new Departamento(linea[0]);
				listaDepartamentos.add(departamento);
			}
			
			return listaDepartamentos;
		}
		catch(IOException ioException ) 
		{
			String error = "Error al realizar la lectura del fichero CSV";
			log.error(error,ioException);
			throw new HorarioException(11,error,ioException);
		}
		finally 
		{
			if(scanner!=null) 
			{
				scanner.close();			
			}
		}	
	}

	/**
	 * endpoint para parsear cursos
	 * @param csvFile
	 * @return
	 * @throws HorarioException
	 */
	public List<Curso> parseCursos(MultipartFile csvFile) throws HorarioException
	{
		Scanner scanner=null;
		try
		{
			List<Curso> listaCursos = new ArrayList<>();
			String contenido = new String(csvFile.getBytes());
			scanner = new Scanner(contenido);
			//saltamos la cabecera
			scanner.nextLine();
			while(scanner.hasNext())
			{
				//separamos la linea por ,
				String[] linea = scanner.nextLine().split(",");
				Curso curso = new Curso(Integer.parseInt(linea[0]),linea[1],linea[2]);
				listaCursos.add(curso);
			}
			return listaCursos;
		}
		catch(IOException ioException) 
		{
			String error = "Error al realizar la lectura del fichero CSV";
			log.error(error,ioException);
			throw new HorarioException(11,error,ioException);
		}
		finally 
		{
			if(scanner!=null) 
			{
				scanner.close();			
			}
		}		
	}
	
	/**
	 * 
	 * @param csvFile
	 * @param listaDepartamentos
	 * @return
	 * @throws HorarioException
	 */
	public  List<Profesor> parseProfesores(MultipartFile csvFile,List<Departamento> listaDepartamentos) throws HorarioException
	{
		Scanner scanner=null;
		try
		{
			List<Profesor> listaProfesores = new ArrayList<>();
			String contenido = new String(csvFile.getBytes());

			scanner = new Scanner(contenido);
			//saltamos la cabecera
			scanner.nextLine();

			while(scanner.hasNext())
			{
				//separamos la linea por ,
				String[] linea = scanner.nextLine().split(",");
				
				Departamento departamento = new Departamento(linea[2]);
				//comprobamos que el departamento existe
				if (!listaDepartamentos.contains(departamento))
				{
					String error = "Departamento no encontrado";
					log.error(error);
					throw new HorarioException(12,error);
				}

				Profesor profesor = new Profesor(linea[0], linea[1], linea[2]);
				listaProfesores.add(profesor);
			}
			return listaProfesores;
		}
		catch (IOException ioException) 
		{
			String error = "Error al realizar la lectura del fichero CSV";
			log.error(error,ioException);
			throw new HorarioException(11,error,ioException);
		}
		finally 
		{
			if(scanner!=null) 
			{
				scanner.close();			
			}
		}	
	}

	/**
	 * metodo para parsear asignaturas
	 * @param csvFile
	 * @param listaCursos
	 * @param listaDepartamentos
	 * @return
	 * @throws HorarioException
	 */
	public  List<Asignatura> parseAsignaturas(MultipartFile csvFile,List<Curso> listaCursos,List<Departamento> listaDepartamentos) throws HorarioException
	{
		Scanner scanner = null;
		try
		{
			List<Asignatura> listaAsignaturas = new ArrayList<>();
			String contenido = new String(csvFile.getBytes());
			scanner = new Scanner(contenido);
			Asignatura asignatura = null;
			//saltamos la cabecera
			scanner.nextLine();
			while(scanner.hasNext())
			{
				//separamos la linea por ,
				String[] linea = scanner.nextLine().split(",");
				
				Curso curso = new Curso(Integer.parseInt(linea [1]), linea[2], linea[3]);
				//comprobamos que el curso existe
				if(!listaCursos.contains(curso))
				{
					String error = "Curso no encontrado: " + curso;
					log.error(error);
					throw new HorarioException(12,error);
				}
				if(linea.length==6) 
				{
					Departamento departamento = new Departamento(linea[5]);
					//comprobamos que el departamento exista
					 asignatura = new Asignatura(linea[0], Integer.parseInt(linea [1]), linea[2], linea[3], Integer.valueOf(linea[4]), linea[5]);
					if(!listaDepartamentos.contains(departamento))
					{
						String error = "Departamento no encontrado";
						log.error(error);
						throw new HorarioException(12, error);
					}
				}
				else 
				{
					asignatura = new Asignatura(linea[0], Integer.parseInt(linea [1]), linea[2], linea[3], Integer.valueOf(linea[4]), null);
				}
				
				log.info(listaAsignaturas);
				listaAsignaturas.add(asignatura);
			}
			
			return listaAsignaturas;
		}
		catch(IOException ioException) 
		{
			String error = "Error al realizar la lectura del fichero CSV";
			log.error(error,ioException);
			throw new HorarioException(11,error,ioException);
		}
		finally 
		{
			if(scanner!=null) 
			{
				scanner.close();			
			}
		}	
	}
	
	/**
	 * metodo para parsear reducciones
	 * @param csvFile
	 * @param listaCursos
	 * @return
	 * @throws HorarioException
	 */
	public List<Reduccion> parseReducciones(MultipartFile csvFile,List<Curso> listaCursos) throws HorarioException
	{
		Scanner scanner=null;
		try
		{
			List<Reduccion> listaReducciones = new ArrayList<>();
			Reduccion reduccion;
			String contenido = new String(csvFile.getBytes());
			scanner = new Scanner(contenido);
			//saltamos la cabecera
			scanner.nextLine();
			while(scanner.hasNext())
			{
				//separamos la linea por ,
				String[] linea = scanner.nextLine().split(",");
				//comprobamos si es tutoria
				if("tutoria".equalsIgnoreCase(linea[1])) 
				{
					Curso curso = new Curso(Integer.parseInt(linea [3]),linea[4],linea[5]);
					//comprobamos si el curso existe
					if(!listaCursos.contains(curso))
					{
						String error = "Curso no encontrado";
						log.error(error);
						throw new HorarioException(12,error);
					}
					
					Reduccion reduccionObject = new Reduccion(linea[0], linea[1], Integer.parseInt(linea[2]), String.valueOf(curso.getCurso()), curso.getEtapa(), curso.getGrupo());
					listaReducciones.add(reduccionObject);
				}
				else
				{
					reduccion = new Reduccion(linea[0],linea[1],Integer.valueOf(linea[2]), null, null, null);
					listaReducciones.add(reduccion);
				}
				
				
			}
			return listaReducciones;
		}
		catch (IOException ioException) 
		{
			String error = "Error al realizar la lectura del fichero CSV";
			log.error(error,ioException);
			throw new HorarioException(11,error,ioException);
		}
		finally 
		{
			if(scanner!=null) 
			{
				scanner.close();			
			}
		}	
	}
	/**
	 * metodo para obtener el resumen de un profesor
	 * @param nombreDepartamento
	 * @param session
	 * @param numeroProfesorDepartamento
	 * @param totalHoras
	 * @return
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public Resumen resumenDepartamento(String nombreDepartamento, HttpSession session) throws HorarioException
	{
		 int numeroProfesorDepartamento=0;
		 int totalHoras =0;
		Map <String,List<ReduccionHoras>> mapaReduccion = (Map<String, List<ReduccionHoras>>) session.getAttribute("mapaReduccion");
		Map <String, List<Asignatura>> mapaAsignatura   = (Map<String, List<Asignatura>>) session.getAttribute("mapaAsignaturas");
		Map <String, Integer> mapaGuardias 				= (Map<String, Integer>) session.getAttribute("mapaGuardias");
		
		List<Profesor> listaProfesores = (List<Profesor>) session.getAttribute("listaProfesores");
		for(Profesor profesor : listaProfesores) 
		{
			//comprobamos si el profesor pertenece a ese departamento
			if(profesor.getDepartamento().equalsIgnoreCase(nombreDepartamento)) 
			{
				numeroProfesorDepartamento++;
				String profesorId=profesor.getIdProfesor();
				//comprobamos que el mapa de asignaturas existe para sumar horas
				if(mapaAsignatura!=null) 
				{
					totalHoras = totalHoras + this.obtenerHorasAsignaturas(mapaAsignatura, profesorId);
				}
				//comprobamos que el mapa de reducciones existe para sumar horas
				if(mapaReduccion!=null) 
				{
					totalHoras = totalHoras + this.obtenerHorasReduccion(mapaReduccion, profesorId);
				}
				//comprobamos que el mapa de guardias no es nulo y contiene la id del profesor
				if(mapaGuardias!=null && mapaGuardias.containsKey(profesorId)) 
				{
					totalHoras+=mapaGuardias.get(profesorId);
				}
			}
		}		
		
		int horasNecesarias = numeroProfesorDepartamento*18;
		int desfase         = totalHoras-horasNecesarias;
		
		String resultadoDesfase = "Cerrado" ;
		if(desfase>0) 
		{
			resultadoDesfase="Sobran horas";
		}
		else if(desfase<0) 
		{
			resultadoDesfase="Faltan horas";
		}
		
		return new Resumen(numeroProfesorDepartamento, horasNecesarias, totalHoras, desfase, resultadoDesfase);
	}
	/**
	 * metodo para obtener las horas de reduccion
	 * @param mapaReduccion
	 * @param profesorId
	 * @return
	 */
	private int obtenerHorasReduccion(Map<String, List<ReduccionHoras>> mapaReduccion, String profesorId)
	{
		int totalHoras = 0 ;
		
		List<ReduccionHoras> listaReducciones = mapaReduccion.get(profesorId);
		
		for (ReduccionHoras reduccionHoras : listaReducciones) 
		{
			totalHoras += reduccionHoras.getNumHoras();
		}
		
		return totalHoras;
	}
	/**
	 * metodo para obtener las horas de las asignaturas
	 * @param mapaAsignatura
	 * @param profesorId
	 * @return
	 */
	private int obtenerHorasAsignaturas(Map<String, List<Asignatura>> mapaAsignatura, String profesorId)
	{
		int totalHoras = 0 ;
		
		List<Asignatura> listaAsignaturas = mapaAsignatura.get(profesorId);
		
		for (Asignatura asignatura : listaAsignaturas) 
		{
			totalHoras += asignatura.getNumeroHorasSemanales();
		}
		
		return totalHoras;
	}
	/**
	 * metodo para hacer un resumen del profesor
	 * @param idProfesor
	 * @param session
	 * @return
	 */
//	@SuppressWarnings("unchecked")
//	public ResumenProfesor resumenProfesor(String idProfesor, HttpSession session)
//	{
//		int horasAsignaturas=0;
//		int horasReduccion=0;
//		Map <String,List<ReduccionHoras>> mapaReduccion = (Map<String, List<ReduccionHoras>>) session.getAttribute("mapaReduccion");
//		Map <String, List<Asignatura>> mapaAsignatura = (Map<String, List<Asignatura>>) session.getAttribute("mapaAsignaturas");
//			
//		if(mapaAsignatura.containsKey(idProfesor)) 
//		{
//			horasAsignaturas = this.obtenerHorasAsignaturas(mapaAsignatura, idProfesor);	
//		}
//		if(mapaReduccion.containsKey(idProfesor)) 
//		{
//			horasReduccion = this.obtenerHorasReduccion(mapaReduccion, idProfesor);
//		}
//		
//		int horasTotales=horasAsignaturas+horasReduccion;
//		ResumenProfesor resumen = new ResumenProfesor(horasAsignaturas, horasReduccion, horasTotales);
//		return resumen;
//	}
	/**
	 * metodo para hacer una reduccion
	 * @param idProfesor
	 * @param idReduccion
	 * @param session
	 * @param listaReducciones
	 * @param listaReduccionHoras
	 * @param reduccionEncontrada
	 * @param reduccionHoras
	 * @return
	 * @throws HorarioException 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<ReduccionHoras>> realizarReduccion(String idProfesor, String idReduccion,
			HttpSession session, List<Reduccion> listaReducciones, List<ReduccionHoras> listaReduccionHoras) throws HorarioException
	{
		ReduccionHoras reduccionHoras = new ReduccionHoras();
		boolean reduccionEncontrada=false;
		Map<String, List<ReduccionHoras>> asignacionReduccion;
		int i = 0;
		while(i< listaReducciones.size() && !reduccionEncontrada) 
		{
			if(listaReducciones.get(i).getIdReduccion().equalsIgnoreCase(idReduccion)) 
			{
				reduccionEncontrada=true;
				reduccionHoras.setIdReduccion(idReduccion);
				reduccionHoras.setNumHoras(listaReducciones.get(i).getNumeroHoras());
			}
			i++;
		}
		listaReduccionHoras.add(reduccionHoras);
		if(session.getAttribute("mapaReduccion") == null) 
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
		        
		        //comprobamos si la id de reduccion existe
		        i = 0;
		        while(i < existingReduccionHoras.size() && !idReduccionExists) 
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
	 * @param nombreAsignatura
	 * @param curso
	 * @param etapa
	 * @param grupo
	 * @param datosAsignacion
	 * @param listaAsignaturas
	 * @param listaCursos
	 * @param asignaturaObject
	 * @param asignaturaEncontrada
	 * @throws HorarioException
	 */
	public String comprobacionCreacionObjeto(String nombreAsignatura, Integer curso, String etapa, String grupo,
			List<Asignatura> datosAsignacion, List<Asignatura> listaAsignaturas, List<Curso> listaCursos,
			Asignatura asignaturaObject) throws HorarioException
	{
		boolean asignaturaEncontrada=false;
		boolean cursoExiste = false;
		String result="No se ha podido crear la asignacion";
		int i = 0;
		while(i < listaAsignaturas.size() && !cursoExiste) 
		{
			if(listaAsignaturas.get(i).getCurso()==curso && listaAsignaturas.get(i).getEtapa().equals(etapa) && listaAsignaturas.get(i).getGrupo().equals(grupo) && listaAsignaturas.get(i).getNombreAsinatura().equalsIgnoreCase(nombreAsignatura) ) 
			{
				cursoExiste = true;
				asignaturaEncontrada=true;
				asignaturaObject.setDepartamento(listaAsignaturas.get(i).getDepartamento());
				asignaturaObject.setNumeroHorasSemanales(listaAsignaturas.get(i).getNumeroHorasSemanales());
			}
			i++;
		}
		if(asignaturaEncontrada && cursoExiste) 
		{
			asignaturaObject.setCurso(curso);
			asignaturaObject.setEtapa(etapa);
			asignaturaObject.setGrupo(grupo);
			result="asignacion creada correctamente";
			datosAsignacion.add(asignaturaObject);
		}
		return result;
	}
	/**
	 * metodo para comprobar el curso
	 * @param listaCursos
	 * @param cursoExiste
	 * @param cursoAsignacion
	 * @throws HorarioException
	 */
	public void comprobarCurso(List<Curso> listaCursos, boolean cursoExiste, Curso cursoAsignacion)
			throws HorarioException 
	{
		int i = 0;
		while(i< listaCursos.size() && !cursoExiste) 
		{
			if(listaCursos.get(i).equals(cursoAsignacion))
			{
				cursoExiste = true;
			}
			i++;
		}
		if(!cursoExiste)
		{
			String error = "Curso no encontrado";
			log.info(error);
			throw new HorarioException(13,error);
		}
	}
	
	/**
	 * metodo para comprobar el id del profesor
	 * @param idProfesor
	 * @param listaProfesores
	 * @throws HorarioException
	 */
	public void comprobarIdProfesor(String idProfesor, List<Profesor> listaProfesores)throws HorarioException 
	{
		boolean idProfesorExiste = false;
		int i = 0;
		while(i < listaProfesores.size() && !idProfesorExiste)
		{
			if(listaProfesores.get(i).getIdProfesor().equals(idProfesor)) 
			{
				idProfesorExiste = true;
			}
			i++;
		}
		if(!idProfesorExiste)
		{
			String error = "Profesor no encontrado";
			log.info(error);
			throw new HorarioException(13,error);
		}
	}
	/**
	 * metodo para comprobar que la lista de departamentos existe
	 * @param session
	 * @param listaDepartamentos
	 * @return
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public List<Departamento> comprobarListaDepartamentos(HttpSession session, List<Departamento> listaDepartamentos)
			throws HorarioException 
	{
		if(session.getAttribute("listaDepartamentos")!=null)
		{
			listaDepartamentos = (List<Departamento>) session.getAttribute("listaDepartamentos");
		}
		else 
		{
			String error = "Los departamentos no han sido cargados en sesion todavía";
			throw new HorarioException(1,error);
		}
		return listaDepartamentos;
	}

	/**
	 * metodo para comprobar si la lista de cursos existe
	 * @param session
	 * @param listaCursos
	 * @return
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public List<Curso> comprobarListCursos(HttpSession session, List<Curso> listaCursos) throws HorarioException
	{
		if(session.getAttribute("listaCursos")!=null)
		{
			listaCursos = (List<Curso>) session.getAttribute("listaCursos");
		}
		else
		{
			String error = "Los cursos no han sido cargados en sesion todavía";
			throw new HorarioException(1,error);
		}
		return listaCursos;
	}
	
	/**
	 * metodo para comprobar la lista de profesores
	 * @param session
	 * @param listaProfesores
	 * @return
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public List<Profesor> comprobarListaProfesores(HttpSession session, List<Profesor> listaProfesores)
			throws HorarioException {
		if(session.getAttribute("listaProfesores")!=null)
		{
			listaProfesores = (List<Profesor>) session.getAttribute("listaProfesores");
		}
		else 
		{
			String error = "Los profesores no han sido cargados en sesion todavía";
			throw new HorarioException(1,error);
		}
		return listaProfesores;
	}
	/**
	 * metodo para comprobar la lista de asignaturas
	 * @param session
	 * @param listaAsignaturas
	 * @return
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public List<Asignatura> comprobarListaAsignaturas(HttpSession session, List<Asignatura> listaAsignaturas)
			throws HorarioException {
		if(session.getAttribute("listaAsignaturas")!=null)
		{
			listaAsignaturas = (List<Asignatura>) session.getAttribute("listaAsignaturas");
		}
		else
		{
			String error = "Las asignatura no han sido cargados en sesion todavía";
			throw new HorarioException(1,error);
		}
		return listaAsignaturas;
	}
	
	/**
	 * metodo para comprobar la lista de reducciones
	 * @param session
	 * @param listaReducciones
	 * @return
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public List<Reduccion> comprobarListaReducciones(HttpSession session, List<Reduccion> listaReducciones)
			throws HorarioException {
		if(session.getAttribute("listaReducciones")!=null)
		{
			listaReducciones = (List<Reduccion>) session.getAttribute("listaReducciones");
		}
		else 
		{
			String error = "Las reducciones no han sido cargados en sesion todavía";
			throw new HorarioException(1,error);
		}
		return listaReducciones;
	}
	
	/**
	 * metodo para comprobar si el departamento existe 
	 * @param listaDepartamentos
	 * @param departamento
	 * @throws HorarioException
	 */
	public void comprobarDepartamentoExiste(List<Departamento> listaDepartamentos, Departamento departamento)
			throws HorarioException {
		if(!listaDepartamentos.contains(departamento))
		{
			String error = "Departamento no encontrado";
			log.info(error);
			throw new HorarioException(13,error);
		}
	}
	
	/**
	 * metodo para comprobar si la id reduccion existe
	 * @param idReduccion
	 * @param listaReducciones
	 * @throws HorarioException
	 */
	public void comprobarIdReduccionExiste(String idReduccion, List<Reduccion> listaReducciones
		) throws HorarioException 
	{
		boolean idReduccionExiste = false;
		int i = 0;
		while(i < listaReducciones.size() && !idReduccionExiste) 
		{
			if(listaReducciones.get(i).getIdReduccion().equals(idReduccion))
			{
				idReduccionExiste = true;
			}
			i++;
		}
		if(!idReduccionExiste)
		{
			String error = "Reduccion no encontrada";
			log.info(error);
			throw new HorarioException(13,error);
		}
	}
	/**
	 * metodo para comprobar si el nombre de asignatura existe
	 * @param nombreAsignatura
	 * @param listaAsignaturas
	 * @param asignaturaExiste
	 * @throws HorarioException
	 */
	public void comprobarNombreAsignaturaExiste(String nombreAsignatura, List<Asignatura> listaAsignaturas) throws HorarioException
	{
		boolean asignaturaExiste = false;
		int i = 0;
		while(i < listaAsignaturas.size() && !asignaturaExiste) 
		{
			if(listaAsignaturas.get(i).getNombreAsinatura().equalsIgnoreCase(nombreAsignatura))
			{
				asignaturaExiste = true;
			}
			i++;
		}
		if(!asignaturaExiste)
		{
			String error = "Asignatura no encontrada";
			log.info(error);
			throw new HorarioException(13,error);
		}
	}
	/**
	 * metodo para obtener las horas de reduccion y de asignaturas de un profesor
	 * @param idProfesor
	 * @param totalHoras
	 * @param mapaReduccion
	 * @param mapaAsignatura
	 * @param listaAsignaturaProfesor
	 * @param listaReduccionHoras
	 * @return
	 */
	public ResumenProfesor obtencionHorasProfesor(String idProfesor,
			Map<String, List<ReduccionHoras>> mapaReduccion, Map<String, List<Asignatura>> mapaAsignatura)
	{
		int totalHoras = 0 ;
		List<Asignatura> listaAsignaturaProfesor = new ArrayList<Asignatura>();
		List<ReduccionHoras> listaReduccionHoras = new ArrayList<ReduccionHoras>();
		if(mapaAsignatura.containsKey(idProfesor)) 
		{
			List<Asignatura> listaAsignaturas = mapaAsignatura.get(idProfesor);
			
			for (Asignatura asignatura : listaAsignaturas) 
			{
				Asignatura asignatura2 = asignatura;
				listaAsignaturaProfesor.add(asignatura2);
				totalHoras += asignatura.getNumeroHorasSemanales();
			}
		}
		if(mapaReduccion.containsKey(idProfesor)) 
		{
			
			List<ReduccionHoras> listaReducciones = mapaReduccion.get(idProfesor);
			
			for (ReduccionHoras reduccionHoras : listaReducciones) 
			{
				ReduccionHoras reduccionHoras2 = reduccionHoras;
				listaReduccionHoras.add(reduccionHoras2);
				totalHoras += reduccionHoras.getNumHoras();
			}
		}
		
		ResumenProfesor resumen = new ResumenProfesor(listaAsignaturaProfesor,listaReduccionHoras,totalHoras);
		return resumen;
	}
	/**
	 * metodo para comprobar si existe el mapa de reduccion
	 * @param session
	 * @param mapaReduccion
	 * @return
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<ReduccionHoras>> comprobarMapaReduccion(HttpSession session,
			Map<String, List<ReduccionHoras>> mapaReduccion) throws HorarioException 
	{
		if(session.getAttribute("mapaReduccion")!=null) 
		{
			mapaReduccion = (Map<String, List<ReduccionHoras>>) session.getAttribute("mapaReduccion");
		}
		else 
		{
			String error = "No se ha realizado reducciones todavía";
			throw new HorarioException(1,error);
		}
		return mapaReduccion;
	}
	/**
	 * metodo para comprobar si existe el mapa asignaturas
	 * @param session
	 * @param mapaAsignatura
	 * @return
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<Asignatura>> comprobarMapaAsignaturas(HttpSession session,
			Map<String, List<Asignatura>> mapaAsignatura) throws HorarioException 
	{
		if(session.getAttribute("mapaAsignaturas")!=null) 
		{
			mapaAsignatura = (Map<String, List<Asignatura>>) session.getAttribute("mapaAsignaturas");
		}
		else 
		{
			String error = "No se ha realizado asignacion de asignaturas todavía";
			throw new HorarioException(1,error);
		}
		return mapaAsignatura;
	}
	/**
	 * metodo para parsear el fichero matriculaCursos 
	 * @param csvFile
	 * @param curso
	 * @param etapa
	 * @param mapaAsignaturas
	 * @param session
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
	        String contenido = new String(csvFile.getBytes());
	        scanner = new Scanner(contenido);
	        String clave = curso + etapa; 
	        String[] linea = scanner.nextLine().split(",");
	        List<Asignatura> listaAsignatura = (List<Asignatura>) session.getAttribute("listaAsignaturas");
	        comprobarListaAsignaturas(session, listaAsignatura);
	        List<String> listaNombreAsignaturas = new ArrayList<String>();
	        String asignatura1 = linea[1];
	        String asignatura2 = linea[2];
	        String asignatura3 = linea[3];
	        listaNombreAsignaturas.add(asignatura1);
	        listaNombreAsignaturas.add(asignatura2);
	        listaNombreAsignaturas.add(asignatura3);
	        //recorrer mediante for ambas listas y comprobar si existe la asignatura
	        // Mapa para almacenar nombres completos de alumnos
	        Map<String, String> mapaNombres = new HashMap<>();

	        while (scanner.hasNext()) 
	        {
	            List<String> listaAsignaturas = new ArrayList<>();
	            // Separamos la línea por comas
	            linea = scanner.nextLine().split(",");

	            String apellidos = linea[0];
	            String nombre = linea[1];

	            String uno = linea[2];
	            String dos = linea[3];
	            String tres = linea[4];

	            if (uno.equalsIgnoreCase("MATR")) 
	            {
	                listaAsignaturas.add(asignatura1);
	            }
	            if (dos.equalsIgnoreCase("MATR")) 
	            {
	                listaAsignaturas.add(asignatura2);
	            }
	            if (tres.equalsIgnoreCase("MATR")) 
	            {
	                listaAsignaturas.add(asignatura3);
	            }

	            mapaAsignaturas.put(apellidos, listaAsignaturas);
	            mapaNombres.put(apellidos, nombre);
	        }

	        session.setAttribute("mapaAsignaturasCursos", mapaAsignaturas);
	        session.setAttribute("mapaNombres", mapaNombres);
	        return clave;
	    }
	    catch(IOException ioException) 
	    {
	        String error = "Error al realizar la lectura del fichero CSV";
	        log.error(error, ioException);
	        throw new HorarioException(11, error, ioException);
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
	 * metodo para comprobar si existe el mapa cursos
	 * @param session
	 * @param mapaCursos
	 * @return
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Map<String, List<String>>> comprobarMapaCursosExiste(HttpSession session,
			Map<String, Map<String, List<String>>> mapaCursos) throws HorarioException
	{
		if(session.getAttribute("mapaCursos")!=null) 
		{
			mapaCursos=(Map<String, Map<String, List<String>>>) session.getAttribute("mapaCursos");
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
	 * @param session
	 * @param mapaBloques
	 * @return
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<String>> comprobarMapaBloquesExiste(HttpSession session,
			Map<String, List<String>> mapaBloques) throws HorarioException
	{
		if(session.getAttribute("mapaBloques")!=null) 
		{
			mapaBloques = (Map<String, List<String>>) session.getAttribute("mapaBloques");
		}
		else 
		{
			String error = "No se ha realizado asignacion de bloques todavía";
			throw new HorarioException(1,error);
		}
		return mapaBloques;
	}
}
