package panel;

import java.awt.BorderLayout;

import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.Client;
import util.Check;

public class BudgetPanel extends WorkingPanel {
	
	//public static BudgetPanel instance = new BudgetPanel();

	JLabel budget = new JLabel("Budget: ");
	JLabel month = new JLabel("Month(1 - 12): ");
	JTextField budgetField = new JTextField();
	JTextField monthField = new JTextField();

	JButton addBtn = new JButton("add Budget");
	JButton updateBtn=new JButton("update Budget");
	JScrollPane scrollPane;
	JTextArea textArea = new JTextArea(10, 30);
	
//public BudgetPanel() {
//		
//		JPanel centerPanel = new JPanel(new GridLayout(2, 2));
//		JPanel southPanel = new JPanel();
//		centerPanel.add(budget);
//		centerPanel.add(budgetField);
//		centerPanel.add(month);
//		centerPanel.add(monthField);
//		southPanel.add(addBtn);
//		this.add(centerPanel, BorderLayout.CENTER);
//		this.add(southPanel, BorderLayout.SOUTH);
//
//		addListener();
//	}

	public BudgetPanel(Client curClient) {
		
		super(curClient);
		JPanel centerPanel = new JPanel(new GridLayout(2, 2));
		JPanel southPanel = new JPanel();
		centerPanel.add(budget);
		centerPanel.add(budgetField);
		centerPanel.add(month);
		centerPanel.add(monthField);
		southPanel.add(addBtn);
		southPanel.add(updateBtn);
		textArea.setEditable(false);
		scrollPane = new JScrollPane(textArea);
		add(scrollPane, BorderLayout.SOUTH);
		this.add(centerPanel, BorderLayout.NORTH);
		this.add(southPanel, BorderLayout.CENTER);

		addListener();
	}

	@Override
	public void updateData() {
		// TODO Auto-generated method stub
		textArea.setText("");
		monthField.setText("");
		budgetField.setText("");
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:javabook.db");
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.exit(0);
		}
		try {
			String query="select * from Budget where userName = ? order by month asc";
			PreparedStatement ps=connection.prepareStatement(query);
			ps.setString(1, userName);
			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					textArea.append("Month: " + resultSet.getInt(3)+ "   Budget: " + resultSet.getInt(2)+"   \n");
				}
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.exit(0);

		}
		// Close the connection
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public void addListener() {
		addBtn.addActionListener((e) -> {
			if (!Check.checkPositive(budgetField, "Budget")) 
				return;
			else if(!Check.checkMonth(monthField, "Month"))
				return;
			// Connect to a database
			else {
				Connection connection = null;
				try {
					connection = DriverManager.getConnection("jdbc:sqlite:javabook.db");
				} catch (SQLException e1) {
					e1.printStackTrace();
					System.exit(0);
				}
				try {
					String query="select * from Budget where userName=? and month=?";
					PreparedStatement ps = connection.prepareStatement(query);
					ps.setString(1,userName);
					ps.setString(2, monthField.getText());
					try (ResultSet resultSet = ps.executeQuery()) {
						if (resultSet.next()) {
							JOptionPane.showMessageDialog(null, "The budget for this month already exists. If you want to change it, use the update button!");
						}
						else {
							try {
								String query1="insert into Budget values(?,?,?) ";
								PreparedStatement ps1 = connection.prepareStatement(query1);
								ps1.setString(1,userName);
								ps1.setString(2, budgetField.getText());
								ps1.setString(3, monthField.getText());
								ps1.execute();
								JOptionPane.showMessageDialog(null, "Inserted Successfully");

							} catch (SQLException e1) {
								e1.printStackTrace();
								System.exit(0);
							}
							
						}
					}

				} catch (SQLException e1) {
					e1.printStackTrace();
					System.exit(0);
				}
				
				// Close the connection
				try {
					connection.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
					System.exit(0);
				}
			}
			updateData();
		});
	
	updateBtn.addActionListener((e) -> {
		if (!Check.checkPositive(budgetField, "Budget")) 
			return;
		else if(!Check.checkMonth(monthField, "Month"))
			return;
		// Connect to a database
		else {
			Connection connection = null;
			try {
				connection = DriverManager.getConnection("jdbc:sqlite:javabook.db");
			} catch (SQLException e1) {
				e1.printStackTrace();
				System.exit(0);
			}
			
			try {
				String query="select * from Budget where userName=? and month=?";
				PreparedStatement ps = connection.prepareStatement(query);
				ps.setString(1,userName);
				ps.setString(2, monthField.getText());
				try (ResultSet resultSet = ps.executeQuery()) {
					if (!resultSet.next()) {
						JOptionPane.showMessageDialog(null, "The budget for this month does not exist! Please add it first!");
					}
					else {
						try{
							String query1="update Budget set num=? where month=? and userName=? ";
							PreparedStatement ps1 = connection.prepareStatement(query1);
							ps1.setString(1,budgetField.getText());
							ps1.setString(2, monthField.getText());
							ps1.setString(3,userName);
							ps1.execute();
							JOptionPane.showMessageDialog(null, "Update Successfully");
						}catch (SQLException e1) {
							e1.printStackTrace();
							System.exit(0);
						}			
					}
				}
			}catch (SQLException e1) {
				e1.printStackTrace();
				System.exit(0);
			}
			// Close the connection
			try {
				connection.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
				System.exit(0);
			}
		}
		updateData();
	});

}
}
