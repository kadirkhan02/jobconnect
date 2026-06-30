package com.jobportal.jobconnect.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String resource,int id)
    {
        super(resource + " nahi mila jiska id hai: " + id);
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
