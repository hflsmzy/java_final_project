package client;

import java.io.*;

import java.net.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class Client {

	private static String host = "localhost";
	private Socket socket;
	private static boolean flag;
	private static boolean loginStatus;
	private static String[] token;
	private static ObjectOutputStream toServer;
	private static ObjectInputStream inputFromServer;


	public Client() {
			
			
			new Thread(new ClientThread()).start();
			
			System.out.println("client built");
		
	}
	
	public boolean getLoginStatus() {
		return loginStatus;
	}
	
	class ClientThread implements Runnable{

		public ClientThread(){
		}

		@Override
		public void run() {
			try {
				Socket curSocket = new Socket(host, 8000);
				socket = curSocket;
				flag =true;
				toServer = new ObjectOutputStream(socket.getOutputStream());
				inputFromServer = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}

	
	public ResultSet queryRequest(String query){
		
		Message msg = new Message(Message.QUERY, new String [] {query});
		ResultSet resultSet = null;
		
		try {
			toServer.writeObject(msg);
			Message responseMessage = (Message) inputFromServer.readObject();

			System.out.println("line64 responseMessage is null "+ (responseMessage == null));
			resultSet = responseMessage.getResult();
			System.out.println("line68 object is null : "+ (resultSet == null));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return resultSet;

		
	}
	
	public String[] getToken() {
		return this.token;
	}

	/*
	 * client register method input: String id , String passWord,
	 *  id only contain alpha and digit and length <= 8. 
	 * First char of password should not be empty blank
	 * 
	 */
	public String clientRegister(String id, String passWord) {
		//roughCheck will return instruction about the id and passWord. if they are legal,
		//a register message will be sent to the server
		// if register success, this method will return String: "Success"
		//else if register failed because id collision will return "Failed"
		String roughCheck = checkRegister.checkIdPassWord(id, passWord);
		if (roughCheck.length() != 0) {
			return roughCheck;
		}

		String[] RegisterData = new String[] { id, passWord };
		token = RegisterData;
		Message msg = new Message(Message.REGISTER, RegisterData);
		String result = "Failed";
		try {
			toServer.writeObject(msg);
			Object object = inputFromServer.readObject();

			Message responseMessage = (Message) object;
			if(responseMessage.getEventType() == Message.REGISTER_RESULT) {
				result = responseMessage.getData()[0];
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			return result;

	}

	@SuppressWarnings("finally")
	public String clientLogin(String id, String passWord) {
		//roughCheck will return instruction about the id and passWord. if they are legal,
		//a login message will be sent to the server
		// if login success, will return String: "Success"
		//else if login failed because id collision will return "Failed"
		String roughCheck = checkRegister.checkIdPassWord(id, passWord);
		if (roughCheck.length() != 0) {
			return roughCheck;
		}

		String[] loginData = new String[] { id, passWord };
		token = loginData;
		Message msg = new Message(Message.LOGIN, loginData);
		String result = "Failed";
		try {
			toServer.writeObject(msg);
			Object object = inputFromServer.readObject();
			Message responseMessage = (Message) object;
			if(responseMessage.getEventType() == Message.LOGIN_RESULT) {
				result = responseMessage.getData()[0];
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			return result;

		
	}

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub

		Client newClient = new Client();
		//newClient.clientRegister("1", "123123123");
		while(newClient.toServer == null) {
			System.out.print(".");
		}
		ResultSet result = newClient.queryRequest("select * from Account");
	
//		ResultSetMetaData rsmd = result.getMetaData();
//		int columnsNumber = rsmd.getColumnCount();
//		while(result.next()) {
//			for(int i = 1; i <= columnsNumber; i++) {
//				System.out.print(result.getString(i) + " " + rsmd.getColumnName(i));
//			}
//		}
				
	}	
	

}



	
