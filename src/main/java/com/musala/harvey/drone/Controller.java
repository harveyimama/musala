package com.musala.harvey.drone;

import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@RestController
@Validated
public class Controller {

  @Autowired
  private DroneHandler droneHandler;

  @GetMapping("/medication/{id}")
  Mono<ResponseEntity<?>>  getDroneMeds(@PathVariable("id") String id) {
    return  droneHandler.getDroneMedication(id)
           .map(medications-> ResponseEntity.ok(medications));
  }

  @GetMapping("/battery-life/{id}")
  Mono<ResponseEntity<?>>  getDroneBattery(@PathVariable("id") String id) {
    return Mono.just(id).flatMap(droneHandler::getDroneBatteryLife)
    .map(battery-> ResponseEntity.ok(battery));
   
  }

  @GetMapping("/available")
  ResponseEntity<Flux<?>> getAvaialbleDrone() {
   return ResponseEntity.ok( droneHandler.getAllAvailableDrones());
    
  }

  @PostMapping("")
  Mono<ResponseEntity<?>> registerDrone(@Valid @RequestBody DroneDto newDrone) {

      return droneHandler.addDrone(newDrone)
                .map(createdDrone -> ResponseEntity.ok(createdDrone));
  }

  @PutMapping("/load/{id}")
  Mono<ResponseEntity<?>> loadDrone(@PathVariable("id") String id,
      @Valid @RequestBody List<MedicationDto> medication) {
   
      return droneHandler.addMedication(medication, id)
            .map(updatedDrone -> ResponseEntity.ok(updatedDrone)); 
   
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  String handleConstraintViolationException(ConstraintViolationException e) {
    return "Validation error: " + e.getMessage();
  }

}
