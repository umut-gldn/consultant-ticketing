package com.umutgldn.tickethub.common.exception;

public class AccessDeniedException extends  RuntimeException{
    public AccessDeniedException(String message){
        super(message);
    }
}
