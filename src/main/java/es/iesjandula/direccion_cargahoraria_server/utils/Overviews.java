package es.iesjandula.direccion_cargahoraria_server.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.iesjandula.direccion_cargahoraria_server.exception.HorarioException;
import es.iesjandula.direccion_cargahoraria_server.models.Asignatura;
import es.iesjandula.direccion_cargahoraria_server.models.Profesor;
import es.iesjandula.direccion_cargahoraria_server.models.ReduccionHoras;
import es.iesjandula.direccion_cargahoraria_server.models.Resumen;
import es.iesjandula.direccion_cargahoraria_server.models.ResumenProfesor;
import jakarta.servlet.http.HttpSession;
/**
 * Clase overviews
 */
public class Overviews
{
	/**
	 * Método para obtener las horas de reduccion
	 * 
	 * @param mapaReduccion Mapa de reducciones
	 * @param profesorId Id del profesor
	 * @return Se devolvera el total de horas de reducción
	 */
	public int obtenerHorasReduccion(Map<String, List<ReduccionHoras>> mapaReduccion, String profesorId) 
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
	 * Método para obtener las horas de las asignaturas
	 * 
	 * @param mapaAsignatura Mapa de las asignaturas
	 * @param profesorId Id del profesor
	 * @return Se devolvera el total de horas de asignaturas
	 */
	public int obtenerHorasAsignaturas(Map<String, List<Asignatura>> mapaAsignatura, String profesorId) 
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
	 * Método para realizar el resumen del departamento
	 * 
	 * @param nombreDepartamento Nombre del departamento
	 * @param session Utilizado para obtener o guardar cosas en sesión
	 * @return Resumen del departamento
	 * @throws HorarioException Se lanza si la lista es nula
	 */
	@SuppressWarnings("unchecked")
	public Resumen resumenDepartamento(String nombreDepartamento, HttpSession session) throws HorarioException 
	{
		int numeroProfesorDepartamento = 0;
		int totalHoras = 0;
		Map<String, List<ReduccionHoras>> mapaReduccion = (Map<String, List<ReduccionHoras>>) session.getAttribute(Constants.SESION_MAPA_REDUCCIONES);
		Map<String, List<Asignatura>> mapaAsignatura = (Map<String, List<Asignatura>>) session.getAttribute(Constants.SESION_MAPA_ASIGNATURAS);
		Map<String, Integer> mapaGuardias = (Map<String, Integer>) session.getAttribute(Constants.SESION_MAPA_GUARDIAS);

		Validations validations = new Validations();
		
		List<Profesor> listaProfesores = validations.obtenerListaProfesores(session);
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
					totalHoras = totalHoras + mapaGuardias.get(profesorId);
				}
			}
		}

		int horasNecesarias = numeroProfesorDepartamento * 18;
		int desfase = totalHoras - horasNecesarias;
		
		// Obtenemos el desfase
		String resultadoDesfase = obtenerDesfase(desfase);

		return new Resumen(numeroProfesorDepartamento, horasNecesarias, totalHoras, desfase, resultadoDesfase);
	}
	/**
	 * Método para obtener el desfase
	 * 
	 * @param desfase Numero de horas
	 * @return Si sobran horas, faltan horas o cerrado
	 */
	public String obtenerDesfase(int desfase)
	{
		String resultadoDesfase = "Cerrado";
		if (desfase > 0)
		{
			resultadoDesfase = "Sobran horas";
		} 
		else if (desfase < 0)
		{
			resultadoDesfase = "Faltan horas";
		}
		
		return resultadoDesfase;
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
			// Obtenemos la lista de asignaturas del profesor
			List<Asignatura> listaAsignaturas = mapaAsignatura.get(idProfesor);
			
			for (Asignatura asignatura : listaAsignaturas)
			{
				listaAsignaturaProfesor.add(asignatura);
				totalHoras += asignatura.getNumeroHorasSemanales();
			}
		}
		if (mapaReduccion.containsKey(idProfesor))
		{
			// Obtenemos la lista de reducciones del profesor
			List<ReduccionHoras> listaReducciones = mapaReduccion.get(idProfesor);

			for (ReduccionHoras reduccionHoras : listaReducciones)
			{
				listaReduccionHoras.add(reduccionHoras);
				totalHoras += reduccionHoras.getNumHoras();
			}
		}

		return new ResumenProfesor(listaAsignaturaProfesor, listaReduccionHoras, totalHoras) ;
	}
}
