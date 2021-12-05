package com.musala.harvey.drone;

import java.net.URI;
import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import com.musala.harvey.drone.DroneHandler.DroneException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;

import ch.qos.logback.core.joran.conditional.ElseAction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties.RSocket;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@RestController
@Validated
public class Controller {

  @Autowired
  private DroneHandler droneHandler;

  @GetMapping("/medication/{id}")
  ResponseEntity<Mono<?>> getDroneMeds(@PathVariable("id") String id) {
    Mono<?> droneflux = droneHandler.getDroneMedication(id);
    return ResponseEntity.ok(droneflux);
  }

  @GetMapping("/battery-life/{id}")
  ResponseEntity<Mono<Integer>> getDroneBattery(@PathVariable("id") String id) {
    Mono<Integer> droneflux = Mono.just(id).flatMap(droneHandler::getDroneBatteryLife);
    return ResponseEntity.ok(droneflux);
  }

  @GetMapping("/available")
  ResponseEntity<Flux<Drone>> getAvaialbleDrone() {
    Flux<Drone> droneflux = droneHandler.getAllAvailableDrones();
    return ResponseEntity.ok(droneflux);
  }

  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  Mono<Drone> registerDrone(/*@Valid*/ @RequestBody DroneDto newDrone) {
   // try {

      return droneHandler.addDrone(newDrone);
      
      /*.flatMap(resp-> {
       if(resp.getClass() == Exception.class)
       return ServerResponse.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(resp));
       else
       return ServerResponse.status(HttpStatus.ACCEPTED).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(resp));
      });

    } catch (Exception e) {
      return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(e));
    }*/

  }

  @PutMapping("/load/{id}")
  ResponseEntity<Mono<?>> loaDrone(@PathVariable("id") String id,
      @Valid @RequestBody List<MedicationDto> medication) {

    try {
      Mono<?> droneflux = droneHandler.addMedication(medication, id);
      return ResponseEntity.ok(droneflux);
    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  String handleConstraintViolationException(ConstraintViolationException e) {
    return "Validation error: " + e.getMessage();
  }

}
