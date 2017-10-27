package com.quinnox.utils;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class TicketIdGenerator {

	public TicketIdGenerator() {
		// TODO Auto-generated constructor stub
	}
	public Double getHighestTicketId(){
		return null;		
	}
	public Double generateCurrentTicketId(){
		return null;		
	}	
/*
	@Autowired
	private MongoOperations operations;*/

	public static Double generateNewTicketId() throws java.net.UnknownHostException {

		Double lastTicketId = 0.0;
		MongoClient m1 = new MongoClient("localhost");

		DB d1 = m1.getDB("test");
		DBCollection coll = d1.getCollection("ticket");		
		DBCursor ticketCollection = coll.find();

		// sorting the cursor in descending order based on ticketId field
		ticketCollection.sort(new BasicDBObject("ticketId", -1));
		
		DBObject dbObject;
		try {
			while(ticketCollection.hasNext()){
				dbObject = ticketCollection.next();
				lastTicketId = (Double) dbObject.get("ticketId");
				break;
			}		
		}
		finally {
			ticketCollection.close();
		}
		return lastTicketId + 1;
	}
}
