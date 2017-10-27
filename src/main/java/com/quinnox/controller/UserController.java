package com.quinnox.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.quinnox.model.Result;
import com.quinnox.model.User;
import com.quinnox.service.Validations;
import com.quinnox.utils.EnctryptPassword;
import com.quinnox.utils.TokenGenerator;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private MongoOperations operations;

	@RequestMapping(method = RequestMethod.POST, produces = "application/json",consumes = "application/json")
	public Result createUser(HttpServletRequest request,@RequestBody User user) {

		try {			
			if(!validateData(user)){
				return new Result("ERROR", "Bad input data");
			} else {				
				String uName = (String) user.get("username");
				user.put("username", uName.toLowerCase());// this is needed because mongoDB is case sensitive
				boolean isDuplicate = Validations.isUserDuplicate(operations,user);
				if (isDuplicate){ 
					return new Result("ERROR" , "Username already exist.");
				} else {
					String defaultPassword = "Password@123"; // Default password
					EnctryptPassword enctryptPassword = new EnctryptPassword();
					String encryptedPassword = enctryptPassword.encryptThis(defaultPassword);
					user.put("password", encryptedPassword);
											
					String uniqueToken = TokenGenerator.generateUniqueToken();
					request.setAttribute("uniqueToken", uniqueToken);
					
					operations.save(user);
					Query q1 = new BasicQuery("{}").limit(50);
					return new Result("SUCCESS", operations.find(q1, User.class));
				}				
			}			
		} catch (Exception e) {
			return new Result("ERROR", e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.GET,consumes = "application/json", produces = "application/json")
	public Result getUser(HttpServletRequest request) {/*
		Query q1;
		try {
			if (request.getParameter("userId") == null)
				 q1 = new BasicQuery("{}").limit(20);
			else 
				 q1 = new BasicQuery("{userId:\""+ request.getParameter("userId") +"\"}").limit(20);
			List<User> resourceList = operations.find(q1, User.class);
			 if(resourceList != null){					
				return new Result("SUCCESS", resourceList);
			 } else {
				 return new Result("No Users","User list is empty. Create new users");
			 }
		} catch (Exception e) {
			return new Result("ERROR", e.getMessage());
		}
	*/
		try {
			return new Result("SUCCESS", operations.findAll(User.class));
		} catch (Exception e) {
			return new Result("ERROR", e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.DELETE,consumes = "application/json", produces = "application/json")
	public Result deleteUser(HttpServletRequest request) {
		
		try {
			Query query = new Query();
			System.out.println(" username = "+request.getParameter("username"));
			if(request.getParameter("username")!= null){
				query.addCriteria(Criteria.where("username").is(request.getParameter("username").toLowerCase()));
				operations.findAndRemove(query, User.class);
				return new Result("SUCCESS", "User has been removed successfully.");
			} else {
				return new Result("ERROR", "Bad input data");
			}
		} catch (Exception e) {
			return new Result("ERROR", e.getMessage());
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT,consumes = "application/json", produces = "application/json")
	public Result updateUser(HttpServletRequest request, @RequestBody User user) {

		try {
			if (user.get("username") == null) {
				return new Result("ERROR", "Username id is blank");
			} else {
				Query query = new Query();
				query.addCriteria(Criteria.where("username").is(user.get("username")));
				
				Update update = new Update();	
				if(user.get("name") != null){
					update.set("name", user.get("name"));
				}
				if( user.get("email") != null){
					update.set("email", user.get("email"));
				}
				if( user.get("userRole") != null){
					update.set("userRole", user.get("userRole"));
				}
				operations.updateFirst(query, update, User.class);
				return new Result("SUCCESS", operations.find(new BasicQuery("{}").limit(20), User.class));
			}

		} catch (Exception e) {
			return new Result("ERROR", e.getMessage());
		}
	}

	public boolean validateData(User user) {

		if (user.get("name") == null || user.get("email") == null ||  user.get("username") == null){
			return false;
		}else {
			return true;
		}			
	}
}
