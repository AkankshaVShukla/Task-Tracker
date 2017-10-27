package com.quinnox.service;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.BasicDBObject;
import com.quinnox.model.User;

@RestController
@RequestMapping("/validateUser")
public class Validations {

	public static boolean isUserDuplicate(MongoOperations operations, User newUser){
		
		try{
			Query query = new Query();
			String tempNewUser = (String) newUser.get("username");
			query.addCriteria(Criteria.where("username").is(tempNewUser.toLowerCase()));
			BasicDBObject user = operations.findOne(query, User.class);
		    if(user != null){
		    	return true;
		    } else {
		    	return false;
		    }
		} catch(Exception e){
			return false;
			//return new Result("ERROR", e.getMessage());
		}		
	}
}
