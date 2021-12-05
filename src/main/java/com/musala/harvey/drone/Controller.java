package com.musala.harvey.drone;

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
  Mono<?> registerDrone(@Valid @RequestBody DroneDto newDrone) {

    try {

      return droneHandler.addDrone(newDrone).map(ret -> {
        if (ret.getClass() == DroneException.class)
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ret);
          else if (ret.getClass() == Exception.class)
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ret);
          else
          return ResponseEntity.status(HttpStatus.OK).body(ret);
      }).cast(ResponseEntity.class);

    } catch (Exception e) {
      return Mono.just(ResponseEntity.internalServerError().build());
    }

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
