package com.jobportal.jobconnect.exception;

public class DuplicateEmailException extends RuntimeException{

    public DuplicateEmailException (String email)
    {
        super("This email is already exists"+email);
    }
}
