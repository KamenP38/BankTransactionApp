import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import java.text.DecimalFormat;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class EmployeePageManager extends JFrame{
	public static final String JDBC_DRIVER = "org.postgresql.Driver";
	// postgres URLs are of the form: jdbc:postgresql://host:port/database
	public static final String JDBC_DB = "banking_system3";
	public static final String JDBC_PORT = "5432";
	public static final String JDBC_HOST = "localhost";
	public static final String JDBC_URL = "jdbc:postgresql://" + JDBC_HOST + ":" + JDBC_PORT + "/" + JDBC_DB;
	public static final String DBUSER = "postgres";
	public static final String DBPASSWD = "kamenpetkov824";

	private JPanel contentPane;
	private JTable tableCart;
	private JTextField depositNum;
	private JTextField withdrawalNum;
	private JTextField addOwnerTxt;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmployeePageManager frame = new EmployeePageManager();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public EmployeePageManager() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 810, 555);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel position = new JLabel("Manager");
		position.setFont(new Font("Tahoma", Font.BOLD, 30));
		position.setBounds(600, 20, 400, 39);
		contentPane.add(position);

		JLabel lblNewLabel = new JLabel("Account Information ");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBounds(45, 70, 400, 39);
		contentPane.add(lblNewLabel);
		

		String[][] data = new String[1][7];
		try {
			Class.forName(JDBC_DRIVER);
			Connection conn = DriverManager.getConnection(JDBC_URL, DBUSER, DBPASSWD);
			BufferedReader reader = new BufferedReader(new FileReader("Teller.ser"));
			int accountNum = Integer.parseInt(reader.readLine());
			String accountType;
			reader.close();
			PreparedStatement pst2 = conn.prepareStatement("SELECT account_name FROM Accounts WHERE account_num = " + accountNum);
			ResultSet rs2 = pst2.executeQuery();
			Statement st = conn.createStatement();
			if(rs2.next()) {
				accountType = rs2.getString("account_name");
				rs2 = st.executeQuery("SELECT balance, account_num, account_name, interest_rate, neg_balance, overdraft_fee, monthly_fee FROM Accounts natural join Account_Types WHERE account_num = " 
						+ accountNum + " AND account_name = '" + accountType + "'");
				if(rs2.next()) {
					data[0][0]= rs2.getString("account_num");
					data[0][1] = rs2.getString("account_name");
					data[0][2] = rs2.getString("interest_rate");
					if(rs2.getString("neg_balance").equals("t"))
						data[0][3] = "true";
					else
						data[0][3] = "false";
					data[0][4] = rs2.getString("overdraft_fee");
					data[0][5] = rs2.getString("monthly_fee");
					data[0][6] = rs2.getString("balance");
				}
			}



			conn.close();
			st.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		String[] columnNames = {"Account Number", "Account Name", "Interest Rate", "Negative Balance Allowed?",
				"Overdraft Fee Allowed?", "Monthly Fee", "Balance"};

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(50, 125, 644, 38);
		getContentPane().add(scrollPane);
		tableCart = new JTable(data,columnNames);
		scrollPane.setViewportView(tableCart);
		tableCart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

			} 
		});

		JLabel lbl1 = new JLabel("$");
		lbl1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lbl1.setBounds(36, 210, 120, 30);
		contentPane.add(lbl1);

		depositNum = new JTextField();
		depositNum.setBounds(50, 210, 120, 30);
		contentPane.add(depositNum);
		depositNum.setColumns(10);

		JButton deposit = new JButton("Deposit");
		deposit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {			
				try {
					// What happens when we deposit?  
					Class.forName(JDBC_DRIVER);
					Connection conn = DriverManager.getConnection(JDBC_URL, DBUSER, DBPASSWD);
					PreparedStatement pst = conn.prepareStatement("SELECT nextval('TRANSACTION_SEQUENCE')");

					BufferedReader reader = new BufferedReader(new FileReader("Teller.ser"));
					int accountNum = Integer.parseInt(reader.readLine());
					reader.close();

					synchronized( this ) {
						DecimalFormat df = new DecimalFormat("############0.00");

						PrintWriter writer = new PrintWriter("TransactionTracker.ser");
						ResultSet rs = pst.executeQuery();
						long transactionID = 0;
						if(rs.next()) {
							transactionID = rs.getLong(1);
						}
						writer.println(transactionID);
						writer.close();

						Statement st = conn.createStatement();
						rs = st.executeQuery("select * from ACCOUNTS where account_num = "+ accountNum);
						if(rs.next()) {

							st.executeUpdate("UPDATE ACCOUNTS "
									+ "SET balance = " + df.format((rs.getDouble("balance") + Double.parseDouble(depositNum.getText())))
									+ " WHERE account_num = " + rs.getInt("account_num"));

							st.executeUpdate("INSERT INTO Transactions VALUES ('Deposit', " + df.format(Double.parseDouble(depositNum.getText())) + 
									", 'description goes here', " + transactionID + ", CURRENT_DATE, " + accountNum + ")");

							new EmployeePageManager().setVisible(true);
							dispose();
							JOptionPane.showMessageDialog(null, "You deposited $" + df.format(Double.parseDouble(depositNum.getText())));
						}		

						st.close();
						conn.close();
					}


					// DISCUSS THIS PART

				} catch (Exception r) {
					r.printStackTrace();
				}
			}	


		});
		deposit.setBounds(50, 250, 120, 39);
		contentPane.add(deposit);



		JButton withdrawal = new JButton("Withdraw");
		withdrawal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName(JDBC_DRIVER);
					Connection conn = DriverManager.getConnection(JDBC_URL, DBUSER, DBPASSWD);
					PreparedStatement pst = conn.prepareStatement("SELECT nextval('TRANSACTION_SEQUENCE')");

					BufferedReader reader = new BufferedReader(new FileReader("Teller.ser"));
					int accountNum = Integer.parseInt(reader.readLine());
					reader.close();

					synchronized( this ) {
						DecimalFormat df = new DecimalFormat("############0.00");

						PrintWriter writer = new PrintWriter("TransactionTracker.ser");
						ResultSet rs = pst.executeQuery();
						long transactionID = 0;
						if(rs.next()) {
							transactionID = rs.getLong(1);
						}
						writer.println(transactionID);
						writer.close();

						Statement st = conn.createStatement();
						rs = st.executeQuery("select * from ACCOUNTS where account_num = "+ accountNum);
						if(rs.next()) {

							st.executeUpdate("UPDATE ACCOUNTS "
									+ "SET balance = " + df.format((rs.getDouble("balance") - Double.parseDouble(withdrawalNum.getText())))
									+ " WHERE account_num = " + rs.getInt("account_num"));

							st.executeUpdate("INSERT INTO Transactions VALUES ('Withdrawal', " + df.format(Double.parseDouble(withdrawalNum.getText())) + 
									", 'description goes here', " + transactionID + ", CURRENT_DATE, " + accountNum + ")");
							new EmployeePageManager().setVisible(true);
							dispose();
							JOptionPane.showMessageDialog(null, "You withdrew $" + df.format(Double.parseDouble(withdrawalNum.getText())));
						}		

						st.close();
						conn.close();


					}
				} catch (Exception r) {
					r.printStackTrace();
				}
			}
		});
		withdrawal.setBounds(300, 250, 120, 39);
		contentPane.add(withdrawal);

		JLabel lbl2 = new JLabel("$");
		lbl2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lbl2.setBounds(286, 210, 120, 30);
		contentPane.add(lbl2);

		withdrawalNum = new JTextField();
		withdrawalNum.setBounds(300, 210, 120, 30);
		contentPane.add(withdrawalNum);
		withdrawalNum.setColumns(10);



		// CHECK TRANSACTION HISTORY
		JLabel trans = new JLabel("TRANSACTIONS");
		trans.setFont(new Font("Tahoma", Font.PLAIN, 16));
		trans.setBounds(50, 310, 120, 30);
		contentPane.add(trans);

		String[][] data2 = new String[15][6];

		try {
			Class.forName(JDBC_DRIVER);
			Connection c = DriverManager.getConnection(JDBC_URL, DBUSER, DBPASSWD);
			Statement st = c.createStatement();

			BufferedReader reader = new BufferedReader(new FileReader("Teller.ser"));
			int accountNum = Integer.parseInt(reader.readLine());
			String accountType = reader.readLine();
			reader.close();

			ResultSet rs = st.executeQuery("select * from TRANSACTIONS where account_num = " + accountNum);
			int i = 0;
			while(rs.next()) {
				data2[i][0]= rs.getString("trans_type");
				data2[i][1] = rs.getString("amount");
				data2[i][2] = rs.getString("description");	
				data2[i][3] = rs.getString("trans_ID");
				data2[i][4] = rs.getString("date");
				data2[i][5] = rs.getString("account_num");
				i++;
			}
			st.close();
			c.close();
		} catch (Exception r) {
			r.printStackTrace();
		}

		String[] columnNames2 = {"Type", "Ammount", "Description", "Transaction ID",
				"date", "Account Number"};

		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setBounds(50, 350, 644, 130);
		getContentPane().add(scrollPane2);
		tableCart = new JTable(data2,columnNames2);
		scrollPane2.setViewportView(tableCart);
		tableCart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

			} 
		});




		// NEW OWNER TO ACCOUNT


		JButton newOwner = new JButton("Add Owner");
		newOwner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName(JDBC_DRIVER);
					Connection c = DriverManager.getConnection(JDBC_URL, DBUSER, DBPASSWD);
					Statement st = c.createStatement(); 
					ResultSet rs = st.executeQuery("select * from CUSTOMER where ID='" + addOwnerTxt.getText() + "'");
					
					if(rs.next()) {
						ResultSet rs2 = st.executeQuery("select * from accounts_and_owners WHERE ID = " + addOwnerTxt.getText() + " AND account_num = "
								+ data[0][0]);
						if(rs2.next()) {
							JOptionPane.showMessageDialog(null, "User is already an owner of this account.");
						} else
							st.executeUpdate("INSERT INTO accounts_and_owners VALUES (" + data[0][0] + ", " + addOwnerTxt.getText() + ")");
					}
					else {
						JOptionPane.showMessageDialog(null, "ID doesn't exist.");
					}
					st.close();
	            	c.close();


				}
			 catch (Exception r) {
				r.printStackTrace();
			}
		}
	});
		newOwner.setBounds(500, 250, 120, 39);
		contentPane.add(newOwner);

		addOwnerTxt = new JTextField();
		addOwnerTxt.setBounds(500, 210, 120, 30);
		contentPane.add(addOwnerTxt);
		addOwnerTxt.setColumns(10);

		JButton homeButton = new JButton("Home");
		homeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MainMenu().setVisible(true);
				dispose();
			}
		});
		homeButton.setBounds(50, 20, 70, 39);
		contentPane.add(homeButton);

		JButton goBackButton = new JButton("Go Back");
		goBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new EmployeePageManagerAcc().setVisible(true);
				dispose();
			}
		});
		goBackButton.setBounds(150, 20, 90, 39);
		contentPane.add(goBackButton);
}
}
