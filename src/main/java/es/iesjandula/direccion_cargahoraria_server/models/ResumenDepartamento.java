package es.iesjandula.direccion_cargahoraria_server.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumenDepartamento 
{
	private int plantilla;
	private int horasNecesarias;
	private int totalHoras;
	private int desfase;
	private String resultado;
}
