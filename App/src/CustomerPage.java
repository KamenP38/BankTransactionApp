import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;



public class CustomerPage extends JFrame {

	public static final String JDBC_DRIVER = "org.postgresql.Driver";
	// postgres URLs are of the form: jdbc:postgresql://host:port/database
	public static final String JDBC_DB = "banking_system3";
	public static final String JDBC_PORT = "5432";
	public static final String JDBC_HOST = "localhost";
	public static final String JDBC_URL = "jdbc:postgresql://" + JDBC_HOST + ":" + JDBC_PORT + "/" + JDBC_DB;
	public static final String DBUSER = "postgres";
	public static final String DBPASSWD = "kamenpetkov824";

	private JPanel contentPane;
	private String user;
	private String nameUser;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CustomerPage frame = new CustomerPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public CustomerPage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 810, 555);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		try (BufferedReader br = new BufferedReader(new FileReader("CUSTOMER.ser"))) {
			String line;

			while ((line = br.readLine()) != null) {
				if(!line.isEmpty()){
					user = line;
				} else {
					break;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		try {
			Class.forName(JDBC_DRIVER);
			Connection c = DriverManager.getConnection(JDBC_URL, DBUSER, DBPASSWD);
			Statement st = c.createStatement();
			ResultSet rs = st.executeQuery("select * from CUSTOMER where ID="+Integer.parseInt(user)+"");
			if(rs.next()) {	
				nameUser = rs.getString("firstname");
			}
			else {
				JOptionPane.showMessageDialog(null, "ERROR: There is something wrong in the system.");
			}
			st.close();
			c.close();


		} catch (Exception r) {
			r.printStackTrace();
		}


		JLabel lblNewLabel = new JLabel("Hello, " + nameUser); 
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBounds(90, 10, 188, 39);
		contentPane.add(lblNewLabel);
		
		JLabel lblAcc = new JLabel("Create new savings/checking account: "); 
		lblAcc.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblAcc.setBounds(64, 85, 400, 39);
		contentPane.add(lblAcc);

		String[] newAcc = {
				"Savings +",
				"Checking Mega",
				"Checking Unlimited"
		};

		JComboBox menuNewAcc = new JComboBox(newAcc);
		menuNewAcc.setEditable(true);
		menuNewAcc.setBounds(64, 130, 120, 39);
		getContentPane().add(menuNewAcc);
		
		JButton btnNewButton = new JButton("Create Account");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String val = (String) menuNewAcc.getSelectedItem();
				if (val.compareTo("Checking Mega")==0) {
					// try catch begin
					try {
						PrintWriter writer = new PrintWriter("BankAccounts.ser");
						Class.forName(JDBC_DRIVER);
						Connection conn = DriverManager.getConnection(JDBC_URL, DBUSER, DBPASSWD);
						PreparedStatement pst = conn.prepareStatement("SELECT nextval('ACCOUNT_SEQUENCE')");
						synchronized( this ) {
							ResultSet rs = pst.executeQuery();
							long accountNum = 0;
							if(rs.next()) {
								accountNum = rs.getLong(1);
							}
							writer.println(accountNum);
			                writer.close();
							Statement st = conn.createStatement();

							st.executeUpdate("INSERT INTO ACCOUNTS VALUES ('" + accountNum + "', '" + 0 + "', 'Checking Mega')");
							st.executeUpdate("INSERT INTO Accounts_And_Owners VALUES ('" + accountNum +"', '" + Integer.parseInt(user) +"')");
							JOptionPane.showMessageDialog(null, "You created a \"Checking Mega\" account.");
						}
					} catch (Exception r) {
						r.printStackTrace();
					}
					// end of try catch
				}
				else if(val.compareTo("Savings +")==0) {
					// try catch begin
					try {
						Class.forName(JDBC_DRIVER);
						Connection conn = DriverManager.getConnection(JDBC_URL, DBUSER, DBPASSWD);
						PreparedStatement pst = conn.prepareStatement("SELECT nextval('ACCOUNT_SEQUENCE')");
						synchronized( this ) {
							PrintWriter writer = new PrintWriter("BankAccounts.ser");
							ResultSet rs = pst.executeQuery();
							long accountNum = 0;
							if(rs.next()) {
								accountNum = rs.getLong(1);
							}
							writer.println(accountNum);
			                writer.close();
							Statement st = conn.createStatement();

							st.executeUpdate("INSERT INTO ACCOUNTS VALUES ('" + accountNum + "', '" + 0 + "', 'Savings +')");
							st.executeUpdate("INSERT INTO Accounts_And_Owners VALUES ('" + accountNum +"', '" + Integer.parseInt(user) +"')");
							JOptionPane.showMessageDialog(null, "You created a \"Savings +\" account.");
						}
					} catch (Exception r) {
						r.printStackTrace();
					}
					// end of try catch
				}
				else if(val.compareTo("Checking Unlimited")==0) {
					// try catch begin
					try {
						Class.forName(JDBC_DRIVER);
						Connection conn = DriverManager.getConnection(JDBC_URL, DBUSER, DBPASSWD);
						PreparedStatement pst = conn.prepareStatement("SELECT nextval('ACCOUNT_SEQUENCE')");
						synchronized( this ) {
							PrintWriter writer = new PrintWriter("BankAccounts.ser");
							ResultSet rs = pst.executeQuery();
							long accountNum = 0;
							if(rs.next()) {
								accountNum = rs.getLong(1);
							}
							writer.println(accountNum);
			                writer.close();
							Statement st = conn.createStatement();
							
							st.executeUpdate("INSERT INTO ACCOUNTS VALUES ('" + accountNum + "', '" + 0 + "', 'Checking Unlimited')");
							st.executeUpdate("INSERT INTO Accounts_And_Owners VALUES ('" + accountNum +"', '" + Integer.parseInt(user) +"')");
							JOptionPane.showMessageDialog(null, "You created a \"Checking Unlimited\" account.");
						}
					} catch (Exception r) {
						r.printStackTrace();
					}
					// end of try catch
				}
			}	
		});
		btnNewButton.setBounds(204, 130, 180, 39);
		contentPane.add(btnNewButton);
		
		
		
		JComboBox menuExistingAcc = new JComboBox(newAcc);
		menuExistingAcc.setEditable(true);
		menuExistingAcc.setBounds(64, 300, 120, 39);
		getContentPane().add(menuExistingAcc);
		
		
		JLabel lblAcc2 = new JLabel("Check existing account: "); 
		lblAcc2.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblAcc2.setBounds(64, 255, 400, 39);
		contentPane.add(lblAcc2);
		
		JButton btnNewButton2 = new JButton("Check Existing Account");
		btnNewButton2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnNewButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String val = (String) menuExistingAcc.getSelectedItem();
				if (val.compareTo("Checking Mega")==0 || val.compareTo("Savings +")==0 || val.compareTo("Checking Unlimited")==0) {
					// try catch begin
					try {
						PrintWriter writer = new PrintWriter("CheckingYourAccount.ser");
						Class.forName(JDBC_DRIVER);
						Connection c = DriverManager.getConnection(JDBC_URL, DBUSER, DBPASSWD);
						Statement st = c.createStatement();
						ResultSet rs = st.executeQuery("select * from ACCOUNTS_AND_OWNERS where ID = "+ Integer.parseInt(user));
						int[] accs = new int[5];
						int tracker = 0;
						while(rs.next()) {
							accs[tracker] = rs.getInt("account_num");
							tracker++;
						}
						
						tracker = 0;
						boolean lastRs = false;
						rs = st.executeQuery("select * from ACCOUNTS where account_num = "+ accs[tracker] + " AND account_name = '" + val + "'");
						lastRs = rs.next();
						while(!lastRs && accs[tracker] != 0) {
							tracker++;
							rs = st.executeQuery("select * from ACCOUNTS where account_num = "+ accs[tracker] + " AND account_name = '" + val + "'");
							lastRs = rs.next();
						}
						
						if(lastRs) {
							writer.println(rs.getInt("account_num") + "\n" + rs.getString("account_name"));
			                writer.close();
							new YourAccountInfo().setVisible(true);
							dispose();
						}
						else {
							JOptionPane.showMessageDialog(null, "There is no " + val + " account.");
						}
							
						st.close();
		            	c.close();
		            	
		            	
					} catch (Exception r) {
						r.printStackTrace();
					}
					// end of try catch
				}
				else {
					JOptionPane.showMessageDialog(null, "Choose one of the three given options.");
				}
			}	
		});
		btnNewButton2.setBounds(204, 300, 250, 39);
		contentPane.add(btnNewButton2);
		
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
