package com.rindus.exceptions;

public class NoAvailableException extends Exception {

	private String errorMessage;

	public NoAvailableException(String errorMessage){
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
