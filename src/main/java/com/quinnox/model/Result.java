package com.quinnox.model;

import java.util.List;

public class Result {
	
	private String status;
	private String errorMsg;
	private List listObject;
	private Object object;
	private String jsonString;
	private Integer count;
	private String userRole;
	private Object tempUser;
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public Result(String status, List listObject) {
		super();
		this.status = status;
		this.listObject = listObject;
	}
	public Result(String status, List listObject, String userRole, Object tempUser) {
		super();
		this.status = status;		
		this.listObject = listObject;
		this.setUserRole(userRole);
		this.setTempUser(tempUser);
	}
	public Result(String status, Object object) {
		super();
		this.status = status;
		this.object = object;
	}
	public Result(String status, String jsonString, Integer count) {
		super();
		this.status = status;
		this.jsonString = jsonString;
		this.count = count;
	}
	public List getListObject() {
		return listObject;
	}
	public void setListObject(List listObject) {
		this.listObject = listObject;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Result(String status, String errorMsg) {
		super();
		this.status = status;
		this.errorMsg = errorMsg;
	}
	public String getJsonString() {
		return jsonString;
	}
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	public Result() {
		// TODO Auto-generated constructor stub
	}
	public Result(String status, String errorMsg, List listObject) {
		super();
		this.status = status;
		this.errorMsg = errorMsg;
		this.listObject = listObject;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public Object getTempUser() {
		return tempUser;
	}
	public void setTempUser(Object tempUser) {
		this.tempUser = tempUser;
	}
}
