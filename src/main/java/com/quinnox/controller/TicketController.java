package com.quinnox.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.quinnox.model.Result;
import com.quinnox.model.Ticket;
import com.quinnox.model.TicketState;
import com.quinnox.model.User;
import com.quinnox.utils.HeaderInfo;
import com.quinnox.utils.TicketIdGenerator;
import com.quinnox.utils.TokenGenerator;

@RestController
@RequestMapping("/ticket")
public class TicketController {

	@Autowired
	private MongoOperations operations;

	@RequestMapping(method = RequestMethod.POST, produces = "application/json",consumes = "application/json")
	public Result createTicket(HttpServletRequest request,@RequestBody Ticket ticket) {

		System.out.println("We are in create ticket");
		if (!validateData(ticket))
			return new Result("ERROR", "Bad input data");
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
			ticket.setCreationDate(dateFormat.format(new Date()));			
			
			Double newTicketId = TicketIdGenerator.generateNewTicketId(); // auto-generate ticket id 
			//ticket.setTicketId(newTicketId);
			ticket.put("ticketId", newTicketId);
			ticket.put("creationDate", dateFormat.format(new Date()));
			ticket.put("state", TicketState.NEW);
			
			operations.save(ticket);
			Query q1 = new BasicQuery("{}");

			return new Result("SUCCESS", operations.find(q1, Ticket.class));
		} catch (Exception e) {
			return new Result("ERROR", e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.GET,consumes = "application/json", produces = "application/json")
	public Result getTicket(HttpServletRequest request, HttpServletResponse response) {
		
		/*Query q1 = new BasicQuery("{}").limit(100);	
		return new Result("SUCCESS", operations.find(q1, Ticket.class));*/
		
		Query query = new Query();	
		String username = (String) request.getSession().getAttribute("username");		
		System.out.println(" request.getAttribute- username ="+username);
		if(username != null ){
			System.out.println("We are in GET of the ticketController and username = "+username);
			try {
				 query.addCriteria(Criteria.where("assignedTo").is(username.toLowerCase()));			
				 List<Ticket> resourceList = operations.find(query, Ticket.class);
				 if(resourceList != null){
					 System.out.println("loggedInUser=  "+request.getSession().getAttribute("loggedInUser"));
					return new Result("SUCCESS", resourceList, (String) request.getSession().getAttribute("userRole"),
							request.getSession().getAttribute("loggedInUser"));
				 } else {
					 return new Result("No Tickets","Assign some tickets");
				 }
				 
			} catch (Exception e) {
				return new Result("ERROR", e.getMessage());
			}
		} else {
			return new Result("Cookie Expired","Please login once again");
		}		
	}

	@RequestMapping(method = RequestMethod.PUT,consumes = "application/json", produces = "application/json")
	public Result updateTicket(HttpServletRequest request, @RequestBody Ticket ticket) {

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
			if(ticket.get("state") != null && "Close".equalsIgnoreCase((String) ticket.get("state"))){
				ticket.setClosedDate(dateFormat.format(new Date()));
			}
			Query query = new Query();
			String ticketId = (String) ticket.get("ticketId");
			Double ticketIdInDouble = Double.parseDouble(ticketId);			

			query.addCriteria(Criteria.where("ticketId").is(ticketIdInDouble));
			//query.addCriteria(Criteria.where("ticketId").is(ticket.get("ticketId")));
			
			query.fields().include("ticketId");

			Update update = new Update();
			if(ticket.get("subject") != null){
				update.set("subject", ticket.get("subject"));
			}
			if( ticket.get("assignedTo") != null){
				update.set("assignedTo", ticket.get("assignedTo"));
			}
			if(ticket.get("description") != null){
				update.set("description", ticket.get("description"));
			}
			if(ticket.get("priority") != null){
				update.set("priority", ticket.get("priority"));
			}
			if(ticket.get("state") != null){
				update.set("state", ticket.get("state"));		
			}
			update.set("lastUpdateDate", dateFormat.format(new Date()));
			//System.out.println(ticket.get("comments"));
			if(ticket.get("comments") != null){
				addComments((Map) ticket.get("comments"), (String)ticket.get("id"));
			}
			operations.updateFirst(query, update, Ticket.class);
			return new Result("SUCCESS", operations.find(new BasicQuery("{}").limit(100), Ticket.class));
		} catch (Exception e) {
			return new Result("ERROR", e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.DELETE,consumes = "application/json", produces = "application/json")
	public Result deleteTicket(HttpServletRequest request){
		Double ticketId = null ;
		System.out.println(" Ticket to be Deleted is = "+request.getParameter("ticketId"));
		if(request.getParameter("ticketId")!=null){
			ticketId =  Double.valueOf((String) request.getParameter("ticketId"));
			
			try{
				if (ticketId == null){
					return new Result("Failure","Ticket Id is null");
				} else {
					Query query = new Query();
					query.addCriteria(Criteria.where("ticketId").is(ticketId));
					operations.findAndRemove(query, Ticket.class);
					return new Result("SUCCESS", "Ticket has been removed successfully..!!");
				}		
			}
			catch (Exception e) {
				return new Result("ERROR", e.getMessage());
			}
		} else {
			return new Result("Failure","Ticket Id is null");
		}	
	}

	public void addComments(Map comments, String id) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		Update args = new Update();
		comments.put("id", TokenGenerator.generateUniqueToken());
		comments.put("timestamp", dateFormat.format(new Date()));
		args.addToSet("comments",comments);
		
		Query query = new Query(Criteria.where("id").is(id));
		// if u want to do upsert 
		//operations.findAndModify(query, args, FindAndModifyOptions.options().upsert(true), Ticket.class);

		//if u want to just update
		operations.findAndModify(query, args, Ticket.class);
	}
	private boolean validateData(Ticket ticket) {
		if(ticket.get("subject") != null /*&& ticket.getDescription() != null && ticket.getPriority() != null && ticket.getAssignedTo() != null*/)
			return true;
		
		return false; //else
	}
	private String getUsernameFromCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		//String cookieName =null;
		String cookieValue = null;
		if(cookies != null) {
			Cookie ck;
			for (int i = 0; i < cookies.length; i++) {
				ck=cookies[i];
		    	  if(ck.getName().equalsIgnoreCase("username")){
		    		  //cookieName = ck.getName();
			          cookieValue = ck.getValue();
		    	  }		    	  	          
		    }		      
		}		 
		 return cookieValue;		
	}	
}
