package client;

import java.sql.ResultSet;
import java.util.*;

public class Message implements java.io.Serializable {
	public static final int DEFAULT_FAIL = -1;
	public static final int DEFAULT_SUCCESS = -2;
	public static final int REGISTER = 0;
	public static final int REGISTER_RESULT = 1;
	public static final int LOGIN = 2;
	public static final int LOGIN_RESULT = 3;
	public static final int QUERY = 4;
	public static final int QUERY_RESULT = 5;
	
	private int eventType;
	// 0 from client to server : register query ;  1 from server to client :register result. 
	// 2 from client to server : login query; 3 from server to client :login result.
	private String[] data;
	//private transient ResultSet result;
	//private transient ResultSet result;
	private transient List<Object> Objects;

	
	public Message(int eventType, String[] data) {
		this.eventType = eventType;
		this.data = data;
	}
	
//	public Message(int eventType, ResultSet result) {
//		this.eventType = eventType;
//		this.result = result;
//	}
	
	public Message(int eventType, List<Object> Objects) {
		this.eventType = eventType;
		this.Objects = Objects;
	}
	
	public Message(int eventType, String[] data, List<Object> Objects) {
		this.eventType = eventType;
		this.data = data;
		this.Objects = Objects;
	}
	
	public int getEventType() {
		return this.eventType;
	}
	
	public String[] getData() {
		return this.data;
	}
	
	public ResultSet getResult() {
		return (ResultSet) Objects.get(0);
	}
	
	public List<Object> getObjects(){
		return this.Objects;
	}
	
}
