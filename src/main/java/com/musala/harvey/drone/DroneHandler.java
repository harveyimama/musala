package com.musala.harvey.drone;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DroneHandler {

    @Autowired
    private DroneRepository droneRepo;

    public Mono<?> addDrone(final DroneDto drone)  {

      return  droneRepo.findBySerialNumber(drone.getSerialNumber())
       .flatMap(newDrone->{
        if (newDrone == null) {
            return droneRepo.save(new Drone(drone));
        } else
           return Mono.just(new DroneException("DUPLICATE", "Drone already created"));
       });

    }

    public Mono<?> addMedication(final List<MedicationDto> medications, final String id)  {

       double  totalweight = this.getMedicationWeight(medications);
        return   droneRepo.findById(id).handle(
                (drone,sink) -> {
                    Exception exp  = validateDrone(drone,totalweight);
                    if (exp == null) {
                        sink.next(this.addMedicationToDrone(drone,medications,totalweight));
                    } else
                    sink.error (exp);
                });
    }


    public Mono<?> getDroneMedication(final String id) {
        return droneRepo.findById(id).map(drone -> {
                return drone.getMedications();
        });
    }

    public Flux<Drone> getAllAvailableDrones() {
        return droneRepo.findAllByStateOrState(DroneState.valueOf("IDEAl"),DroneState.valueOf("LOADING"));
    }

    public Mono<Integer> getDroneBatteryLife(final String id) {
        return droneRepo.findById(id).map(drone -> {
            return drone.getBatteryCapacity();
    });
    }



    class DroneException extends Exception {

        private String code;

        public DroneException(String code, String message) {
            super(message);
            this.setCode(code);
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

    }

    private Mono<Drone> addMedicationToDrone( Drone drone, final List<MedicationDto> medications, 
    final  double totalweight) {
           
            List<MedicationDto> currentMedications = drone.getMedications();

            for( MedicationDto med : medications)
            currentMedications.add(med);

            drone.setCurrentLimit(drone.getCurrentLimit()+totalweight);

            if(drone.getCurrentLimit() == drone.getWeightLimit())
            drone.setState(DroneState.valueOf("LOADED"));

            return droneRepo.save(drone);

    }

    private Exception  validateDrone(final Drone drone,final double totalweight) {

        if ( drone.getState() == DroneState.valueOf("IDLE") || drone.getState() == DroneState.valueOf("LOADED") )
           return new DroneException("DUPLICATE", "Drone already created");
        else if (drone.getBatteryCapacity() > 25 )
        return new DroneException("DUPLICATE", "Drone already created");
        else if ( drone.getCurrentLimit() > totalweight)
        return new DroneException("DUPLICATE", "Drone already created");
        else      
        return null;
    }

    private double getMedicationWeight( final List<MedicationDto> medications) {
           
           double weight = 0;

            for( MedicationDto med : medications)
             weight = weight+ med.getWeight();

            return weight;
    }


}
