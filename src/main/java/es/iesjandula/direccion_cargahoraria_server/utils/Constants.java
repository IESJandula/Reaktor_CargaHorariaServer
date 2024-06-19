package es.iesjandula.direccion_cargahoraria_server.utils;

public final class Constants
{
	/** Error - Lectura ficheros genérica - Código */
	public static final int ERR_LECTURA_FICHEROS_CSV_CODE = 0;
	
	/** Error - Lectura ficheros genérica - Mensaje */
	public static final String ERR_LECTURA_FICHEROS_CSV_MSG = "Error al realizar la lectura del fichero CSV en ";

	/** Error - Contenido ficheros genérica - Código */
	public static final int ERR_CONTENIDO_FICHEROS_CSV_CODE = 1;
	
	/** Error - Contenido ficheros genérica - Mensaje */
	public static final String ERR_CONTENIDO_FICHEROS_CSV_MSG = "Error el fichero siguiente se escuentra vacio: ";
	
	/** Error - Excepción lista nula - Code */
	public static final int ERR_LIST_NULL_CODE = 2;
	
	/** Error - Excepción al validar curso o etapa - Code */
	public static final int ERR_VALIDATE_CURSO_ETAPA = 3;
	
	/** Error - Excepción al obtener id del profesor - Code */
	public static final int ERR_PROFESOR_ID = 4;
	
	/** Error - Excepción al obtener el nombre de la asignatura - Code */
	public static final int ERR_NOMBRE_ASIGNATURA = 5;
	
	/** Error - Excepción al obtener la asignatura- Code */
	public static final int ERR_ASIGNATURA = 6;
	
	/** Error - Excepción al comprobar si la asignatura existe- Code */
	public static final int ERR_ASIGNACION_ASIGNATURA = 7;
	
	/** Error - Excepción mapa asignaturas nulo - Code */
	public static final int ERR_MAP_NULL = 8;
	
	/** Error - Excepción al obtener el id de la reduccion - Code */
	public static final int ERR_ID_REDUCCION = 9;
	
	/** Error - Excepción la reduccion existe - Code */
	public static final int ERR_REDUCCION_EXIS = 10;
	
	/** Error - Excepción al obtener el departamento - Code */
	public static final int ERR_DEPART_DONT_EXIS = 11;
	
	/** Error - Excepción al obtener el curso - Code */
	public static final int ERR_CURSO_EXIS = 12;
	
	/** Error - Excepción el alumno ya ha sido asignado - Code */
	public static final int ERR_ALUM_ASIG = 13;
	
	/** Error - Excepción el alumno no existe - Code */
	public static final int ERR_ALUM_EXIS = 14;
	
	/** Error - Excepción la asignatura ya esta registrada - Code */
	public static final int ERR_ASIG_REGIS = 15;
	
	/** Error - Excepción la asignatura ya esta registrada - Code */
	public static final int ERR_ASIG_CURSO = 16;
	
	/** Error - Excepción genérica - Código */
	public static final int ERR_GENERIC_EXCEPTION_CODE = 100;
	
	/** Error - Excepción genérica - Mensaje */
	public static final String ERR_GENERIC_EXCEPTION_MSG = "Excepción genérica en ";
	
	/** String - nombre del metodo que parsea departamentos */
	public static final String NOMBRE_METH_PARSE_DEPARTAMENTOS = "parseDepartamentos";
	
	/** String - nombre del metodo que parsea curso */
	public static final String NOMBRE_METH_PARSE_CURSOS = "parseCursos";
	
	/** String - nombre del metodo que parsea profesores */
	public static final String NOMBRE_METH_PARSE_PROFESORES = "parseProfesores";
	
	/** String - nombre del metodo que parsea asignaturas */
	public static final String NOMBRE_METH_PARSE_ASIGNATURAS = "parseAsignaturas";
	
	/** String - nombre del metodo que parsea reducciones */
	public static final String NOMBRE_METH_PARSE_REDUCCIONES = "parseReducciones";
	
	/** String - nombre del metodo que parsea reducciones */
	public static final String NOMBRE_METH_PARSE_CURSOSMAP = "parseCursosMap";
	
	/** String - tipo de asignatura */
	public static final String ASIGNATURA_TUTORIA = "tutoria";
	
	/** String - Nombre lista departamento session */
	public static final String SESION_LISTA_DEPARTAMENTOS = "listaDepartamentos";
	
	/** String - Nombre lista cursos session */
	public static final String SESION_LISTA_CURSOS = "listaCursos";
	
	/** String - Nombre lista cursos session */
	public static final String SESION_LISTA_PROFESORES = "listaProfesores";
	
	/** String - Nombre lista cursos session */
	public static final String SESION_LISTA_ASIGNATURAS = "listaAsignaturas";
	
	/** String - Nombre lista cursos session */
	public static final String SESION_LISTA_REDUCCIONES = "listaReducciones";
	
	/** String - Nombre lista nombres session */
	public static final String SESION_LISTA_NOMBRES = "listaNombres";
	
	/** String - Nombre mapa asignaturas session */
	public static final String SESION_MAPA_ASIGNATURAS = "mapaAsignaturas";
	
	/** String - Nombre mapa reducciones session */
	public static final String SESION_MAPA_REDUCCIONES = "mapaReduccion";
	
	/** String - Nombre mapa guardias session */
	public static final String SESION_MAPA_GUARDIAS = "mapaGuardias";
	
	/** String - Nombre mapa asignatura cursos session */
	public static final String SESION_MAPA_ASIGNATURA_CURSOS = "mapaAsignaturasCursos";
	
	/** String - Nombre mapa cursos session */
	public static final String SESION_MAPA_CURSOS = "mapaCursos";
	
	/** String - Nombre mapa bloques session */
	public static final String SESION_MAPA_BLOQUES = "mapaBloques";
	
	/** String - Nombre mapa alumnos session */
	public static final String SESION_MAPA_ALUMNOS = "mapaAlumnos";

}
