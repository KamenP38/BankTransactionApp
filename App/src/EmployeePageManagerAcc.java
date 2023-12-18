import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class EmployeePageManagerAcc extends JFrame {
	public static final String JDBC_DRIVER = "org.postgresql.Driver";
	// postgres URLs are of the form: jdbc:postgresql://host:port/database
	public static final String JDBC_DB = "banking_system3";
	public static final String JDBC_PORT = "5432";
	public static final String JDBC_HOST = "localhost";
	public static final String JDBC_URL = "jdbc:postgresql://" + JDBC_HOST + ":" + JDBC_PORT + "/" + JDBC_DB;
	public static final String DBUSER = "postgres";
	public static final String DBPASSWD = "kamenpetkov824";

	private JTextField accountID;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmployeePageManagerAcc frame = new EmployeePageManagerAcc();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public EmployeePageManagerAcc() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel position = new JLabel("Manager");
		position.setFont(new Font("Tahoma", Font.BOLD, 30));
		position.setBounds(400, 180, 400, 39);
		contentPane.add(position);

		JLabel lblNewLabel = new JLabel("Choose account on which to work: ");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBounds(35, 10, 400, 39);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Account ID:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(38, 90, 100, 13);
		contentPane.add(lblNewLabel_1);

		accountID = new JTextField();
		accountID.setBounds(166, 89, 125, 20);
		contentPane.add(accountID);
		accountID.setColumns(10);

		JButton btnNewButton2 = new JButton("Check Existing Account");
		btnNewButton2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnNewButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// try catch begin
				try {
					Class.forName(JDBC_DRIVER);
					Connection c = DriverManager.getConnection(JDBC_URL, DBUSER, DBPASSWD);
					Statement st = c.createStatement();
					ResultSet rs = st.executeQuery("select * from ACCOUNTS where account_num = "+ accountID.getText());
					
					PrintWriter writer = new PrintWriter("Teller.ser");
	                writer.println(accountID.getText());
	                writer.close();
					
					if(rs.next()) {
						new EmployeePageManager().setVisible(true);
						dispose();
					}
					else {
						JOptionPane.showMessageDialog(null, "This account does not exist.");
					}

					st.close();
					c.close();


				} catch (Exception r) {
					r.printStackTrace();
				}
			}
		});
		btnNewButton2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnNewButton2.setBounds(38, 130, 250, 40);
		contentPane.add(btnNewButton2);
		
		JButton homeButton = new JButton("Home");
		homeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MainMenu().setVisible(true);
				dispose();
			}
		});
		homeButton.setBounds(467, 20, 70, 39);
		contentPane.add(homeButton);
	}
}
