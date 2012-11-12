package br.unioeste.common;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Frame implements Serializable {

	private static final long serialVersionUID = 8833179763892333272L;

	private String username;

	private String touser;

	private BufferedImage message;

	public Frame(String username, String touser, BufferedImage message) {
		setUsername(username);
		setTouser(touser);
		setMessage(message);
	}

	public BufferedImage getMessage() {
		return message;
	}

	public void setMessage(BufferedImage message) {
		this.message = message;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

}
