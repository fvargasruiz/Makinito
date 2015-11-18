package dad.makinito.hardware;

/**
 * Fases por las que pasa el secuenciador de forma c�clica:
 * - Carga (LOADING)				: 	recupera una instrucci�n de la memoria y la lleva a la unidad de control.
 * - Decodificaci�n (DECODIFICATION): 	determina las se�ales de control a ejecutar para llevar a cabo la instrucci�n cargada.
 * - Ejecuci�n (EXECUTION)			:	activa las se�ales de control generadas en la fase anterior.
 *  
 * @author Francisco Vargas
 */
public enum Phase {
	LOADING,
	DECODIFICATION,
	EXECUTION;
}
