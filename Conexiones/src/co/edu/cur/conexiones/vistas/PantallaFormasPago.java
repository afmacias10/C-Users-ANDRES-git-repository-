package co.edu.cur.conexiones.vistas;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import co.edu.cur.conexiones.implementaciones.TransaccionMYSQL;
import co.edu.cur.conexiones.implementaciones.TransaccionSQLSERVER;
import co.edu.cur.conexiones.interfaces.ITransaccion;

public class PantallaFormasPago {

	private JFrame frame;
	private JTextField txtCodigo;
	private JTextField txtFormaPago;

	/**
	 * Launch the application.
	 */
	
	PreparedStatement ps = null;
	ResultSet rs = null;
	int retorno = 0;
	private JTextField txtEstado;
	
	private void limpiarcuadrotxt() {
		txtCodigo.setText(null);
		txtFormaPago.setText(null);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PantallaFormasPago window = new PantallaFormasPago();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PantallaFormasPago() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setForeground(Color.BLACK);
		frame.getContentPane().setBackground(new Color(100, 149, 237));
		frame.getContentPane().setFont(new Font("Tahoma", Font.BOLD, 12));
		frame.setBounds(100, 100, 262, 423);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblConexionBdMysql = new JLabel("Maestro Formas de Pago");
		lblConexionBdMysql.setForeground(new Color(0, 0, 0));
		lblConexionBdMysql.setFont(new Font("Segoe UI Symbol", Font.BOLD, 15));
		lblConexionBdMysql.setBounds(28, 0, 191, 31);
		frame.getContentPane().add(lblConexionBdMysql);
		
		JLabel lblCodigo = new JLabel("Codigo");
		lblCodigo.setFont(new Font("Segoe UI Symbol", Font.BOLD, 14));
		lblCodigo.setBounds(95, 39, 50, 23);
		frame.getContentPane().add(lblCodigo);
		
		JLabel lblFormadePago = new JLabel("Forma de Pago");
		lblFormadePago.setFont(new Font("Segoe UI Symbol", Font.BOLD, 14));
		lblFormadePago.setBounds(71, 122, 108, 14);
		frame.getContentPane().add(lblFormadePago);
		
		txtCodigo = new JTextField();
		txtCodigo.setForeground(Color.RED);
		txtCodigo.setBounds(107, 63, 26, 31);
		frame.getContentPane().add(txtCodigo);
		txtCodigo.setColumns(10);
		
		txtFormaPago = new JTextField();
		txtFormaPago.setBounds(28, 138, 191, 23);
		frame.getContentPane().add(txtFormaPago);
		txtFormaPago.setColumns(10);
		
		JLabel lblEstado = new JLabel("Estado");
		lblEstado.setFont(new Font("Segoe UI Symbol", Font.BOLD, 14));
		lblEstado.setBounds(96, 168, 62, 11);
		frame.getContentPane().add(lblEstado);
		
		txtEstado = new JTextField();
		txtEstado.setForeground(Color.RED);
		txtEstado.setColumns(10);
		txtEstado.setBounds(87, 182, 62, 23);
		frame.getContentPane().add(txtEstado);
		
		JButton btnConsultar = new JButton("Consultar");
		btnConsultar.setForeground(new Color(0, 0, 0));
		btnConsultar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ITransaccion transaccion= new TransaccionMYSQL();
					Connection con = transaccion.conectar();
					String sql = ("select FormadePago,Estado from t_formasdepago where Codigo = ?");
					ps = con.prepareStatement(sql);
					ps.setString(1, txtCodigo.getText());
					ResultSet rs = ps.executeQuery();
					if(rs.next()) {
						txtFormaPago.setText(rs.getString ("FormadePago"));
						txtEstado.setText(rs.getString ("Estado"));
					}else {
						rs.close();
						ps.close();
						con.close();
						transaccion= new TransaccionSQLSERVER();
						con = transaccion.conectar();
						sql = ("select FormasdePagos,Estado from FormasdePago where Codigo = ?");
						ps = con.prepareStatement(sql);
						ps.setString(1, txtCodigo.getText());
						rs = ps.executeQuery();
						if(rs.next()) {
							txtFormaPago.setText(rs.getString ("FormasdePagos"));
							txtEstado.setText(rs.getString ("Estado"));
						}else {
						
						JOptionPane.showMessageDialog(null, "Codigo no Encontrado");
						}
						rs.close();
						ps.close();
						con.close();
					}
					con.close();
			}catch (SQLException e1){
				JOptionPane.showMessageDialog(null,"Error al Acceder a BD");
				e1.printStackTrace();
			}
			}	
		});
		btnConsultar.setBounds(71, 220, 97, 23);
		frame.getContentPane().add(btnConsultar);
	
		JButton btnInsertar = new JButton("Insertar");
		btnInsertar.setBackground(new Color(255, 255, 255));
		btnInsertar.setForeground(new Color(0, 0, 0));
		btnInsertar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ITransaccion transaccionMySQL = new TransaccionMYSQL();
					Connection con = transaccionMySQL.conectar();
					ITransaccion transaccionSQLSERVER = new TransaccionSQLSERVER();
					Connection conSQLServer = transaccionSQLSERVER.conectar();
					String sql = "insert into t_formasdepago(Codigo,FormadePago,Estado)values(?,?,?)";
					
					ps = con.prepareStatement(sql);
					ps.setString(1, txtCodigo.getText());
					ps.setString(2, txtFormaPago.getText());
					ps.setString(3, txtEstado.getText());
					retorno = ps.executeUpdate();
					if(retorno >0 ) {
						JOptionPane.showMessageDialog(null,"Registro Insertado Correctamente en la BD de MYSQL" );
						
					} else {
						JOptionPane.showMessageDialog(null,"No se Pudo Insertar el Registro en la BD de MYSQL" );
					}
					
					ps.close();
					con.close();
					String sqlSQLServer = ("insert into FormasdePago (Codigo,FormasdePagos,Estado)values(?,?,?)");
					ps = conSQLServer.prepareStatement(sqlSQLServer);
					ps.setString(1, txtCodigo.getText());
					ps.setString(2, txtFormaPago.getText());
					ps.setString(3, txtEstado.getText());
					retorno = ps.executeUpdate();
					if(retorno >0 ) {
						JOptionPane.showMessageDialog(null,"Registro Insertado Correctamente en la BD de SQLServer" );
						limpiarcuadrotxt();
				
					} else {
						JOptionPane.showMessageDialog(null,"No se Pudo Insertar el Registro en la BD de SQLServer" );
					}
					ps.close();
					con.close();
				}
				catch (SQLException e1){
					JOptionPane.showMessageDialog(null,"Error al Acceder a la BD");
					e1.printStackTrace();
				}
			}

			
		});
		btnInsertar.setBounds(28, 279, 97, 23);
		frame.getContentPane().add(btnInsertar);
		
		JButton button = new JButton("Actualizar");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ITransaccion transaccionMySQL = new TransaccionMYSQL();
					Connection con = transaccionMySQL.conectar();
					ITransaccion transaccionSQLSERVER = new TransaccionSQLSERVER();
					Connection conSQLServer = transaccionSQLSERVER.conectar();
					String sql = ("update t_formasdepago set Estado = ? where Codigo = ?");
					ps = con.prepareStatement(sql);
					ps.setString(2, txtCodigo.getText());
					ps.setString(1, txtEstado.getText());
					retorno = ps.executeUpdate();
					if(retorno >0 ) {
						JOptionPane.showMessageDialog(null,"Registro Actualizado Correctamente en la BD de MYSQL" );
						
					} else {
						JOptionPane.showMessageDialog(null,"No se Pudo Actualizar el Registro en la BD de MYSQL" );
					}
					ps.close();
					con.close();
					String sqlSQLServer = ("update FormasdePago set Estado = ? where Codigo = ?");
					ps = conSQLServer.prepareStatement(sqlSQLServer);
					ps.setString(2, txtCodigo.getText());
					ps.setString(1, txtEstado.getText());
					retorno = ps.executeUpdate();
					if(retorno >0 ) {
						JOptionPane.showMessageDialog(null,"Registro Actualizado Correctamente en la BD de SQLServer" );
						limpiarcuadrotxt();
				
					} else {
						JOptionPane.showMessageDialog(null,"No se Pudo Actualizar el Registro en la BD de SQLServer" );
					}
					ps.close();
					con.close();
				}
				catch (SQLException e1){
					JOptionPane.showMessageDialog(null,"Error al Acceder a BD");
					e1.printStackTrace();
				}
			}
		});
		button.setBounds(28, 313, 97, 23);
		frame.getContentPane().add(button);
		
		JButton btnBorrar = new JButton("Borrar");
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ITransaccion transaccionMySQL = new TransaccionMYSQL();
					Connection con = transaccionMySQL.conectar();
					ITransaccion transaccionSQLSERVER = new TransaccionSQLSERVER();
					Connection conSQLServer = transaccionSQLSERVER.conectar();
					String sql = ("delete from t_formasdepago where Codigo = ?");
					ps = con.prepareStatement(sql);
					ps.setString(1, txtCodigo.getText());
					retorno = ps.executeUpdate();
					if(retorno >0 ) {
						JOptionPane.showMessageDialog(null,"Registro Borrado Correctamente de la BD de MYSQL" );
						
					} else {
						JOptionPane.showMessageDialog(null,"No se Pudo Borrar el Registro de la BD de MYSQL porque no Existe" );
					}
					ps.close();
					con.close();
					String sqlSQLServer = ("delete from FormasdePago where Codigo = ?");
					ps = conSQLServer.prepareStatement(sqlSQLServer);
					ps.setString(1, txtCodigo.getText());
					retorno = ps.executeUpdate();
					if(retorno >0 ) {
						JOptionPane.showMessageDialog(null,"Registro Borrado Correctamente de la BD de SQLServer" );
						limpiarcuadrotxt();
				
					} else {
						JOptionPane.showMessageDialog(null,"No se Pudo Borrar el Registro de la BD de SQLServer porque no Existe" );
					}
					ps.close();
					con.close();
				}
				catch (SQLException e1){
					JOptionPane.showMessageDialog(null,"Error al Acceder a BD");
					e1.printStackTrace();
				}
			}
			
		});
		btnBorrar.setBounds(36, 347, 82, 23);
		frame.getContentPane().add(btnBorrar);
		
		JButton btnSalir = new JButton("Salir");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				System.exit(0);
			}
		});
		btnSalir.setBounds(151, 279, 74, 60);
		frame.getContentPane().add(btnSalir);
	}
}
		
