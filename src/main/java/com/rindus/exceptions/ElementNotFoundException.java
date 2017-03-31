package com.rindus.exceptions;

public class ElementNotFoundException extends Exception {

	private String errorMessage;

	public ElementNotFoundException(String errorMessage){
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
