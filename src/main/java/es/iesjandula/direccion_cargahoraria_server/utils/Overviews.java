package es.iesjandula.direccion_cargahoraria_server.utils;

import java.util.List;
import java.util.Map;

import es.iesjandula.direccion_cargahoraria_server.exception.HorarioException;
import es.iesjandula.direccion_cargahoraria_server.models.Asignatura;
import es.iesjandula.direccion_cargahoraria_server.models.Profesor;
import es.iesjandula.direccion_cargahoraria_server.models.ReduccionHoras;
import es.iesjandula.direccion_cargahoraria_server.models.Resumen;
import jakarta.servlet.http.HttpSession;

public class Overviews
{
	/**
	 * método para obtener las horas de reduccion
	 * 
	 * @param mapaReduccion mapa de reducciones
	 * @param profesorId id del profesor
	 * @return
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
	 * método para obtener las horas de las asignaturas
	 * 
	 * @param mapaAsignatura mapa de las asignaturas
	 * @param profesorId id del profesor
	 * @return
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
	 * 
	 * @param nombreDepartamento Nombre del departamento
	 * @param session Utilizado para obtener o guardar cosas en sesión
	 * @return Resumen del departamento
	 * @throws HorarioException
	 */
	@SuppressWarnings("unchecked")
	public Resumen resumenDepartamento(String nombreDepartamento, HttpSession session) throws HorarioException 
	{
		Overviews overviews = new Overviews() ;
		int numeroProfesorDepartamento = 0;
		int totalHoras = 0;
		Map<String, List<ReduccionHoras>> mapaReduccion = (Map<String, List<ReduccionHoras>>) session.getAttribute("mapaReduccion");
		Map<String, List<Asignatura>> mapaAsignatura = (Map<String, List<Asignatura>>) session.getAttribute("mapaAsignaturas");
		Map<String, Integer> mapaGuardias = (Map<String, Integer>) session.getAttribute("mapaGuardias");

		List<Profesor> listaProfesores = (List<Profesor>) session.getAttribute("listaProfesores"); 
		Parse parse = new Parse();
		parse.obtenerListaProfesores(session, listaProfesores);
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
					totalHoras = totalHoras + overviews.obtenerHorasAsignaturas(mapaAsignatura, profesorId);
				}
				
				// comprobamos que el mapa de reducciones existe para sumar horas
				if (mapaReduccion != null)
				{
					totalHoras = totalHoras + overviews.obtenerHorasReduccion(mapaReduccion, profesorId);
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
		// Obtenemos el desfase
		String resultadoDesfase = obtenerDesfase(desfase);

		return new Resumen(numeroProfesorDepartamento, horasNecesarias, totalHoras, desfase, resultadoDesfase);
	}
	
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
}
