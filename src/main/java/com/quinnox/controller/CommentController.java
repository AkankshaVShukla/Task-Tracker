package com.quinnox.controller;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.WriteConcern;
import com.quinnox.model.Result;
import com.quinnox.model.Ticket;

@RestController
@RequestMapping("/comments")
public class CommentController {

	@Autowired
	private MongoOperations operations;

	@RequestMapping(method = RequestMethod.PUT,consumes = "application/json", produces = "application/json")
	public Result updateComment(HttpServletRequest request, @RequestBody Ticket ticket) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
			Map comment = (Map) ticket.get("comments");
			BasicDBObject find = new BasicDBObject(
					"comments.id", ((Map<?, ?>) comment).get("id"));

			BasicDBObject set = new BasicDBObject(
					"$set", 
					new BasicDBObjectBuilder().add("comments.$.text", ((Map<?, ?>) comment).get("text")).add("comments.$.timestamp", dateFormat.format(new Date())));
			operations.getCollection("ticket").update(find, set);
			return new Result("SUCCESS", operations.find(new BasicQuery("{}").limit(20), Ticket.class));
		} catch (Exception e) {
			return new Result("ERROR", e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.DELETE,consumes = "application/json", produces = "application/json")
	public boolean deleteComment(HttpServletRequest request, @RequestBody Ticket ticket){
		System.out.println(ticket.getCommentsList().getId());
		try{
			if (ticket.get("id") == null)
				return false;
		
			Map comment = (Map) ticket.get("comments");
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(ticket.get("id")));
			BasicDBObject t =  operations.findOne(query, Ticket.class);
			BasicDBObject clientQuery = new BasicDBObject();
			clientQuery.put("id", ((Map<?, ?>) comment).get("id")); 
			BasicDBObject pullFieldValue = new BasicDBObject("comments",
			clientQuery);
			BasicDBObject pull = new BasicDBObject("$pull",	pullFieldValue);
			operations.getCollection("ticket").update(t, pull, false,false, WriteConcern.SAFE);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
}
