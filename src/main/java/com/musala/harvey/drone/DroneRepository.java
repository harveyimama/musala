package com.musala.harvey.drone;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DroneRepository  extends ReactiveMongoRepository<Drone,String>{

    Mono<Drone> findBySerialNumber(String serialNumber);

    Flux<Drone> findAllByStateOrState(DroneState valueOf, DroneState valueOf2);

   
}
