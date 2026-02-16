package com.umutgldn.tickethub.common.exception;

public class ResourceNotFoundException extends RuntimeException{

    public  ResourceNotFoundException(String resource, Object identifier){
        super("%s not found with identifier: %s".formatted(resource, identifier));
    }
}
