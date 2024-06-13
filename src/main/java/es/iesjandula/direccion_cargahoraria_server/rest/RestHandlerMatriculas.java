package es.iesjandula.direccion_cargahoraria_server.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import es.iesjandula.direccion_cargahoraria_server.utils.Parse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@RequestMapping(value="/matriculas")
@RestController
@Log4j2
public class RestHandlerMatriculas 
{
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.POST,value="/cursos",consumes="multipart/form-data")
	public ResponseEntity<?> uploadCursos(@RequestParam(value="csv",required=true)MultipartFile csvFile,
			@RequestHeader(value="curso",required=true)Integer curso,
			@RequestHeader(value="etapa",required=true)String etapa,
			HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			Map<String, Map<String, List<String>>> mapaCursos = new HashMap<String, Map<String,List<String>>>();
			Map<String, List<String>> mapaAsignaturas = new HashMap<String, List<String>>();
			String clave = parse.parseCursosMap(csvFile, curso, etapa, mapaAsignaturas, session);
			mapaAsignaturas= (Map<String, List<String>>) session.getAttribute("mapaAsignaturasCursos");
			mapaCursos.put(clave, mapaAsignaturas);
			session.setAttribute("mapaCursos", mapaCursos);
			log.info(mapaCursos);
			return ResponseEntity.ok().body("Carga realizada con exito");
		}
		catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(exception.getMessage());
		}
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/cursos")
	public ResponseEntity<?> getCursos(HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			Map<String, Map<String, List<String>>> mapaCursos = new HashMap<String, Map<String,List<String>>>();
			mapaCursos = parse.comprobarMapaCursosExiste(session, mapaCursos);
			return ResponseEntity.ok().body(mapaCursos.keySet());
		}
		catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(exception.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.POST,value="/bloques")
	public ResponseEntity<?> uploadBloques(@RequestHeader(value="curso",required=true)Integer curso,
	@RequestHeader(value="etapa",required=true)String etapa,
	@RequestHeader(value="nombreAsignatura",required=true)String nombreAsignatura,
	HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			Map<String, List<String>> mapaBloques = new HashMap<String, List<String>>();
			List<Asignatura> listaAsignaturas = (List<Asignatura>) session.getAttribute("listaAsignaturas");
			parse.comprobarListaAsignaturas(session, listaAsignaturas);
			List<String> listaNombreAsignatura = new ArrayList<String>();
			boolean encontrado = false;
			String resultado="alguno de los parametros es incorrecto";
			int i = 0;
			while(i < listaAsignaturas.size() && !encontrado) 
			{
				if(listaAsignaturas.get(i).getNombreAsinatura().equals(nombreAsignatura) && listaAsignaturas.get(i).getCurso()==curso && listaAsignaturas.get(i).getEtapa().equalsIgnoreCase(etapa)) 
				{
					encontrado=true;
				}
			}
			if(encontrado) 
			{
				resultado="Se ha realizado correctamente";
				String clave = curso + etapa; 
				listaNombreAsignatura.add(nombreAsignatura);
				mapaBloques.put(clave, listaNombreAsignatura);
				session.setAttribute("mapaBloques", mapaBloques);
			}
			return ResponseEntity.ok().body(resultado);
		}
		catch(HorarioException horarioException)
		{
			String error = "Error de parseo";
			log.error(error,horarioException.getBodyExceptionMessage());
			return ResponseEntity.status(410).body(error);
		}
		catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(error);
		}
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/bloques")
	public ResponseEntity<?> getBloques(@RequestHeader(value="curso",required=true)Integer curso,
	@RequestHeader(value="etapa",required=true)String etapa,
	HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			Map<String, List<String>> mapaBloques = new HashMap<String, List<String>>();
			mapaBloques = parse.comprobarMapaBloquesExiste(session, mapaBloques);
			String clave = curso + etapa; 
			List<String> listaAsignatura = mapaBloques.get(clave);
			return ResponseEntity.ok().body(listaAsignatura);
		}
		catch(HorarioException horarioException)
		{
			String error = "Error de parseo";
			log.error(error,horarioException.getBodyExceptionMessage());
			return ResponseEntity.status(410).body(error);
		}
		catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(error);
		}
	}
	/**
	 * metodo para cargar alumnos
	 * @param alumno
	 * @param curso
	 * @param etapa
	 * @param grupo
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.POST,value="/alumnos")
	public ResponseEntity<?> uploadAlumno(@RequestHeader(value="alumno",required=true)String alumno,
			@RequestHeader(value="curso",required=true)Integer curso,
			@RequestHeader(value="etapa",required=true)String etapa,
			@RequestHeader(value="grupo",required=true)String grupo,
	HttpSession session)
	{
		try
		{
			Parse parse = new Parse();
			Curso cursoObject = new Curso(curso, etapa, grupo);
			String resultado="";
			Map<String, List<String>> mapaAlumnos;
			String clave = curso+etapa+grupo;
			List<Curso> listaCursos = new ArrayList<Curso>();
			listaCursos = (List<Curso>) session.getAttribute("listaCursos");
			parse.comprobarListCursos(session, listaCursos);
			if(listaCursos.contains(cursoObject)) 
			{
				List<String> listaAlumnos = new ArrayList<String>();
				if(session.getAttribute("mapaAlumnos")!=null) 
				{
					mapaAlumnos=(Map<String, List<String>>) session.getAttribute("mapaAlumnos");
					if(mapaAlumnos.containsKey(clave)) 
					{
						listaAlumnos = mapaAlumnos.get(clave);
						if(listaAlumnos.contains(alumno)) 
						{
							resultado = "no se ha podido añadir";
						}
						else 
						{
							listaAlumnos.add(alumno);
							resultado="se ha añadido correctamente";
							mapaAlumnos.put(clave, listaAlumnos);
							session.setAttribute("mapaAlumnos", mapaAlumnos);
						}	
					}
					else
					{
						listaAlumnos.add(alumno);
						resultado="se ha añadido correctamente";
						mapaAlumnos.put(clave, listaAlumnos);
						session.setAttribute("mapaAlumnos", mapaAlumnos);
					}
				}
				else 
				{
					mapaAlumnos= new HashMap<String, List<String>>();
					List<String> listaAlumnos2 = new ArrayList<String>();
					listaAlumnos2.add(alumno);
					mapaAlumnos.put(clave, listaAlumnos2);
					resultado="se ha añadido correctamente";
					session.setAttribute("mapaAlumnos", mapaAlumnos);
				}
				log.info(mapaAlumnos);
			}
			else
			{
				String error = "El curso no existe";
				throw new HorarioException(1,error);
			}
			return ResponseEntity.ok().body(resultado);
		}
		catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(exception.getMessage());
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,value="/alumnos")
	public ResponseEntity<?> getAlumno(@RequestHeader(value="alumno",required=true)String alumno,
			@RequestHeader(value="curso",required=true)Integer curso,
			@RequestHeader(value="etapa",required=true)String etapa,
			@RequestHeader(value="grupo",required=true)String grupo,
	HttpSession session)
	{
		try
		{
			String clave=curso+etapa+grupo;
			Map<String, List<String>> mapaAlumnos;
			mapaAlumnos=(Map<String, List<String>>) session.getAttribute("mapaAlumnos");
			if(session.getAttribute("mapaAlumnos")!=null) 
			{
				mapaAlumnos=(Map<String, List<String>>) session.getAttribute("mapaAlumnos");
			}
			else 
			{
				String error = "Los alumnos no han sido cargados en sesion todavía";
				throw new HorarioException(1,error);
			}
			List<String> listaAlumnos = mapaAlumnos.get(clave);
			return ResponseEntity.ok().body(listaAlumnos);
		}
		catch(HorarioException horarioException)
		{
			return ResponseEntity.status(410).body(horarioException.getBodyExceptionMessage());
		}
		catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(exception.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.DELETE,value="/alumnos")
	public ResponseEntity<?> borrarAlumno(@RequestHeader(value="alumno",required=true)String alumno,
			@RequestHeader(value="curso",required=true)Integer curso,
			@RequestHeader(value="etapa",required=true)String etapa,
			@RequestHeader(value="grupo",required=true)String grupo,
	HttpSession session)
	{
		try
		{
			String resultado="No existe el alumno";
			String clave=curso+etapa+grupo;
			Map<String, List<String>> mapaAlumnos;
			mapaAlumnos=(Map<String, List<String>>) session.getAttribute("mapaAlumnos");
			if(session.getAttribute("mapaAlumnos")!=null) 
			{
				mapaAlumnos=(Map<String, List<String>>) session.getAttribute("mapaAlumnos");
			}
			else 
			{
				String error = "Los alumnos no han sido cargados en sesion todavía";
				throw new HorarioException(1,error);
			}
			List<String> listaAlumnos = mapaAlumnos.get(clave);
			log.info(listaAlumnos);
			boolean encontrado= false;
			int i = 0;
			while(i < listaAlumnos.size() && !encontrado) 
			{
				if(listaAlumnos.get(i).contains(alumno)) 
				{
					listaAlumnos.remove(i);
					encontrado=true;
					resultado="Se ha eliminado correctamente";
					
				}
				i++;
			}
			return ResponseEntity.ok().body(resultado);
		}
		catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(exception.getMessage());
		}
	
	}
	/**
	 * 
	 * @param nombreAsignatura
	 * @param curso
	 * @param etapa
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.DELETE,value="/asignaturas/resumen")
	public ResponseEntity<?> getAsignaturasResuemn(@RequestHeader(value="nombreAsignatura",required=true)String nombreAsignatura,
			@RequestHeader(value="curso",required=true)Integer curso,
			@RequestHeader(value="etapa",required=true)String etapa,
	HttpSession session)
	{
		try
		{
			String clave = curso+etapa;
			Map<String, List<String>> mapaAlumnos;
			if(session.getAttribute("mapaAlumnos")!=null) 
			{
				mapaAlumnos=(Map<String, List<String>>) session.getAttribute("mapaAlumnos");
			}
			else
			{
				String error = "No se ha cargado el mapa cursos";
				throw new HorarioException(1,error);
			}
			mapaAlumnos.get(clave);
			return ResponseEntity.ok().body(mapaAlumnos);
		}
		catch(Exception exception)
		{
			String error = "Error desconocido";
			log.error(error,exception.getMessage());
			return ResponseEntity.status(400).body(exception.getMessage());
		}
	}
}
