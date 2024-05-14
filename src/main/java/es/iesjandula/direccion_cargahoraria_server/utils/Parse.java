package es.iesjandula.direccion_cargahoraria_server.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.web.multipart.MultipartFile;

import es.iesjandula.direccion_cargahoraria_server.exception.HorarioException;
import es.iesjandula.direccion_cargahoraria_server.models.Asignatura;
import es.iesjandula.direccion_cargahoraria_server.models.Curso;
import es.iesjandula.direccion_cargahoraria_server.models.Departamento;
import es.iesjandula.direccion_cargahoraria_server.models.Profesor;
import es.iesjandula.direccion_cargahoraria_server.models.Reduccion;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Parse 
{
	/**
	 * 
	 * @param csvFile
	 * @return
	 * @throws HorarioException
	 */
	public static List<Departamento> parseDepartamentos(MultipartFile csvFile) throws HorarioException
	{
		try
		{
			List<Departamento> listaDepartamentos = new ArrayList<>();
			String contenido = new String(csvFile.getBytes());
			Scanner scanner = new Scanner(contenido);
			scanner.nextLine();
			while(scanner.hasNext())
			{
				String[] linea = scanner.nextLine().split(",");
				Departamento departamento = new Departamento(linea[0]);
				listaDepartamentos.add(departamento);
			}
			
			scanner.close();
			return listaDepartamentos;
		}catch(IOException ioException ) 
		{
			String error = "Error al realizar la lectura del fichero CSV";
			log.error(error,ioException);
			throw new HorarioException(11,error,ioException);
		}
		
	}

	/**
	 * 
	 * @param csvFile
	 * @return
	 * @throws HorarioException
	 */
	public static List<Curso> parseCursos(MultipartFile csvFile) throws HorarioException
	{
		try
		{
			List<Curso> listaCursos = new ArrayList<>();
			String contenido = new String(csvFile.getBytes());
			Scanner scanner = new Scanner(contenido);
			scanner.nextLine();
			while(scanner.hasNext())
			{
				String[] linea = scanner.nextLine().split(",");
				Curso curso = new Curso(linea[0],linea[1],linea[2]);
				listaCursos.add(curso);
			}
			scanner.close();
			return listaCursos;
		}catch(IOException ioException ) 
		{
			String error = "Error al realizar la lectura del fichero CSV";
			log.error(error,ioException);
			throw new HorarioException(11,error,ioException);
		}
		
	}
	
	/**
	 * 
	 * @param csvFile
	 * @param listaDepartamentos
	 * @return
	 * @throws HorarioException
	 */
	public static List<Profesor> parseProfesores(MultipartFile csvFile,List<Departamento> listaDepartamentos) throws HorarioException
	{
		try
		{
			List<Profesor> listaProfesores = new ArrayList<>();
			String contenido = new String(csvFile.getBytes());
			Scanner scanner = new Scanner(contenido);
			scanner.nextLine();
			boolean departamentoExiste = false;
			while(scanner.hasNext())
			{
				String[] linea = scanner.nextLine().split(",");
				
				Departamento departamento = new Departamento(linea[2]);
				for(Departamento departamentoLista : listaDepartamentos)
				{
					if(departamentoLista.getNombre().equals(departamento.getNombre()))
					{
						departamentoExiste = true;
					}
				}
				if(!departamentoExiste)
				{
					String error = "Departamento no encontrado";
					log.info(error);
					throw new HorarioException(12,error);
				}
				Profesor profesor = new Profesor(linea[0],linea[1],linea[2]);
				listaProfesores.add(profesor);
			}
			scanner.close();
			return listaProfesores;
		}catch(IOException ioException ) 
		{
			String error = "Error al realizar la lectura del fichero CSV";
			log.error(error,ioException);
			throw new HorarioException(11,error,ioException);
		}
		
	}

	/**
	 * 
	 * @param csvFile
	 * @param listaCursos
	 * @param listaDepartamentos
	 * @return
	 * @throws HorarioException
	 */
	public static List<Asignatura> parseAsignaturas(MultipartFile csvFile,List<Curso> listaCursos,List<Departamento> listaDepartamentos) throws HorarioException
	{
		try
		{
			List<Asignatura> listaAsignaturas = new ArrayList<>();
			String contenido = new String(csvFile.getBytes());
			Scanner scanner = new Scanner(contenido);
			scanner.nextLine();
			boolean departamentoExiste = false;
			boolean cursoExiste = false;
			while(scanner.hasNext())
			{
				String[] linea = scanner.nextLine().split(",");
				
				Departamento departamento = new Departamento(linea[5]);
				for(Departamento departamentoLista : listaDepartamentos)
				{
					if(departamentoLista.getNombre().equals(departamento.getNombre()))
					{
						departamentoExiste = true;
					}
				}
				if(!departamentoExiste)
				{
					String error = "Departamento no encontrado";
					log.info(error);
					throw new HorarioException(12,error);
				}
				
				Curso curso = new Curso(linea[1],linea[2],linea[3]);
				for(Curso cursoLista : listaCursos)
				{
					if(cursoLista.equals(curso))
					{
						cursoExiste = true;
					}
				}
				if(!cursoExiste)
				{
					String error = "Curso no encontrado";
					throw new HorarioException(12,error);
				}
				Asignatura asignatura = new Asignatura(linea[0],linea[1],linea[2],linea[3],Integer.valueOf(linea[4]),linea[5]);
				listaAsignaturas.add(asignatura);
			}
			scanner.close();
			return listaAsignaturas;
		}catch(IOException ioException ) 
		{
			String error = "Error al realizar la lectura del fichero CSV";
			log.error(error,ioException);
			throw new HorarioException(11,error,ioException);
		}
		
	}
	
	/**
	 * 
	 * @param csvFile
	 * @param listaCursos
	 * @return
	 * @throws HorarioException
	 */
	public static List<Reduccion> parseReducciones(MultipartFile csvFile,List<Curso> listaCursos) throws HorarioException
	{
		try
		{
			List<Reduccion> listaReducciones = new ArrayList<>();
			Reduccion reduccion;
			String contenido = new String(csvFile.getBytes());
			Scanner scanner = new Scanner(contenido);
			scanner.nextLine();
			boolean cursoExiste = false;
			while(scanner.hasNext())
			{
				String[] linea = scanner.nextLine().split(",");
				
				if(linea[1].equals("Tutoria")) {
					Curso curso = new Curso(linea[3],linea[4],linea[5]);
					for(Curso cursoLista : listaCursos)
					{
						if(cursoLista.equals(curso))
						{
							cursoExiste = true;
						}
					}
					if(!cursoExiste)
					{
						String error = "Curso no encontrado";
						throw new HorarioException(12,error);
					}
					reduccion = new Reduccion(linea[0],linea[1],Integer.valueOf(linea[2]),curso.getCurso(),curso.getEtapa(),curso.getGrupo());
					listaReducciones.add(reduccion);
				}else
				{
					reduccion = new Reduccion(linea[0],linea[1],Integer.valueOf(linea[2]),null,null,null);
					listaReducciones.add(reduccion);
				}
				
				
			}
			scanner.close();
			return listaReducciones;
		}catch(IOException ioException ) 
		{
			String error = "Error al realizar la lectura del fichero CSV";
			log.error(error,ioException);
			throw new HorarioException(11,error,ioException);
		}
		
	}

}
