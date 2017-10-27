package com.quinnox.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.BasicDBObject;
import com.quinnox.dao.TrackLoginHistory;
import com.quinnox.model.Result;
import com.quinnox.model.User;
import com.quinnox.utils.EnctryptPassword;
import com.quinnox.utils.TokenGenerator;

@RestController
@RequestMapping("/login")
public class LoginController {

	public LoginController() {
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private MongoOperations operations;

	@RequestMapping(method = RequestMethod.POST,consumes = "application/json", produces = "application/json")
	public Result login(HttpServletRequest request, HttpServletResponse response, @RequestBody User user) {

		if(user.get("username")==null){
			System.out.println("Username can not be null.");
			return new Result("ERROR", "Username can not be null.");			
		} else if (user.get("password")==null){
			System.out.println("Password can not be null.");
			return new Result("ERROR", "Password can not be null.");
		} else{
			EnctryptPassword enctryptPassword = new EnctryptPassword();
			String pw = (String) user.get("password");
			String encryptedPassword = enctryptPassword.encryptThis(pw);		

			try {
				//q1 = new BasicQuery("{username:\""+user.getUsername()+"\" , password:\""+encryptedPassword+"\"}").limit(20);
				Query query = new Query();
				String uName = (String) user.get("username");
				query.addCriteria(Criteria.where("username").is(uName.toLowerCase()));
				query.addCriteria(Criteria.where("password").is(encryptedPassword));
				
			    BasicDBObject u = operations.findOne(query, User.class);
			    if(u != null){
			    	/*Cookie usernameCk = new Cookie("username", user.getUsername());
			    	usernameCk.setMaxAge(1800);
				    response.addCookie(usernameCk); */
				    
			    	/*Cookie passwordCk = new Cookie("password", user.getPassword());
			    	passwordCk.setMaxAge(1800);
				    response.addCookie(passwordCk);*/
			    	request.getSession().setAttribute("username", user.get("username"));
			    	user.put("userRole",u.get("userRole"));//This is to send userRole on home page
			    	user.put("name",u.get("name"));
			    	//System.out.println(" role = "+u.get("userRole")+" "+user.get("userRole"));
			    	request.getSession().setAttribute("userRole", u.get("userRole"));	
			    	
			    	String uniqueToken = TokenGenerator.generateUniqueToken();
				    response.setHeader("uniqueToken", uniqueToken);			    			    			    
				    Cookie ck = new Cookie("tokenId", uniqueToken); //creating cookie object  
				    ck.setMaxAge(30);
				    response.addCookie(ck);//adding cookie in the response  
				    System.out.println("uniqueToken = "+uniqueToken);
				    //request.getSession().setAttribute("tokenId", uniqueToken);
				   // System.out.println("tokenId from session in LoginController = "+request.getSession().getAttribute("tokenId"));
				    
				    TrackLoginHistory loginHistory = new TrackLoginHistory();
				    loginHistory.persistLoginHistory(uniqueToken, uName.toLowerCase(), operations);			    	

			    	Object tempUser = user;
			    	request.getSession().setAttribute("loggedInUser", tempUser);
			    	
				    return new Result("SUCCESS", user);
			    } else { 
			    	return new Result("FAILURE", user);
			    }
			} catch (Exception e) {
				return new Result("ERROR", e.getMessage());
			}
		}
	}
	
	@RequestMapping(method = RequestMethod.GET,consumes = "application/json", produces = "application/json")
	public Result logout(HttpServletRequest request, HttpServletResponse response){
		
		Cookie ck = new Cookie("tokenId",null);
    	//ck.setPath("/TaskTracker");
    	ck.setMaxAge(0);
    	//ck.setHttpOnly(true);
		response.addCookie(ck);
		HttpSession session = request.getSession(false);
		if (session != null) {
		    session.invalidate();
		}
		//System.out.println("**************Name = "+ck.getName()+"       Value = "+ck.getValue()+"   timeout = "+ck.getMaxAge());      
		return new Result("SUCCESS", "Logout Sucessful..!!");
	}		
}
