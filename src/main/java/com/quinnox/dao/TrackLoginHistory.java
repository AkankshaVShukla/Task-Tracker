package com.quinnox.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.mongodb.core.MongoOperations;
import com.quinnox.model.LoginHistory;

public class TrackLoginHistory {

	public TrackLoginHistory() {
		// TODO Auto-generated constructor stub
	}

	public void persistLoginHistory(String uniqueToken, String userName, MongoOperations operations) {
		
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
			LoginHistory loginHistory = new LoginHistory();
			loginHistory.setTokenId(uniqueToken);
			loginHistory.setUsername(userName);
			loginHistory.setLoginTime(dateFormat.format(new Date()));
			operations.save(loginHistory);
					
			/*Query q1;
			//String a = "103faac5de5dea0ab9c80bf2f9f18930";
			 q1 = new BasicQuery("{tokenId:\""+uniqueToken+"\"}").limit(20);
			 List<Login> resourceList = operations.find(q1, Login.class);
			 Iterator<Login> temp = resourceList.iterator();
			 Login loginDocument = null;		 
			 while(temp.hasNext()){
				 loginDocument = temp.next();					
			 }
			 String a = loginDocument.getLoginTime();*/
			 //System.out.println(" ****** "  +a);							
		} catch (Exception e){
			System.out.println(e);
		}
	}
}
