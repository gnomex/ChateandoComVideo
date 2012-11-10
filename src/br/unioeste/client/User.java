package br.unioeste.client;

import java.io.Serializable;

public class User implements Serializable{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userName; //User name
	private String userTag; //User tag identifier
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserTag() {
		return userTag;
	}
	public void setUserTag(String userTag) {
		this.userTag = userTag;
	}
	
	

}
