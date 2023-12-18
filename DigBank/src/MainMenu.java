import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.Canvas;

import java.sql.*;

public class MainMenu extends JFrame {
	
	public static final String JDBC_DRIVER = "org.postgresql.Driver";
	// postgres URLs are of the form: jdbc:postgresql://host:port/database
	public static final String JDBC_DB = "banking_system3";
	public static final String JDBC_PORT = "5432";
	public static final String JDBC_HOST = "localhost";
	public static final String JDBC_URL = "jdbc:postgresql://" + JDBC_HOST + ":" + JDBC_PORT + "/" + JDBC_DB;
	public static final String DBUSER = "postgres";
	public static final String DBPASSWD = "kamenpetkov824";

	private JPanel contentPane;
	private JTextField usernameCus;
	private JTextField passCus;
	private JTextField usernameEmp;
	private JTextField passEmp;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu frame = new MainMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	
	
	/**
	 * Create the frame.
	 */
	public MainMenu() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 810, 555); 
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("ID");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(115, 208, 85, 43);
		contentPane.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Log in");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName(JDBC_DRIVER);
					Connection c = DriverManager.getConnection(JDBC_URL, DBUSER, DBPASSWD);
					Statement st = c.createStatement();
					ResultSet rs = st.executeQuery("select * from CUSTOMER where ID='"+usernameCus.getText() + "'");
					if(rs.next() && rs.getString("cust_password").compareTo(passCus.getText())==0) {	
						PrintWriter writer = new PrintWriter("CUSTOMER.ser");	
		                writer.println(rs.getString("ID"));
		                writer.close();
						new CustomerPage().setVisible(true);
						dispose();
					}
					else {
						JOptionPane.showMessageDialog(null, "Log in failed. Wrong username or password.");
					}
					st.close();
	            	c.close();
	            	
	            	
				} catch (Exception r) {
					r.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(167, 372, 113, 43);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Log in");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName(JDBC_DRIVER);
					Connection c = DriverManager.getConnection(JDBC_URL, DBUSER, DBPASSWD);
					Statement st = c.createStatement();
					ResultSet rs = st.executeQuery("select * from EMPLOYEE where ID='"+usernameEmp.getText()+"'");
					if(rs.next() && rs.getString("emp_password").compareTo(passEmp.getText())==0) {
						PrintWriter writer = new PrintWriter("EMPLOYEE.ser");
		                writer.println(rs.getString("ID"));
		                writer.close();
		                if(rs.getString("emp_type").equals("Teller")) {
		                	new EmployeePage().setVisible(true);
							dispose();
		                } else {
		                	new EmployeePageManagerAcc().setVisible(true);
							dispose();
		                }
					}
					else {
						JOptionPane.showMessageDialog(null, "Log in failed. Wrong username or password.");
					}
					st.close();
	            	c.close();
				} catch (Exception r) {
					r.printStackTrace();
				}
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnNewButton_1.setBounds(514, 372, 113, 43);
		contentPane.add(btnNewButton_1);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPassword.setBounds(90, 286, 85, 43);
		contentPane.add(lblPassword);
		
		JLabel lblNewLabel_2 = new JLabel("ID");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(445, 208, 85, 43);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblPassword_1 = new JLabel("Password");
		lblPassword_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPassword_1.setBounds(427, 286, 85, 43);
		contentPane.add(lblPassword_1);
		
		usernameCus = new JTextField();
		usernameCus.setBounds(204, 215, 122, 26);
		contentPane.add(usernameCus);
		usernameCus.setColumns(10);
		
		passCus = new JPasswordField();
		passCus.setColumns(10);
		passCus.setBounds(204, 300, 122, 26);
		contentPane.add(passCus);
		
		usernameEmp = new JTextField();
		usernameEmp.setColumns(10);
		usernameEmp.setBounds(548, 219, 122, 26);
		contentPane.add(usernameEmp);
		
		passEmp = new JPasswordField();
		passEmp.setColumns(10);
		passEmp.setBounds(548, 300, 122, 26);
		contentPane.add(passEmp);
		
		JLabel lblCustomer = new JLabel("Customer Sign in");
		lblCustomer.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblCustomer.setBounds(90, 144, 160, 43);
		contentPane.add(lblCustomer);
		
		JLabel lblEmployee = new JLabel("Employee Sign in");
		lblEmployee.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEmployee.setBounds(427, 144, 169, 43);
		contentPane.add(lblEmployee);
		
		JLabel lblOnlineRetailer = new JLabel("Online Banking");
		lblOnlineRetailer.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblOnlineRetailer.setBounds(327, 44, 180, 43);
		contentPane.add(lblOnlineRetailer);
		
		JButton btnSignUp = new JButton("Sign Up");
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CustomerSignup().setVisible(true);
				dispose();
				
			}
		});
		btnSignUp.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSignUp.setBounds(167, 447, 113, 43);
		contentPane.add(btnSignUp);
		
		JLabel lblOr = new JLabel("Or");
		lblOr.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblOr.setBounds(204, 410, 85, 43);
		contentPane.add(lblOr);
		
		Icon icon = new ImageIcon("C:\\Users\\ijahw\\Documents\\IIT\\Spring 2022\\CS 425\\home.png");
		
		JButton btnSignUp2 = new JButton("Sign Up");
		btnSignUp2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new EmployeeSignup().setVisible(true);
				dispose();
			}
		});
		btnSignUp2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSignUp2.setBounds(514, 447, 113, 43);
		contentPane.add(btnSignUp2);
		
		JLabel lblOr_1 = new JLabel("Or");
		lblOr_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblOr_1.setBounds(551, 410, 85, 43);
		contentPane.add(lblOr_1);
		
	}
	
	
//	public int getIdCustomer() {
//		return Integer.parseInt(usernameCus.getText());
//	}
//	
//	public int getIdEmp() {
//		return Integer.parseInt(usernameEmp.getText());
//	}
}
