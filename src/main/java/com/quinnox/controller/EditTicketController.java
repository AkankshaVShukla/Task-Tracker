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
import com.quinnox.model.Ticket;


@RestController
@RequestMapping("/editTicket")
public class EditTicketController {

	public EditTicketController() {
		// TODO Auto-generated constructor stub
	}
	@Autowired
	private MongoOperations operations;

	@RequestMapping(method = RequestMethod.GET,consumes = "application/json", produces = "application/json")
	public Result getTicketToEdit(HttpServletRequest request, HttpServletResponse response) {
		
		Double ticketId ;
		if(request.getParameter("ticketId")!=null){
			ticketId =  Double.valueOf((String) request.getParameter("ticketId"));
			System.out.println(" Ticket to be edited is = "+ticketId);

			if(ticketId != null ){		
				Query query = new Query();	
				try {
					 query.addCriteria(Criteria.where("ticketId").is(ticketId));			
					 BasicDBObject ticketTobeEdited = operations.findOne(query, Ticket.class);
					 System.out.println("operations.findOne(query, Ticket.class)="+operations.findOne(query, Ticket.class));
					 System.out.println("operations.find(query, Ticket.class)="+operations.find(query, Ticket.class));
					 if(ticketTobeEdited != null){					
						return new Result("SUCCESS", ticketTobeEdited);
					 } else {
						 return new Result("No Ticket","Ticket not found");
					 }
					 
				} catch (Exception e) {
					return new Result("ERROR", e.getMessage());
				}
			} else {
				return new Result("Failure","Ticket Id is null");
			}	
		} else {
			return new Result("Failure","Ticket Id is null");
		}
	}
}
