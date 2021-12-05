package com.musala.harvey.drone;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ValidationHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
      ValidationErrorResponse error = new ValidationErrorResponse();
      for (ConstraintViolation violation : e.getConstraintViolations()) {
        error.getViolations().add(new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
      }
      return error;
    }
  
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
      ValidationErrorResponse error = new ValidationErrorResponse();
      for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
        error.getViolations().add(new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
      }
      return error;
    }
  

    class ValidationErrorResponse {

      private List<Violation> violations = new ArrayList<>();
  
    public List<Violation> getViolations() {
      return violations;
    }
  
    public void setViolations(List<Violation> violations) {
      this.violations = violations;
    }
      
  }

  class Violation {

    private final String field;

  private final String message;

  public Violation(String field, String message) {
    this.field = field;
    this.message = message;
  }

  public String getField() {
    return field;
  }

  public String getMessage() {
    return message;
  }
    
}
    
}
