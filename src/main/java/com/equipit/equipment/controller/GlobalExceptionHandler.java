package com.equipit.equipment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.equipit.equipment.exception.EquipmentAvailableException;
import com.equipit.equipment.exception.EquipmentNotFoundException;
import com.equipit.equipment.exception.EquipmentUnavailableException;

@RestControllerAdvice 
public class GlobalExceptionHandler {

    @ExceptionHandler(EquipmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String catchNotFound(EquipmentNotFoundException ex) {
       return ex.getMessage();
    }

    @ExceptionHandler({ EquipmentAvailableException.class, EquipmentUnavailableException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String catchBadRequest(RuntimeException ex) {
       return ex.getMessage();
    }

}
