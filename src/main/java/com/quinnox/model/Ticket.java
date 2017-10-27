package com.quinnox.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.BasicDBObject;

@Document(collection="ticket")
public class Ticket extends BasicDBObject{

	@Id
	private String id;	
	private Double ticketId;
	private String subject;
	private String description;
	private String assignedTo;
	private String priority;
	private String state;
	private List<?> comments;
	private String updatedBy;
	private String creationDate;
	private String closedDate;
	private String lastUpdateDate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Comments getCommentsList() {
		 try {
		        return new Comments((Map) get("comments"));
		    } catch(NullPointerException e) {
		        return null;
		    }
	}
	public void setCommentsList(Comments comments) {
		put("company", comments);
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getClosedDate() {
		return closedDate;
	}
	public void setClosedDate(String closedDate) {
		this.closedDate = closedDate;
	}
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public Double getTicketId() {
		return ticketId;
	}
	public void setTicketId(Double ticketId) {
		this.ticketId = ticketId;
	}
	
}
