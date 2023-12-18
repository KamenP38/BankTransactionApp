import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class YourId extends JFrame {
	public static final String JDBC_DRIVER = "org.postgresql.Driver";
	// postgres URLs are of the form: jdbc:postgresql://host:port/database
	public static final String JDBC_DB = "banking_system3";
	public static final String JDBC_PORT = "5432";
	public static final String JDBC_HOST = "localhost";
	public static final String JDBC_URL = "jdbc:postgresql://" + JDBC_HOST + ":" + JDBC_PORT + "/" + JDBC_DB;
	public static final String DBUSER = "postgres";
	public static final String DBPASSWD = "kamenpetkov824";
	
	private JPanel contentPane;
	private String userID = "";
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					YourId frame = new YourId();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public YourId() throws FileNotFoundException {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(600, 300, 300, 200); 
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		try {
			//creating File instance to reference text file in Java
	        File text = new File("Customer.ser");
	     
	        //Creating Scanner instance to read File in Java
	        Scanner scnr = new Scanner(text);
	     
	        //Reading each line of the file using Scanner class
	        while(scnr.hasNextLine()){
	        	userID = scnr.nextLine();
	        }      
		} catch(Exception FileNotFoundException) {
		    throw new FileNotFoundException("Error.");
	    }
		
		//new MainMenu().setVisible(true);
		
		JLabel lblNewLabel = new JLabel("Your ID is: " + userID, SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(60, 85, 150, 40);
		contentPane.add(lblNewLabel);

		JButton btnNewButton = new JButton("Go to Login Page");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MainMenu().setVisible(true);
				dispose();
			}
		});
		btnNewButton.setBounds(15, 20, 250, 40);
		contentPane.add(btnNewButton);
		
	}
	
	
	
}
