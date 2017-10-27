package com.quinnox.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.BasicDBObject;
import com.quinnox.model.Result;
import com.quinnox.model.User;
import com.quinnox.utils.EnctryptPassword;

@RestController
@RequestMapping("/resetPassword")
public class ResetPasswordController {
	public ResetPasswordController() {
		// TODO Auto-generated constructor stub
	}
	@Autowired
	private MongoOperations operations;
	
	@RequestMapping(method = RequestMethod.PUT,consumes = "application/json", produces = "application/json")
	public Result resetPassword(HttpServletRequest request, @RequestBody User user) {

		try {
			if(!validateData(user)){
				return new Result("ERROR", "Username or email id is blank");
			} else {
				Query query1 = new Query();								
				query1.addCriteria(Criteria.where("username").is(user.get("username")));
				query1.addCriteria(Criteria.where("email").is(user.get("email")));
				BasicDBObject tempuser = operations.findOne(query1, User.class);
				if(tempuser!=null){
					Query query2 = new Query();
					Update update = new Update();
					EnctryptPassword enctryptPassword = new EnctryptPassword();
					String encryptedPassword = enctryptPassword.encryptThis("Password@123"); // Default Password
					update.set("password", encryptedPassword);
					operations.updateFirst(query2, update, User.class);
					return new Result("SUCCESS", "Password has been reset successfully..!!");
				} else {
					return new Result("ERROR", "Invalide username or email");
				}
			}							
		} catch (Exception e) {
			return new Result("ERROR", e.getMessage());
		}
	}
	public boolean validateData(User user) {
		if (user.get("username") == null || user.get("email") == null ){
			return false;
		} else {
			return true;
		}
	}
}
