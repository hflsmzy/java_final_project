package panel;

import java.awt.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;

import client.Client;
import util.Check;

public class RecordPanel extends WorkingPanel {

	//public static RecordPanel instance = new RecordPanel();

	JLabel spend = new JLabel("Spending");
	JLabel category = new JLabel("Item Category");
	JLabel comment = new JLabel("Description");
	JLabel date = new JLabel("Date(YYYY-MM-DD)");

	public JTextField spendText = new JTextField("0");
	public JComboBox<String> categoryCb = new JComboBox<String>();
	public JTextField commentText = new JTextField();
	public JTextField dateText = new JTextField();

	public JTextField tfSpend = new JTextField("0");

	JButton submitBtn = new JButton("OK!!!");
	
//public RecordPanel() {
//
//		
//		JPanel northPanel = new JPanel(new GridLayout(4, 2, 40, 40));
//		northPanel.add(spend);
//		northPanel.add(spendText);
//
//		northPanel.add(category);
//		northPanel.add(categoryCb);
//
//		northPanel.add(comment);
//		northPanel.add(commentText);
//
//		northPanel.add(date);
//		northPanel.add(dateText);
//
//		JPanel submitPanel = new JPanel();
//		submitPanel.add(submitBtn);
//		this.addListener();
//
//		this.setLayout(new BorderLayout());
//		this.add(northPanel, BorderLayout.NORTH);
//		this.add(submitPanel, BorderLayout.CENTER);
//	}

	public RecordPanel(Client curClient) {
		
		super(curClient);
		
		
		JPanel northPanel = new JPanel(new GridLayout(4, 2, 40, 40));
		northPanel.add(spend);
		northPanel.add(spendText);

		northPanel.add(category);
		northPanel.add(categoryCb);

		northPanel.add(comment);
		northPanel.add(commentText);

		northPanel.add(date);
		northPanel.add(dateText);

		JPanel submitPanel = new JPanel();
		submitPanel.add(submitBtn);
		this.addListener();

		this.setLayout(new BorderLayout());
		this.add(northPanel, BorderLayout.NORTH);
		this.add(submitPanel, BorderLayout.CENTER);
	}

	public String getSelectedCategory() {
		return (String) categoryCb.getSelectedItem();
	}

	@Override
	public void updateData() {
		categoryCb.removeAllItems();
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:javabook.db");
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.exit(0);
		}

		try {

			String query="select * from category where userName = ?";
			PreparedStatement ps=connection.prepareStatement(query);
			ps.setString(1, userName);
			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					categoryCb.addItem(resultSet.getString(3));
				}
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}

	@Override
	public void addListener() {
		submitBtn.addActionListener((e) -> {
			if (!Check.checkPositive(spendText, "Amount"))
				return;
			// Connect to a database
			Connection connection = null;
			try {
				connection = DriverManager.getConnection("jdbc:sqlite:javabook.db");
			} catch (SQLException e1) {
				e1.printStackTrace();
				System.exit(0);
			}
			int cid = 0;
			int amount=0;
			try {
				String query="select * from category where userName = ? and name=?";
				PreparedStatement ps=connection.prepareStatement(query);
				ps.setString(1, userName);
				ps.setString(2, this.getSelectedCategory());
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						cid=resultSet.getInt(1);
						amount=resultSet.getInt(4);
						
					}
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
				System.exit(0);
			}

			String spendS = spendText.getText();
			int spend = Integer.parseInt(spendS);
			String commentS = commentText.getText();
			String dateS = dateText.getText();
			String query = "insert into Record values(?,null,?,?,?,?)";
			String query1="update Category set amount=? where id=? and userName=?";

			if (!Check.checkEmpty(dateText, "Date"))
				return;
			else if (!Check.isValid(dateS))
				JOptionPane.showMessageDialog(null, "Date must be in format");
			else {
				try {
					PreparedStatement ps = connection.prepareStatement(query);
					PreparedStatement ps1 = connection.prepareStatement(query1);
					ps.setString(1,userName);
					ps.setInt(2, spend);
					ps.setInt(3, cid);
					ps.setString(4, commentS);
					ps.setString(5, dateS);
					ps.execute();
					ps1.setInt(1,amount+spend);
					ps1.setInt(2,cid);
					ps1.setString(3,userName);
					ps1.execute();
					JOptionPane.showMessageDialog(null, "Inserted Successfully");
				} catch (SQLException e1) {
					e1.printStackTrace();
					System.exit(0);
				}
			
				}
				// Close the connection
				try {
					connection.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
					System.exit(0);
				}
			spendText.setText("0");
			commentText.setText("");
			dateText.setText("");
		});

	}
}