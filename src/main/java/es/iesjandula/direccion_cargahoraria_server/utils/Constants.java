package es.iesjandula.direccion_cargahoraria_server.utils;

public final class Constants
{
	/** Error - Lectura ficheros genérica - Código */
	public static final int ERR_LECTURA_FICHEROS_CSV_CODE		= 11 ; 
	/** Error - Lectura ficheros genérica - Mensaje */
	public static final String ERR_LECTURA_FICHEROS_CSV_MSG		= "Error al realizar la lectura del fichero CSV en " ;
	
	/** Error - Lectura ficheros genérica - Código */
	public static final int ERR_CONTENIDO_FICHEROS_CSV_CODE		= 1; 
	/** Error - Lectura ficheros genérica - Mensaje */
	public static final String ERR_CONTENIDO_FICHEROS_CSV_MSG 	= "Error el fichero siguiente se escuentra vacio: " ;
	
	/** Error - Excepción genérica - Código */
	public static final int ERR_GENERIC_EXCEPTION_CODE  		= 100 ;
	/** Error - Excepción genérica - Mensaje */
	public static final String ERR_GENERIC_EXCEPTION_MSG 		= "Excepción genérica en " ;
	/** String - nombre del metodo que parsea departamentos*/
	public static final String NOMBRE_METH_PARSE_DEPARTAMENTOS 	= "parseDepartamentos" ;
	/** String - nombre del metodo que parsea curso*/
	public static final String NOMBRE_METH_PARSE_CURSOS 	  	= "parseCursos" ;
	/** String - nombre del metodo que parsea profesores*/
	public static final String NOMBRE_METH_PARSE_PROFESORES		= "parseProfesores" ;
	/** String - nombre del metodo que parsea asignaturas*/
	public static final String NOMBRE_METH_PARSE_ASIGNATURAS	= "parseAsignaturas" ;
	/** String - nombre del metodo que parsea reducciones*/
	public static final String NOMBRE_METH_PARSE_REDUCCIONES	= "parseReducciones" ;
	/** String - tipo de asignatura*/
	public static final String ASIGNATURA_TUTORIA				= "tutoria" ;
}
