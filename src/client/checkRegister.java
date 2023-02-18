package client;

public class checkRegister {
	//following three method is to check whether the id is valid
	public static String checkIdPassWord(String id, String passWord) {
		String result = "";
		  if(!roughCheckId(id).equals("good")){
			  result += roughCheckId(id);
		  }
		  if(!roughCheckPassWord(id).equals("good")){
			  if(result.length() != 0) {
				  result += " and ";
			  }
			  result += roughCheckPassWord(passWord);
		  }
		  
		  return result;
	}  
	  
	private static String roughCheckPassWord(String passWord) {
		// TODO Auto-generated method stub
		if(passWord.charAt(0) == ' ') {
			return "start of password should not be empty blank";
		}
		return "good";
	}
	
	private static String roughCheckId(String input) {
		  if(input .length() > 8) {
			  return "id length is too long";
		  }
		  
		  for(int i = 0; i < input.length(); i++) {
			  char cur = input.charAt(i);
			  if(!isDigit(cur) && !isAlpha(cur)) {
				  return "Id should only contain digit or alpha";
			  }
		  }
		  
		  return "good";
	 }
	
	private static boolean isDigit(char in) {
		return in >= '0' && in <= '9'; 
	}
	
	private static boolean isAlpha(char in) {
		return (in >= 'A' && in <= 'Z') || (in >= 'a' && in <= 'z'); 
	}
	
	public static void main(String args[]) {
		System.out.println(checkIdPassWord("123", "123"));
	}
	
}
