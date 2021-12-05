package com.musala.harvey.drone;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MedicationRepository extends ReactiveMongoRepository<Medication,String>{
    
}
