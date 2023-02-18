package panel;

import java.awt.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;

import client.Client;

public class SpendPanel extends WorkingPanel {
	//public static SpendPanel instance = new SpendPanel();

	public JLabel monthlyBudget1 = new JLabel("Budget");
	public JLabel monthSpend1 = new JLabel("This month's spending");
	public JLabel MonthLeft1 = new JLabel("Amount left");
	public JLabel monthLeftDay1 = new JLabel("Days until next month");
	public JLabel todaySpend1 = new JLabel("Today's spending");

	public JLabel monthlyBudget2 = new JLabel("$10000");
	public JLabel monthSpend2 = new JLabel("$2300");
	public JLabel MonthLeft2 = new JLabel("$7000");
	public JLabel monthLeftDay2 = new JLabel("15days");
	public JLabel todaySpend2 = new JLabel("$100");


	String startDate;
	String endDate;
	String todayDate;
	String thismonth;
	
//public SpendPanel() {
//
//		this.setLayout(new BorderLayout());
//		this.add(center(), BorderLayout.CENTER);
//	}
//	


	public SpendPanel(Client curClient) {	
		super(curClient);
		this.setLayout(new BorderLayout());
		this.add(center(), BorderLayout.CENTER);
	}
	

	private JPanel center() {
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(6, 2));

		centerPanel.add(monthlyBudget1);
		centerPanel.add(monthlyBudget2);

		centerPanel.add(todaySpend1);
		centerPanel.add(todaySpend2);

		centerPanel.add(monthSpend1);
		centerPanel.add(monthSpend2);

		centerPanel.add(MonthLeft1);
		centerPanel.add(MonthLeft2);

		centerPanel.add(monthLeftDay1);
		centerPanel.add(monthLeftDay2);

		return centerPanel;

	}

	@Override
	public void updateData() {
		int budget = 0;
		int mspend = 0;
		int left = 0;
		int dayLeft = 0;
		int tdspend = 0;
		// Connect to a database
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:javabook.db");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		// Create a statement
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		ResultSet dateResultSet = null;
		try {
			dateResultSet = statement.executeQuery("select date('now', 'localtime')");
			todayDate = dateResultSet.getString(1);
			dateResultSet = statement.executeQuery("select strftime('%m', 'now', 'localtime')");
			thismonth = dateResultSet.getString(1);
			dateResultSet = statement.executeQuery("select date('now', 'localtime', 'start of month')");
			startDate = dateResultSet.getString(1);
			dateResultSet = statement.executeQuery("select date('now', 'localtime', 'start of month', '+1 month', '-1 day')");
			endDate = dateResultSet.getString(1);
			
		}
		catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		try {
			String query="select * from Budget where month =? and userName = ?";
			PreparedStatement ps=connection.prepareStatement(query);
			ps.setString(1, thismonth);
			ps.setString(2, userName);
			try (ResultSet resultSet = ps.executeQuery()) {
				if (resultSet.next()) {
					budget=resultSet.getInt(2);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}


		try {
			String query="select * from Record where userName = ? and date between ? and ? ";
			PreparedStatement ps=connection.prepareStatement(query);
			ps.setString(1, userName);
			ps.setString(2, startDate);
			ps.setString(3, endDate);
			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					mspend += resultSet.getInt(3);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		left = budget - mspend;
		
		try {
			String query="select * from Record where date=? and userName = ?";
			PreparedStatement ps=connection.prepareStatement(query);
			ps.setString(1, todayDate);
			ps.setString(2, userName);
			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					tdspend += resultSet.getInt(3);
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
		monthlyBudget2.setText("" + budget);
		monthSpend2.setText("" + mspend);
		MonthLeft2.setText("" + left);
		Calendar cal = Calendar.getInstance();
		dayLeft = cal.getActualMaximum(Calendar.DATE) - cal.get((Calendar.DAY_OF_MONTH));
		monthLeftDay2.setText("" + dayLeft);
		todaySpend2.setText("" + tdspend);
	}

	@Override
	public void addListener() {
		// TODO Auto-generated method stub

	}
	
}
