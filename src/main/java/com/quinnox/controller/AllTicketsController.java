package com.quinnox.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.quinnox.model.Result;
import com.quinnox.model.Ticket;

@RestController
@RequestMapping("/allTickets")
public class AllTicketsController {
	
	@Autowired
	private MongoOperations operations;
	
	@RequestMapping(method = RequestMethod.GET,consumes = "application/json", produces = "application/json")
	public Result getAllTickets(HttpServletRequest request, HttpServletResponse response) {
		
		try{			
			return new Result("SUCCESS", operations.findAll(Ticket.class));
		} catch (Exception e) {
			return new Result("ERROR", e.getMessage());
		}	
	}
}
