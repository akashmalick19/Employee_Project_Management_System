package com.example.demoEmployeeManagementProject.Exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@RestControllerAdvice
public class CustomValidationCheckingException extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        Map<String,Object> errors=new LinkedHashMap<>();
        errors.put("Timestamp ",new Date());
        errors.put("Status ",status.value());
        List<ObjectError> fieldErrors= ex.getBindingResult().getAllErrors();
        List<String> listError=new ArrayList<>();
        for(ObjectError fieldError : fieldErrors){
            String errorMessage=fieldError.getDefaultMessage();
            listError.add(errorMessage);
        }
        errors.put("Errors ",listError);
        return new ResponseEntity<Object>(errors,headers,status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers,
                                                                         HttpStatus status, WebRequest request) {


        Map<String,Object> errors=new LinkedHashMap<>();
        errors.put("Timestamp ",new Date());
        errors.put("Status ",status.value());
        errors.put("Message ","Request Method Type is not properly choose, please change the Proper Http method type.");
        errors.put("Supported Method is ",ex.getSupportedHttpMethods());
        return new ResponseEntity<Object>(errors,HttpStatus.METHOD_NOT_ALLOWED);

    }
}
