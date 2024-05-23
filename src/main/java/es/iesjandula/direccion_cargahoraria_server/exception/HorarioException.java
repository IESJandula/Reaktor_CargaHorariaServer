package es.iesjandula.direccion_cargahoraria_server.exception;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
/**
 * Clase HorarioException
 */
public class HorarioException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4929334625724771326L;
	/**Codigo de error para la excepcion*/
	private int code;
	/**Mensaje de error para la excepcion*/
	private String message;
	/**Excepcion de error para la excepcion*/
	private Exception exception;
	/**
	 * 
	 * @param code
	 * @param message
	 */
	public HorarioException(int code, String message) 
	{
		super(message);
		this.code = code;
		this.message = message;
	}
	/**
	 * 
	 * @param code
	 * @param message
	 * @param exception
	 */
	public HorarioException(int code, String message, Exception exception) 
	{
		super(message,exception);
		this.code = code;
		this.message = message;
		this.exception = exception;
	}
	/**
	 * mapa para devolver la excepcion
	 * @return
	 */
	public Map<String,String> getBodyExceptionMessage()
	{
		Map<String,String> messageMap = new HashMap<String,String>();
		messageMap.put("code", String.valueOf(code));
		messageMap.put("message",message);
		if(this.exception!=null)
		{
			String stackTrace = ExceptionUtils.getStackTrace(exception);
			messageMap.put("exception", stackTrace);
		}
		return messageMap;
	}
}
