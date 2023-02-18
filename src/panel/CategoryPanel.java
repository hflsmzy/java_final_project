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
import util.CenterPanel;
import util.Check;

public class CategoryPanel extends WorkingPanel {

	//public static CategoryPanel instance = new CategoryPanel();

	public JTextField textField = new JTextField(15);
	public JButton addBtn = new JButton("Add");
	public JButton deleteBtn = new JButton("Delete");
	public JButton resetBtn = new JButton("Reset");

	JScrollPane scrollPane;
	JTextArea textArea = new JTextArea(10, 30);
	
//	public CategoryPanel() {
//
//		scrollPane = new JScrollPane(textArea);
//
//		JPanel southPanel = new JPanel();
//		southPanel.add(textField);
//		southPanel.add(addBtn);
//		southPanel.add(deleteBtn);
//
//		textArea.setEditable(false);
//		scrollPane = new JScrollPane(textArea);
//		add(scrollPane, BorderLayout.CENTER);
//		this.add(southPanel, BorderLayout.SOUTH);
//
//		addListener();
//	}

	public CategoryPanel(Client curClient) {
	
		super(curClient);
		scrollPane = new JScrollPane(textArea);
		
		JPanel southPanel = new JPanel();
		JPanel CenterPanel = new JPanel();
		CenterPanel.add(textField);
		southPanel.add(addBtn);
		southPanel.add(deleteBtn);
		southPanel.add(resetBtn);

		textArea.setEditable(false);
		scrollPane = new JScrollPane(textArea);
		add(scrollPane, BorderLayout.NORTH);
		this.add(CenterPanel, BorderLayout.CENTER);
		this.add(southPanel, BorderLayout.SOUTH);

		addListener();
	}
	

	@Override
	public void updateData() {
		textArea.setText("");
		// Connect to a database
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
					textArea.append("Category: "+resultSet.getString(3)+"   Spending: " + resultSet.getInt(4)+"   \n");
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
		textField.setText("");
	}

	@Override
	public void addListener() {
		addBtn.addActionListener((e) -> {
			if (!Check.checkEmpty(textField, "Category"))
			    return;
			// Connect to a database
			Connection connection = null;
			try {
				connection = DriverManager.getConnection("jdbc:sqlite:javabook.db");
			} catch (SQLException e1) {
				e1.printStackTrace();
				System.exit(0);
			}
			
			try {
				String query="select * from Category where userName=? and name=?";
				PreparedStatement ps = connection.prepareStatement(query);
				ps.setString(1,userName);
				ps.setString(2,textField.getText());
				try (ResultSet resultSet = ps.executeQuery()) {
					if(resultSet.next()) {
						JOptionPane.showMessageDialog(null, "Duplicate Category");
					}
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
				System.exit(0);
			}
			try {
				String query="insert into Category values(null,?,?,?)";
				PreparedStatement ps = connection.prepareStatement(query);
				ps.setString(1,userName);
				ps.setString(2, textField.getText());
				ps.setInt(3,0);
				ps.execute();
				JOptionPane.showMessageDialog(null, "Inserted Successfully");
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
			updateData();
		});
		deleteBtn.addActionListener((e) -> {
			if (!Check.checkEmpty(textField, "Category"))
			    return;
			// Connect to a database
			Connection connection = null;
			try {
				connection = DriverManager.getConnection("jdbc:sqlite:javabook.db");
			} catch (SQLException e1) {
				e1.printStackTrace();
				System.exit(0);
			}
		
			try {
				String query="delete from Category where userName=? and name = ?";
				PreparedStatement ps = connection.prepareStatement(query);
				ps.setString(1,userName);
				ps.setString(2, textField.getText());
				ps.execute();
				JOptionPane.showMessageDialog(null, "Deleted Successfully");
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
			updateData();
		});
		
		resetBtn.addActionListener((e) -> {
			// Connect to a database
			Connection connection = null;
			try {
				connection = DriverManager.getConnection("jdbc:sqlite:javabook.db");
			} catch (SQLException e1) {
				e1.printStackTrace();
				System.exit(0);
			}
		
			try {
				String query="update Category set amount=? where userName=? ";
				PreparedStatement ps = connection.prepareStatement(query);
				ps.setInt(1,0);
				ps.setString(2,userName);
				ps.execute();
				JOptionPane.showMessageDialog(null, "Reset Successfully");
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
			updateData();
		});
	}

}
