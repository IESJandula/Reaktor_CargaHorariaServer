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
import es.iesjandula.direccion_cargahoraria_server.models.Guardia;
import es.iesjandula.direccion_cargahoraria_server.models.Profesor;
import es.iesjandula.direccion_cargahoraria_server.models.Reduccion;
import es.iesjandula.direccion_cargahoraria_server.models.ResumenProfesor;
import es.iesjandula.direccion_cargahoraria_server.utils.Parse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@RequestMapping(value="/carga_horaria")
@RestController
@Log4j2
public class RestHandler 
{

	/**
	 * 
	 * @param csvFile
	 * @param session
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST,value="/departamentos",consumes="multipart/form-data")
	public ResponseEntity<?> uploadDepartamentos(@RequestParam(value="csv",required=true)MultipartFile csvFile,HttpSession session)
	{
		try
		{
			List<Departamento> listaDepartamentos = Parse.parseDepartamentos(csvFile);
			session.setAttribute("listaDepartamentos", listaDepartamentos);
			log.info(listaDepartamentos.toString());
			return ResponseEntity.ok("Departamentos subidos correctamente");
		}catch(HorarioException horarioException)
		{
			String error = "Error de parseo";
			log.error(error,horarioException.getMessage());
			return ResponseEntity.status(410).body(error);
		}catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(error);
		}
	}
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,value="/departamentos")
	public ResponseEntity<?> consultaDepartamentos(HttpSession session)
	{
		try
		{
			List<Departamento> listaDepartamentos;
			if(session.getAttribute("listaDepartamentos")!=null)
			{
				listaDepartamentos = (List<Departamento>) session.getAttribute("listaDepartamentos");
			}else {
				String error = "Los departamentos no han sido cargados en sesion todavía";
				throw new HorarioException(1,error);
			}

			log.info(listaDepartamentos.toString());
			return ResponseEntity.ok(listaDepartamentos.toString());
		}catch(HorarioException horarioException)
		{
			String error = "Error de carga de datos";
			log.error(error,horarioException.getMessage());
			return ResponseEntity.status(410).body(horarioException.getBodyExceptionMessage());
			
		}catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(error);
		}
	}
	
	/**
	 * 
	 * @param csvFile
	 * @param session
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST,value="/cursos",consumes="multipart/form-data")
	public ResponseEntity<?> uploadCursos(@RequestParam(value="csv",required=true)MultipartFile csvFile,HttpSession session)
	{
		try
		{
			List<Curso> listaCursos = Parse.parseCursos(csvFile);
			session.setAttribute("listaCursos", listaCursos);
			log.info(listaCursos.toString());
			return ResponseEntity.ok("Cursos subidos correctamente");
		}catch(HorarioException horarioException)
		{
			String error = "Error de parseo";
			log.error(error,horarioException.getMessage());
			return ResponseEntity.status(410).body(error);
		}catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(exception.getMessage());
		}
	}
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,value="/cursos")
	public ResponseEntity<?> consultaCursos(HttpSession session)
	{
		try
		{
			List<Curso> listaCursos;
			if(session.getAttribute("listaCursos")!=null)
			{
				listaCursos = (List<Curso>) session.getAttribute("listaCursos");
			}else {
				String error = "Los cursos no han sido cargados en sesion todavía";
				throw new HorarioException(1,error);
			}

			log.info(listaCursos.toString());
			return ResponseEntity.ok(listaCursos.toString());
		}catch(HorarioException horarioException)
		{
			String error = "Error de carga de datos";
			log.error(error,horarioException.getMessage());
			return ResponseEntity.status(410).body(horarioException.getBodyExceptionMessage());
			
		}catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(error);
		}
	}
	
	/**
	 * 
	 * @param csvFile
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.POST,value="/profesores",consumes="multipart/form-data")
	public ResponseEntity<?> uploadProfesores(@RequestParam(value="csv",required=true)MultipartFile csvFile,HttpSession session)
	{
		try
		{
			if(session.getAttribute("listaDepartamentos")==null)
			{
				String error = "Los departamentos no han sido cargados";
				throw new HorarioException(2,error);
			}
			List<Departamento> listaDepartamentos = (List<Departamento>) session.getAttribute("listaDepartamentos");
			List<Profesor> listaProfesores = Parse.parseProfesores(csvFile,listaDepartamentos);
			session.setAttribute("listaProfesores", listaProfesores);
			log.info(listaProfesores.toString());
			return ResponseEntity.ok("Profesores subidos correctamente");
		}catch(HorarioException horarioException)
		{
			String error = "Error de parseo";
			log.error(error,horarioException.getMessage());
			return ResponseEntity.status(410).body(error);
		}catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(error);
		}
	}
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,value="/profesores")
	public ResponseEntity<?> consultaProfesores(HttpSession session)
	{
		try
		{
			List<Profesor> listaProfesores;
			if(session.getAttribute("listaProfesores")!=null)
			{
				listaProfesores = (List<Profesor>) session.getAttribute("listaProfesores");
			}else {
				String error = "Los profesores no han sido cargados en sesion todavía";
				throw new HorarioException(1,error);
			}

			log.info(listaProfesores.toString());
			return ResponseEntity.ok(listaProfesores.toString());
		}catch(HorarioException horarioException)
		{
			String error = "Error de carga de datos";
			log.error(error,horarioException.getMessage());
			return ResponseEntity.status(410).body(horarioException.getBodyExceptionMessage());
			
		}catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(exception.getMessage());
		}
	}
	
	/**
	 * 
	 * @param csvFile
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.POST,value="/asignaturas",consumes="multipart/form-data")
	public ResponseEntity<?> uploadAsignaturas(@RequestParam(value="csv",required=true)MultipartFile csvFile,HttpSession session)
	{
		try
		{
			if(session.getAttribute("listaDepartamentos")==null)
			{
				String error = "Los departamentos no han sido cargados";
				throw new HorarioException(2,error);
			}
			List<Departamento> listaDepartamentos = (List<Departamento>) session.getAttribute("listaDepartamentos");
			
			if(session.getAttribute("listaCursos")==null)
			{
				String error = "Los cursos no han sido cargados";
				throw new HorarioException(2,error);
			}
			List<Curso> listaCursos = (List<Curso>) session.getAttribute("listaCursos");
			
			
			List<Asignatura> listaAsignaturas = Parse.parseAsignaturas(csvFile,listaCursos,listaDepartamentos);
			session.setAttribute("listaAsignaturas", listaAsignaturas);
			log.info(listaAsignaturas.toString());
			return ResponseEntity.ok("Asignaturas subidas correctamente");
		}catch(HorarioException horarioException)
		{
			return ResponseEntity.status(410).body(horarioException.getMessage());
		}catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(error);
		}
	}
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,value="/asignaturas")
	public ResponseEntity<?> consultaAsignaturas(HttpSession session)
	{
		try
		{
			List<Asignatura> listaAsignaturas;
			if(session.getAttribute("listaAsignaturas")!=null)
			{
				listaAsignaturas = (List<Asignatura>) session.getAttribute("listaAsignaturas");
			}else {
				String error = "Las asignatura no han sido cargados en sesion todavía";
				throw new HorarioException(1,error);
			}

			log.info(listaAsignaturas.toString());
			return ResponseEntity.ok(listaAsignaturas.toString());
		}catch(HorarioException horarioException)
		{
			String error = "Error de carga de datos";
			log.error(error,horarioException.getMessage());
			return ResponseEntity.status(410).body(horarioException.getBodyExceptionMessage());
			
		}catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(error);
		}
	}
	
	/**
	 * 
	 * @param idProfesor
	 * @param nombreAsignatura
	 * @param curso
	 * @param etapa
	 * @param grupo
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	@RequestMapping(method=RequestMethod.PUT,value="/asignaturas")
	public ResponseEntity<?> asignacionAsignaturas(@RequestHeader(value="idProfesor",required=true)String idProfesor,
			@RequestHeader(value="nombreAsignatura",required=true)String nombreAsignatura,
			@RequestHeader(value="curso",required=true)String curso,
			@RequestHeader(value="etapa",required=true)String etapa,
			@RequestHeader(value="grupo",required=true)String grupo,HttpSession session)
	{
		try
		{
			Map<String,List<Object>> asignacion = new TreeMap<>();	
			List<Object> datosAsignacion = new ArrayList<>();
			
			List<Asignatura> listaAsignaturas = (List<Asignatura>) session.getAttribute("listaAsignaturas");
			List<Curso> listaCursos = (List<Curso>) session.getAttribute("listaCursos");
			
			boolean asignaturaExiste = false;
			for(Asignatura asignatura : listaAsignaturas)
			{
				if(asignatura.getNombreAsignatura().equals(nombreAsignatura))
				{
					asignaturaExiste = true;
				}
			}
			if(!asignaturaExiste)
			{
				String error = "Asignatura no encontrada";
				log.info(error);
				throw new HorarioException(13,error);
			}
			datosAsignacion.add(nombreAsignatura);
			
			boolean cursoExiste = false;
			Curso cursoAsignacion = new Curso(curso,etapa,grupo);
			for(Curso cursoComprobante : listaCursos)
			{
				if(cursoComprobante.equals(cursoAsignacion))
				{
					cursoExiste = true;
				}
			}
			if(!cursoExiste)
			{
				String error = "Curso no encontrado";
				log.info(error);
				throw new HorarioException(13,error);
			}
			datosAsignacion.add(cursoAsignacion);
			
			asignacion.put(idProfesor, datosAsignacion);
			
			List<Map<String,List<Object>>> listaAsignaturasAsignadas;
			if(session.getAttribute("listaAsignaciones")==null)
			{
				listaAsignaturasAsignadas = new ArrayList<>();
			}else
			{
				listaAsignaturasAsignadas = (List<Map<String, List<Object>>>) session.getAttribute("listaAsignaciones");
			}
			
			listaAsignaturasAsignadas.add(asignacion);
			
			session.setAttribute("listaAsignaturasAsignadas", listaAsignaturasAsignadas);
			
			log.info(listaAsignaturasAsignadas.toString());
			return ResponseEntity.ok("Asignacion creada correctamente");
		}catch(HorarioException horarioException)
		{
			return ResponseEntity.status(410).body(horarioException.getMessage());
		}catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(exception.getMessage());
		}
	}
	
	/**
	 * 
	 * @param csvFile
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.POST,value="/reducciones",consumes="multipart/form-data")
	public ResponseEntity<?> uploadReducciones(@RequestParam(value="csv",required=true)MultipartFile csvFile,HttpSession session)
	{
		try
		{
			if(session.getAttribute("listaCursos")==null)
			{
				String error = "Los cursos no han sido cargados";
				throw new HorarioException(2,error);
			}
			List<Curso> listaCursos = (List<Curso>) session.getAttribute("listaCursos");
			
			
			List<Reduccion> listaReducciones = Parse.parseReducciones(csvFile,listaCursos);
			session.setAttribute("listaReducciones", listaReducciones);
			log.info(listaReducciones.toString());
			return ResponseEntity.ok("Reducciones subidas correctamente");
		}catch(HorarioException horarioException)
		{
			return ResponseEntity.status(410).body(horarioException.getMessage());
		}catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(exception.getMessage());
		}
	}
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,value="/reducciones")
	public ResponseEntity<?> consultaReducciones(HttpSession session)
	{
		try
		{
			List<Reduccion> listaReducciones;
			if(session.getAttribute("listaReducciones")!=null)
			{
				listaReducciones = (List<Reduccion>) session.getAttribute("listaReducciones");
			}else {
				String error = "Las reducciones no han sido cargados en sesion todavía";
				throw new HorarioException(1,error);
			}

			log.info(listaReducciones.toString());
			return ResponseEntity.ok(listaReducciones.toString());
		}catch(HorarioException horarioException)
		{
			String error = "Error de carga de datos";
			log.error(error,horarioException.getMessage());
			return ResponseEntity.status(410).body(horarioException.getBodyExceptionMessage());
			
		}catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(error);
		}
	}
	
	/**
	 * 
	 * @param idProfesor
	 * @param idReduccion
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	@RequestMapping(method=RequestMethod.PUT,value="/reducciones")
	public ResponseEntity<?> asignacionReducciones(@RequestHeader(value="idProfesor",required=true)String idProfesor,
			@RequestHeader(value="idReduccion",required=true)String idReduccion,HttpSession session)
	{
		try
		{
			Map<String,String> asignacionReduccion = new TreeMap<>();	
			
			List<Profesor> listaProfesores = (List<Profesor>) session.getAttribute("listaProfesores");
			List<Reduccion> listaReducciones = (List<Reduccion>) session.getAttribute("listaReducciones");
			
			boolean idProfesorExiste = false;
			for(Profesor profesor : listaProfesores)
			{
				if(profesor.getIdProfesor().equals(idProfesor))
				{
					idProfesorExiste = true;
				}
			}
			if(!idProfesorExiste)
			{
				String error = "Profesor no encontrado";
				log.info(error);
				throw new HorarioException(13,error);
			}
			
			boolean idReduccionExiste = false;
			for(Reduccion reduccion : listaReducciones)
			{
				if(reduccion.getIdReduccion().equals(idReduccion))
				{
					idReduccionExiste = true;
				}
			}
			if(!idReduccionExiste)
			{
				String error = "Reduccion no encontrada";
				log.info(error);
				throw new HorarioException(13,error);
			}
			
			asignacionReduccion.put(idProfesor, idReduccion);
			
			List<Map<String,String>> listaReduccionesAsignadas;
			if(session.getAttribute("listaReduccionesAsignadas")==null)
			{
				listaReduccionesAsignadas = new ArrayList<>();
			}else
			{
				listaReduccionesAsignadas =  (List<Map<String, String>>) session.getAttribute("listaReduccionesAsignadas");
			}
			
			listaReduccionesAsignadas.add(asignacionReduccion);
			
			session.setAttribute("listaReduccionesAsignadas", listaReduccionesAsignadas);
			
			log.info(listaReduccionesAsignadas.toString());
			return ResponseEntity.ok("Asignacion de reduccion creada correctamente");
		}catch(HorarioException horarioException)
		{
			return ResponseEntity.status(410).body(horarioException.getMessage());
		}catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(exception.getMessage());
		}
	}
	
	/**
	 * 
	 * @param idProfesor
	 * @param horasAsignadas
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.POST,value="/guardias")
	public ResponseEntity<?> uploadGuardias(@RequestHeader(value="idProfesor",required=true)String idProfesor,
			@RequestHeader(value="horasAsignadas",required=true)String horasAsignadas,HttpSession session)
	{
		try
		{
			List<Profesor> listaProfesores = (List<Profesor>) session.getAttribute("listaProfesores");
			boolean idProfesorExiste = false;
			for(Profesor profesor : listaProfesores)
			{
				if(profesor.getIdProfesor().equals(idProfesor))
				{
					idProfesorExiste = true;
				}
			}
			if(!idProfesorExiste)
			{
				String error = "Profesor no encontrado";
				log.info(error);
				throw new HorarioException(13,error);
			}
			
			Guardia guardia = new Guardia(idProfesor,horasAsignadas);
			List<Guardia> listaGuardias;
			
			if(session.getAttribute("listaGuardias")==null)
			{
				listaGuardias = new ArrayList<>();
			}else
			{
				listaGuardias =  (List<Guardia>) session.getAttribute("listaGuardias");
			}
			
			listaGuardias.add(guardia);
			
			session.setAttribute("listaGuardias", listaGuardias);
			
			log.info(listaGuardias.toString());
			return ResponseEntity.ok("Guardia subida correctamente");
		}catch(HorarioException horarioException)
		{
			return ResponseEntity.status(410).body(horarioException.getMessage());
		}catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(exception.getMessage());
		}
	}
	
	/**
	 * 
	 * @param idProfesor
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,value="/profesores/resumen")
	public ResponseEntity<?> resumenProfesores(@RequestHeader(value="idProfesor",required=true)String idProfesor,HttpSession session)
	{
		try
		{
			List<Profesor> listaProfesores = (List<Profesor>) session.getAttribute("listaProfesores");
			boolean idProfesorExiste = false;
			for(Profesor profesor : listaProfesores)
			{
				if(profesor.getIdProfesor().equals(idProfesor))
				{
					idProfesorExiste = true;
				}
			}
			if(!idProfesorExiste)
			{
				String error = "Profesor no encontrado";
				log.info(error);
				throw new HorarioException(13,error);
			}
			
			List<Asignatura> listaAsignaturas = (List<Asignatura>) session.getAttribute("listaAsignaturas");
			List<Reduccion> listaReducciones = (List<Reduccion>) session.getAttribute("listaReducciones");
			int horasAsignaturas = 0;
			for(Asignatura asignatura:listaAsignaturas)
			{
				horasAsignaturas+= asignatura.getNumeroHorasSemanales();
			}
			int horasReducciones = 0;
			for(Reduccion reduccion:listaReducciones)
			{
				horasReducciones+= reduccion.getNumeroHoras();
			}
			int horasTotales = horasAsignaturas+horasReducciones;
			ResumenProfesor resumenProfesor = new ResumenProfesor(listaAsignaturas,listaReducciones,horasTotales);

			
			log.info(resumenProfesor.toString());
			return ResponseEntity.ok("Datos del profesor "+idProfesor+": \n"+ resumenProfesor.toString());
		}catch(HorarioException horarioException)
		{
			return ResponseEntity.status(410).body(horarioException.getMessage());
		}catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(exception.getMessage());
		}
	}
	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(method=RequestMethod.GET,value="/departamentos/resumen")
//	public ResponseEntity<?> resumenDepartamento(@RequestHeader(value="nombreDepartamento",required=true)String nombreDepartamento,HttpSession session)
//	{
//		try
//		{
//			List<Departamento> listaDepartamentos = (List<Departamento>) session.getAttribute("listaDepartamentos");
//			boolean nombreDepartamentoExiste = false;
//			for(Departamento departamento : listaDepartamentos)
//			{
//				if(departamento.getNombre().equals(nombreDepartamento))
//				{
//					nombreDepartamentoExiste = true;
//				}
//			}
//			if(!nombreDepartamentoExiste)
//			{
//				String error = "Departamento no encontrado";
//				log.info(error);
//				throw new HorarioException(13,error);
//			}
//			
//			List<Asignatura> listaAsignaturas = (List<Asignatura>) session.getAttribute("listaAsignaturas");
//			List<Reduccion> listaReducciones = (List<Reduccion>) session.getAttribute("listaReducciones");
//			int horasAsignaturas = 0;
//			for(Asignatura asignatura:listaAsignaturas)
//			{
//				horasAsignaturas+= asignatura.getNumeroHorasSemanales();
//			}
//			int horasReducciones = 0;
//			for(Reduccion reduccion:listaReducciones)
//			{
//				horasReducciones+= reduccion.getNumeroHoras();
//			}
//			int horasTotales = horasAsignaturas+horasReducciones;
//			ResumenProfesor resumenProfesor = new ResumenProfesor(listaAsignaturas,listaReducciones,horasTotales);
//
//			
//			log.info(resumenProfesor.toString());
//			return ResponseEntity.ok("Datos del profesor "+idProfesor+": \n"+ resumenProfesor.toString());
//		}catch(HorarioException horarioException)
//		{
//			return ResponseEntity.status(410).body(horarioException.getMessage());
//		}catch(Exception exception)
//		{
//			String error = "Error desconocido";
//			log.error(error,exception.getMessage());
//			return ResponseEntity.status(400).body(exception.getMessage());
//		}
//	}
}
