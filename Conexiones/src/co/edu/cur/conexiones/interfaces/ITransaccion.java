/**
 * 
 */
package co.edu.cur.conexiones.interfaces;

import java.sql.Connection;
/**
 * @author ANDRES,
 *Esta interface me permite tener un contrato para el manejo de transacciones de Base de Datos
 */
public interface ITransaccion {
	
	public Connection conectar(); 

}