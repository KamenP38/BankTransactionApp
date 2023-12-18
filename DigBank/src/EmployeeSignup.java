import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.Random;

import javax.swing.JTable;

public class EmployeeSignup extends JFrame {

	public static final String JDBC_DRIVER = "org.postgresql.Driver";
	// postgres URLs are of the form: jdbc:postgresql://host:port/database
	public static final String JDBC_DB = "banking_system3";
	public static final String JDBC_PORT = "5432";
	public static final String JDBC_HOST = "localhost";
	public static final String JDBC_URL = "jdbc:postgresql://" + JDBC_HOST + ":" + JDBC_PORT + "/" + JDBC_DB;
	public static final String DBUSER = "postgres";
	public static final String DBPASSWD = "kamenpetkov824";

	private JPanel contentPane;
	private JTextField SSN;
	private JTextField role;
	private JTextField firstname;
	private JTextField lastname;
	private JTextField country;
	private JTextField state;
	private JTextField city;
	private JTextField zip;
	private JTextField street;
	private JTextField password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmployeeSignup frame = new EmployeeSignup();
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
	public EmployeeSignup() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 810, 555);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);


		JLabel lblNewLabel = new JLabel("Employee Sign Up");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBounds(85, 10, 188, 39);
		contentPane.add(lblNewLabel);

		JLabel lblSSN = new JLabel("SSN:");
		lblSSN.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSSN.setBounds(340, 90, 100, 13);
		contentPane.add(lblSSN);

		JLabel lblEmpType = new JLabel("Role:");
		lblEmpType.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEmpType.setBounds(340, 125, 100, 13);
		contentPane.add(lblEmpType);

		JLabel lblNewLabel_1 = new JLabel("First Name:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(38, 90, 100, 13);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Last Name:");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1.setBounds(38, 125, 100, 13);
		contentPane.add(lblNewLabel_1_1);

		JLabel lblNewLabel_1_1_2 = new JLabel("Country: ");
		lblNewLabel_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1_2.setBounds(38, 154, 75, 20);
		contentPane.add(lblNewLabel_1_1_2);

		JLabel lblNewLabel_state = new JLabel("State: ");
		lblNewLabel_state.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_state.setBounds(38, 187, 75, 20);
		contentPane.add(lblNewLabel_state);

		JLabel lblNewLabel_1_1_2_1 = new JLabel("City: ");
		lblNewLabel_1_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1_2_1.setBounds(38, 221, 45, 20);
		contentPane.add(lblNewLabel_1_1_2_1);

		JLabel lblNewLabel_1_1_2_1_1 = new JLabel("ZIP: ");
		lblNewLabel_1_1_2_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1_2_1_1.setBounds(38, 251, 125, 20);
		contentPane.add(lblNewLabel_1_1_2_1_1);

		JLabel lblNewLabel_1_1_2_1_1_1 = new JLabel("Street: ");
		lblNewLabel_1_1_2_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1_2_1_1_1.setBounds(38, 281, 125, 20);
		contentPane.add(lblNewLabel_1_1_2_1_1_1);

		JLabel lblNewLabel_1_1_2_1_1_1_1 = new JLabel("Password: ");
		lblNewLabel_1_1_2_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1_2_1_1_1_1.setBounds(38, 310, 125, 20);
		contentPane.add(lblNewLabel_1_1_2_1_1_1_1);

		SSN = new JTextField();
		SSN.setBounds(385, 89, 125, 20);
		contentPane.add(SSN);
		SSN.setColumns(10);

		role = new JTextField();
		role.setColumns(10);
		role.setBounds(385, 122, 125, 20);
		contentPane.add(role);

		firstname = new JTextField();
		firstname.setBounds(166, 89, 125, 20);
		contentPane.add(firstname);
		firstname.setColumns(10);

		lastname = new JTextField();
		lastname.setColumns(10);
		lastname.setBounds(166, 119, 125, 20);
		contentPane.add(lastname);

		country = new JTextField();
		country.setColumns(10);
		country.setBounds(166, 154, 125, 20);
		contentPane.add(country);

		state = new JTextField();
		state.setColumns(10);
		state.setBounds(166, 187, 125, 20);
		contentPane.add(state);

		city = new JTextField();
		city.setColumns(10);
		city.setBounds(166, 221, 125, 20);
		contentPane.add(city);

		zip = new JTextField();
		zip.setColumns(10);
		zip.setBounds(166, 254, 125, 20);
		contentPane.add(zip);

		street = new JTextField();
		street.setColumns(10);
		street.setBounds(166, 281, 125, 20);
		contentPane.add(street);

		password = new JTextField();
		password.setColumns(10);
		password.setBounds(166, 311, 125, 20);
		contentPane.add(password);


		JButton btnNewButton = new JButton("Sign Up");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {			
				try {
					Class.forName(JDBC_DRIVER);
					Connection conn = DriverManager.getConnection(JDBC_URL, DBUSER, DBPASSWD);
					PreparedStatement pst = conn.prepareStatement("SELECT nextval('EMPLOYEE_SEQUENCE')");
					synchronized( this ) {
						PrintWriter writer = new PrintWriter("EMPLOYEE.ser");
						ResultSet rs = pst.executeQuery();
						long myId = 0;
						if(rs.next()) {
							myId = rs.getLong(1);
						}

						Random branchGen = new Random();
						long branchId = branchGen.nextInt(3) + 1;

						Statement st = conn.createStatement();

						int salary = 0;
						if (role.getText().equals("Teller")) {
							salary = 73000;
						} else {
							salary = 80000;
						}

						rs = st.executeQuery("SELECT nextval('ADDRESS_SEQUENCE')");
						long addressId = 0;
						if(rs.next()) {
							addressId = rs.getLong(1);
						}
						st.executeUpdate("INSERT INTO ADDRESS VALUES ('" + city.getText() + "', '" + street.getText() + "', '" + zip.getText() + "', '" + country.getText() 
						+ "', '" + state.getText()+ "', '"+ addressId+ "')");
						st.executeUpdate("INSERT INTO EMPLOYEE VALUES ('" + myId + "', '" + password.getText() + "', '" + firstname.getText() + "', '" + lastname.getText() + "', '"
								+ salary +"', '" + role.getText()  + "', '" + SSN.getText() + "', '" +addressId +"', '"+ branchId + "')");

						writer.println(myId);
						writer.close();
						st.close();
					}
					conn.close();		
					new YourIdEmp().setVisible(true);
					dispose();
				} catch (Exception r) {
					r.printStackTrace();
				}
			}	


		});

		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnNewButton.setBounds(125, 350, 101, 29);
		contentPane.add(btnNewButton);

		JButton homeButton = new JButton("Home");
		homeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MainMenu().setVisible(true);
				dispose();
			}
		});
		homeButton.setBounds(10, 10, 70, 39);
		contentPane.add(homeButton);


	}
}
