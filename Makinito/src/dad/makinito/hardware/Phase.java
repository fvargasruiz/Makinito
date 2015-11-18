package dad.makinito.hardware;

/**
 * Fases por las que pasa el secuenciador de forma cíclica:
 * - Carga (LOADING)				: 	recupera una instrucción de la memoria y la lleva a la unidad de control.
 * - Decodificación (DECODIFICATION): 	determina las señales de control a ejecutar para llevar a cabo la instrucción cargada.
 * - Ejecución (EXECUTION)			:	activa las señales de control generadas en la fase anterior.
 *  
 * @author Francisco Vargas
 */
public enum Phase {
	LOADING,
	DECODIFICATION,
	EXECUTION;
}
