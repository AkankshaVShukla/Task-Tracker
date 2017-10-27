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
@RequestMapping("/changePassword")
public class ChangePasswordController {

	public ChangePasswordController() {
		// TODO Auto-generated constructor stub
	}
	@Autowired
	private MongoOperations operations;
	
	@RequestMapping(method = RequestMethod.PUT,consumes = "application/json", produces = "application/json")
	public Result changePassword(HttpServletRequest request, @RequestBody User user) {

		try {
			if(!validateData(user)){
				return new Result("ERROR", "Username or password is blank");
			} else if (user.get("newPassword") == null || user.get("confirmPassword") == null) {
				return new Result("ERROR", "New Password or confirm Password is blank");
			} else if(!user.get("newPassword").toString().equals(user.get("confirmPassword").toString())){
				return new Result("ERROR", "New password and confirm password does not match.");
			} else {
				Query query1 = new Query();
				EnctryptPassword enctryptPassword = new EnctryptPassword();
				String encryptedOldPassword = enctryptPassword.encryptThis(user.get("oldPassword").toString());			
				query1.addCriteria(Criteria.where("username").is(user.get("username")));
				query1.addCriteria(Criteria.where("password").is(encryptedOldPassword));
				BasicDBObject tempuser = operations.findOne(query1, User.class);
				if(tempuser!=null){
					Query query2 = new Query();
					Update update = new Update();	
					String encryptedConfimedPassword = enctryptPassword.encryptThis(user.get("confirmPassword").toString());
					update.set("password", encryptedConfimedPassword);
					operations.updateFirst(query2, update, User.class);
					return new Result("SUCCESS", "Password has been changed successfully..!!");
				} else {
					return new Result("ERROR", "Invalide username or password");
				}
			}							
		} catch (Exception e) {
			return new Result("ERROR", e.getMessage());
		}
	}
	public boolean validateData(User user) {
		if (user.get("username") == null || user.get("oldPassword") == null){
			return false;
		}else {
			return true;
		}
	}
}
