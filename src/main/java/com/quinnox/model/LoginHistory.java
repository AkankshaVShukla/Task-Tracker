package com.quinnox.model;

import org.springframework.data.annotation.Id;

public class LoginHistory {

	public LoginHistory() {
		// TODO Auto-generated constructor stub
	}
	
	@Id
	private String id;	
	private String tokenId;
	private String username;
	private String loginTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
}
