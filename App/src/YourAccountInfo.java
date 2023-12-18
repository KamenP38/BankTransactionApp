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


public class YourAccountInfo extends JFrame{
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
	private JTextField transferNum;
	private JTextField transferTo;
	private JTextField description;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					YourAccountInfo frame = new YourAccountInfo();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public YourAccountInfo() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 810, 555);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		String[][] data = new String[1][7];
		try {
			Class.forName(JDBC_DRIVER);
			Connection conn = DriverManager.getConnection(JDBC_URL, DBUSER, DBPASSWD);
			BufferedReader reader = new BufferedReader(new FileReader("CheckingYourAccount.ser"));
			int accountNum = Integer.parseInt(reader.readLine());
			String accountType = reader.readLine();
			reader.close();
			PreparedStatement pst2 = conn.prepareStatement("SELECT balance, account_num, account_name, interest_rate, neg_balance, overdraft_fee, monthly_fee FROM Accounts natural join Account_Types WHERE account_num = " 
					+ accountNum + " AND account_name = '" + accountType + "'");
			ResultSet rs2 = pst2.executeQuery();

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
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		String[] columnNames = {"Account Number", "Account Name", "Interest Rate", "Negative Balance Allowed?",
				"Overdraft Fee Allowed?", "Monthly Fee", "Balance"};

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(50, 100, 644, 38);
		getContentPane().add(scrollPane);
		tableCart = new JTable(data,columnNames);
		scrollPane.setViewportView(tableCart);
		tableCart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

			} 
		});

		JButton deposit = new JButton("Deposit");
		deposit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {			
				try {
					// What happens when we deposit?  
					Class.forName(JDBC_DRIVER);
					Connection conn = DriverManager.getConnection(JDBC_URL, DBUSER, DBPASSWD);
					PreparedStatement pst = conn.prepareStatement("SELECT nextval('TRANSACTION_SEQUENCE')");

					BufferedReader reader = new BufferedReader(new FileReader("CheckingYourAccount.ser"));
					int accountNum = Integer.parseInt(reader.readLine());
					String accountType = reader.readLine();
					reader.close();

					// WHAT DOES DEPOSIT DO??? HMMM LETS SEEEEEEE
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

							new YourAccountInfo().setVisible(true);
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

		JLabel lbl1 = new JLabel("$");
		lbl1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lbl1.setBounds(36, 210, 120, 30);
		contentPane.add(lbl1);

		depositNum = new JTextField();
		depositNum.setBounds(50, 210, 120, 30);
		contentPane.add(depositNum);
		depositNum.setColumns(10);




		//CREATE TABLE Transactions(
		//    trans_type VARCHAR(10) CHECK(trans_type IN ('Deposit', 'Withdrawal', 'Transfer')),
		//    amount NUMERIC(8, 2),
		//    description VARCHAR(1000),
		//    trans_ID INT NOT NULL,
		//    date DATE,
		//    account_num INT,
		//    PRIMARY KEY (trans_ID),
		//    FOREIGN KEY (account_num) REFERENCES Accounts(account_num)
		//);

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

			BufferedReader reader = new BufferedReader(new FileReader("CheckingYourAccount.ser"));
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



		JButton withdrawal = new JButton("Withdraw");
		withdrawal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName(JDBC_DRIVER);
					Connection conn = DriverManager.getConnection(JDBC_URL, DBUSER, DBPASSWD);
					PreparedStatement pst = conn.prepareStatement("SELECT nextval('TRANSACTION_SEQUENCE')");

					BufferedReader reader = new BufferedReader(new FileReader("CheckingYourAccount.ser"));
					int accountNum = Integer.parseInt(reader.readLine());
					String accountType = reader.readLine();
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

							if(accountType.equals("Checking Unlimited") && (rs.getInt("balance") - Double.parseDouble(withdrawalNum.getText())) < 0) {
								JOptionPane.showMessageDialog(null, "Negative balance not allowed!");
							} else {
								st.executeUpdate("UPDATE ACCOUNTS "
										+ "SET balance = " + df.format((rs.getDouble("balance") - Double.parseDouble(withdrawalNum.getText())))
										+ " WHERE account_num = " + rs.getInt("account_num"));

								st.executeUpdate("INSERT INTO Transactions VALUES ('Withdrawal', " + df.format(Double.parseDouble(withdrawalNum.getText())) + 
										", 'description goes here', " + transactionID + ", CURRENT_DATE, " + accountNum + ")");
								new YourAccountInfo().setVisible(true);
								dispose();
								JOptionPane.showMessageDialog(null, "You withdrew $" + df.format(Double.parseDouble(withdrawalNum.getText())));
							}	
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

		JButton transfer = new JButton("Transfer");
		transfer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName(JDBC_DRIVER);
					Connection conn = DriverManager.getConnection(JDBC_URL, DBUSER, DBPASSWD);
					PreparedStatement pst = conn.prepareStatement("SELECT nextval('TRANSACTION_SEQUENCE')");

					BufferedReader reader = new BufferedReader(new FileReader("CheckingYourAccount.ser"));
					int accountNum = Integer.parseInt(reader.readLine());
					String accountType = reader.readLine();
					reader.close();

					synchronized( this ) {
						DecimalFormat df = new DecimalFormat("############0.00");

						PrintWriter writer = new PrintWriter("TransactionTracker.ser");
						ResultSet rs = pst.executeQuery();
						ResultSet rs2;
						long transactionID = 0;
						if(rs.next()) {
							transactionID = rs.getLong(1);
						}
						writer.println(transactionID);
						writer.close();

						Statement st = conn.createStatement();
						Statement st2 = conn.createStatement();
						rs = st.executeQuery("select * from ACCOUNTS where account_num = "+ accountNum);
						rs2 = st2.executeQuery("select * from ACCOUNTS where account_num = "+ Integer.parseInt(transferTo.getText()));
						if(rs.next() && rs2.next()) {

							st.executeUpdate("BEGIN WORK; UPDATE ACCOUNTS "
									+ "SET balance = " + df.format((rs.getDouble("balance") - Double.parseDouble(transferNum.getText())))
									+ " WHERE account_num = " + rs.getInt("account_num") + "; INSERT INTO Transactions VALUES ('Transfer', " + 
									df.format(Double.parseDouble(transferNum.getText())) + ", 'description goes here', " + transactionID + ", CURRENT_DATE, " 
									+ accountNum + "); UPDATE ACCOUNTS SET balance = " + df.format((rs2.getDouble("balance") + Double.parseDouble(transferNum.getText())))
									+ " WHERE account_num = " + rs2.getInt("account_num") + "; COMMIT;");

							//	st.executeUpdate("INSERT INTO Transactions VALUES ('Transfer', " + df.format(Double.parseDouble(transferNum.getText())) + 
							//			", 'description goes here', " + transactionID + ", CURRENT_DATE, " + accountNum + ");");

							new YourAccountInfo().setVisible(true);
							dispose();
							JOptionPane.showMessageDialog(null, "You transferred $" + df.format(Double.parseDouble(transferNum.getText())) + " to "+ 
									transferTo.getText());
						} 
						else {
							JOptionPane.showMessageDialog(null, "This transaction got denied. The account does not exist");
						}

						//	if(rs2.next()) {
						//		st.executeUpdate("UPDATE ACCOUNTS "
						//				+ "SET balance = " + df.format((rs2.getDouble("balance") + Double.parseDouble(transferNum.getText())))
						//				+ " WHERE account_num = " + rs2.getInt("account_num"));
						//	}
						//						long transactionID = 0;
						//						if(rs.next()) {
						//							transactionID = rs.getLong(1);
						//						}
						//						writer.println(transactionID);
						//						writer.close();
						//
						//						Statement st = conn.createStatement();
						//						rs = st.executeQuery("select * from ACCOUNTS where account_num = "+ accountNum);
						//						
						//						if(rs.next()) {
						//
						//							st.executeUpdate("UPDATE ACCOUNTS "
						//									+ "SET balance = " + df.format((rs.getDouble("balance") - Double.parseDouble(transferNum.getText())))
						//									+ " WHERE account_num = " + rs.getInt("account_num"));
						//
						//							st.executeUpdate("INSERT INTO Transactions VALUES ('Transfer', " + df.format(Double.parseDouble(transferNum.getText())) + 
						//									", 'description goes here', " + transactionID + ", CURRENT_DATE, " + accountNum + ")");
						//							
						//							new YourAccountInfo().setVisible(true);
						//							dispose();
						//							JOptionPane.showMessageDialog(null, "You transferred $" + df.format(Double.parseDouble(transferNum.getText())) + " to "+ 
						//							transferTo.getText());
						//						}
						//						
						//						rs = st.executeQuery("select * from ACCOUNTS where account_num = "+ Integer.parseInt(transferTo.getText()));
						//						if(rs.next()) {
						//							st.executeUpdate("UPDATE ACCOUNTS "
						//									+ "SET balance = " + df.format((rs.getDouble("balance") + Double.parseDouble(transferNum.getText())))
						//									+ " WHERE account_num = " + rs.getInt("account_num"));
						//						}


						st.close();
						st2.close();
						conn.close();


					}
				} catch (Exception r) {
					r.printStackTrace();
				}
			}
		});
		transfer.setBounds(550, 290, 120, 39);
		contentPane.add(transfer);

		JLabel lbl3 = new JLabel("$");
		lbl3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lbl3.setBounds(536, 210, 120, 30);
		contentPane.add(lbl3);

		transferNum = new JTextField();
		transferNum.setBounds(550, 210, 120, 30);
		contentPane.add(transferNum);
		transferNum.setColumns(10);

		JLabel lblTo = new JLabel("To");
		lblTo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTo.setBounds(527, 250, 120, 30);
		contentPane.add(lblTo);

		transferTo = new JTextField();
		transferTo.setBounds(550, 250, 120, 30);
		contentPane.add(transferTo);
		transferTo.setColumns(10);


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
				new CustomerPage().setVisible(true);
				dispose();
			}
		});
		goBackButton.setBounds(150, 20, 90, 39);
		contentPane.add(goBackButton);
	}
}
