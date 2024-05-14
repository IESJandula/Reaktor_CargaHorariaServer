package es.iesjandula.direccion_cargahoraria_server.exception;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class HorarioException extends Exception
{

	
	private static final long serialVersionUID = -1478009934059719321L;
	private int code;
	private String message;
	private Exception exception;
	
	
	public HorarioException(int code, String message) 
	{
		super(message);
		this.code = code;
		this.message = message;
	}


	public HorarioException(int code, String message, Exception exception) 
	{
		super(message,exception);
		this.code = code;
		this.message = message;
		this.exception = exception;
	}
	
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
