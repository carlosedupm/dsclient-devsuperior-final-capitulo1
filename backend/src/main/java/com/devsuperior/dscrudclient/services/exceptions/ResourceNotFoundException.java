package com.devsuperior.dscrudclient.services.exceptions;

public class ResourceNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ResourceNotFoundException(String erro) {
		super(erro);
	}

}

