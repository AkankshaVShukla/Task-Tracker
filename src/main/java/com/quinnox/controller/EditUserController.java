package com.quinnox.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.BasicDBObject;
import com.quinnox.model.Result;
import com.quinnox.model.User;

@RestController
@RequestMapping("/editUser")
public class EditUserController {

	public EditUserController() {
		// TODO Auto-generated constructor stub
	}
	@Autowired
	private MongoOperations operations;

	@RequestMapping(method = RequestMethod.GET,consumes = "application/json", produces = "application/json")
	public Result getUserToEdit(HttpServletRequest request, HttpServletResponse response) {

		if(request.getParameter("username")!=null){
			System.out.println("In getUserToEdit() , username = "+request.getParameter("username"));
			Query query = new Query();	
			try {
				 query.addCriteria(Criteria.where("username").is(request.getParameter("username")));			
				 BasicDBObject userTobeEdited = operations.findOne(query, User.class);
				 if(userTobeEdited != null){					
					System.out.println(" Success In getUserToEdit() , username = "+request.getParameter("username"));
					return new Result("SUCCESS", userTobeEdited);			
				 } else {
					 return new Result("No User","User not found");
				 }			 
			} catch (Exception e) {
				return new Result("ERROR", e.getMessage());
			}							
		} else {
			return new Result("Failure","username is null");
		}
	}
}

