package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.swing.JOptionPane;

import client.Message;
import util.Check;

public class server {

	private static List<String[]> clients = new ArrayList<>();
	List<Socket> connections;

	public server() {
		try {
			ServerSocket server = new ServerSocket(8000);

			boolean flag = true;

			while (flag) {
				System.out.print("listening...");
				Socket client = server.accept();
				connections = new ArrayList<>();
				connections.add(client);
				System.out.println("build a new connection");
				new Thread(new serveThread(client)).start();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class serveThread implements Runnable {

		private Socket client;
		ObjectInputStream inputFromClient;
		ObjectOutputStream toClient;
		boolean loginStatus;

		public serveThread(Socket client) {
			this.client = client;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			try {
				inputFromClient = new ObjectInputStream(client.getInputStream());
				toClient = new ObjectOutputStream(client.getOutputStream());
				boolean flag = true;

				while (flag) {
					Message q = (Message) inputFromClient.readObject();

					responseQuery(q);
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}

		}

		private void responseQuery(Message msg) throws IOException {
			// TODO Auto-generated method stub
			int msgType = msg.getEventType();
			switch (msgType) {
			case Message.REGISTER: {
				String[] registerData = msg.getData();
				boolean registerResult = Register(registerData[0], registerData[1]);
				Message registerReplyMessage;
				System.out.print("line86 : "+registerResult);
				if (registerResult) {
					loginStatus = true;
					registerReplyMessage = new Message(Message.REGISTER_RESULT, new String[] { "Success" });

				} else {
					registerReplyMessage = new Message(Message.REGISTER_RESULT, new String[] { "Failed" });
				}
				toClient.writeObject(registerReplyMessage);
				break;
			}
			case Message.LOGIN: {
				String[] login = msg.getData();
				boolean loginResult = Login(login[0], login[1]);
				System.out.println("line 100 : " + loginResult);
				Message loginReplyMessage;
				if (loginResult) {
					loginStatus = true;
					loginReplyMessage = new Message(Message.LOGIN_RESULT, new String[] { "Success" });
				} else {
					loginReplyMessage = new Message(Message.LOGIN_RESULT, new String[] { "Failed" });
				}
				toClient.writeObject(loginReplyMessage);

				break;
			}
			case Message.QUERY : {
				Message loginReplyMessage;
				loginReplyMessage = new Message(Message.DEFAULT_SUCCESS, responseDBQuery(msg));
				
				toClient.writeObject(loginReplyMessage);

				break;
			}
			default: {
				if (loginStatus) {
					toClient.writeObject(new Message(Message.DEFAULT_SUCCESS, responseDBQuery(msg)));
					break;
				} else {
					toClient.writeObject(new Message(Message.DEFAULT_FAIL, new String[] { "Failed : Not Login" }));
				}
			}
			}

		}

	}

	public List<Object> responseDBQuery(Message msg) {
		List<Object> result = new ArrayList<>();
		String query = msg.getData()[0];
		System.out.print("line 136 " + query);
		Connection connection = null;
		ResultSet rs = null;
		try {
			System.out.println("line141");
			connection = DriverManager.getConnection("jdbc:sqlite:javabook.db");
			System.out.println("line144");
			PreparedStatement ps1 = connection.prepareStatement(query);
			rs = ps1.executeQuery();
			System.out.println("line145" + (rs == null));
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		result.add(rs);
		System.out.println("line156 : list is null " + (result == null));
		return result;

	}
	

	public  boolean Register(String name, String password) {
		// use this name and passWord to register
		// if the name have been used return false;
		// else save this account in DB and return true;
		Connection connection = null;
		boolean result = false;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:javabook.db");
			String query1 = "select * from Account where userName=?";
			PreparedStatement ps1 = connection.prepareStatement(query1);
			ps1.setString(1, name);
			try (ResultSet rs = ps1.executeQuery()) {
				System.out.println(rs);
				if (rs.next()) {
					result = false;
				}
				else {
					System.out.print("line175 start insert");
					String query2 = "insert into Account values(?,?)";
					PreparedStatement ps2 = connection.prepareStatement(query2);
					ps2.setString(1, name);
					ps2.setString(2, password);
					ps2.execute();
					result = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return result;
	}

	public boolean Login(String name, String password) {
		// use this name and passWord to login
		// if the password is wrong return false;
		// else save this account in DB and return true;

		Connection connection = null;
		boolean result = false;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:javabook.db");
			String query = "select * from Account where userName=? and password=?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, name);
			ps.setString(2, password);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result=true;
				}
				else {
					result=false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return result;
	}

//	public boolean Login(String string, String string2) {
//		//use this name and passWord to login
//		//if the password is wrong return false; 
//		//else save this account in DB and return true;
//		return false;
//	}

	public static void main(String args[]) {
		server newServer = new server();
	}

}
