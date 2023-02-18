package panel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import client.Client;

public class ListPanel extends WorkingPanel {
	//public static ListPanel instance = new ListPanel();

	JScrollPane scrollPane;
	JTextArea textArea = new JTextArea(15, 30);

//	public ListPanel() {
//		textArea.setEditable(false);
//		scrollPane = new JScrollPane(textArea);
//		add(scrollPane);
//	}
	
	public ListPanel(Client curClient) {
		super(curClient);
		textArea.setEditable(false);
		scrollPane = new JScrollPane(textArea);
		add(scrollPane);
	}
	
	public static String convertToName(String userName,int id) {
		Connection connection = null;
		String name="";
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:javabook.db");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		try {
			String query="select * from Category where userName=? and id=?";
			PreparedStatement ps=connection.prepareStatement(query);
			ps.setString(1, userName);
			ps.setInt(2,id);
			try (ResultSet resultSet = ps.executeQuery()) {
				if(resultSet.next()) {
					name=resultSet.getString(3);
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}	
		return name;
	}

	@Override
	public void updateData() {
		textArea.setText("");
		// Connect to a database
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:javabook.db");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		try {
			String query="select * from Record where userName=? order by date asc";
			PreparedStatement ps=connection.prepareStatement(query);
			ps.setString(1, userName);
			try (ResultSet resultSet = ps.executeQuery()) {
			while (resultSet.next()) {
				textArea.append("Spend： " + resultSet.getString(3)+ "   Catogary: " + convertToName(userName,resultSet.getInt(4))+ "   Comment: " + resultSet.getString(5) + "   Date: " + resultSet.getString(6)+ "   \n");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
		// TODO Auto-generated method stub
		
	}

}
