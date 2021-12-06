package com.musala.harvey.drone;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DroneHandler {

    @Autowired
    private DroneRepository droneRepo;

    public Mono<Drone> addDrone(final DroneDto drone) {
        return droneRepo.save(new Drone(drone));

    }

    public Mono<?> addMedication(final List<MedicationDto> medications, final String id) {

        double totalweight = this.getMedicationWeight(medications);
        return droneRepo.findById(id).handle(
                (drone, sink) -> {
                    Exception exp = validateDrone(drone, totalweight);
                    if (exp == null) {
                        sink.next(this.addMedicationToDrone(drone, medications, totalweight));
                    } else
                        sink.error(exp);
                });
    }

    public Mono<?> getDroneMedication(final String id) {
        return droneRepo.findById(id).map(drone -> {
            if(null != drone.getMedications())
            return new DroneResponse<>("Success",drone.getMedications());
            else 
            return new DroneResponse<>("No Medication found",null);
        });
    }

    public Flux<Drone> getAllAvailableDrones() {
        return droneRepo.findAllByStateOrState(DroneState.IDLE, DroneState.LOADING);
    }

    public Mono<DroneResponse<String>> getDroneBatteryLife(final String id) {
        return droneRepo.findById(id).map(drone -> {
            return new DroneResponse<>("Success",""+drone.getBatteryCapacity());

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

    private Mono<Drone> addMedicationToDrone(Drone drone, final List<MedicationDto> medications,
            final double totalweight) {

        List<MedicationDto> currentMedications = drone.getMedications();

        for (MedicationDto med : medications)
            currentMedications.add(med);

        drone.setCurrentLimit(drone.getCurrentLimit() + totalweight);

        if (drone.getCurrentLimit() == drone.getWeightLimit())
            drone.setState(DroneState.valueOf("LOADED"));

        return droneRepo.save(drone);

    }

    private Exception validateDrone(final Drone drone, final double totalweight) {

        if (drone.getState() != DroneState.IDLE && drone.getState() != DroneState.LOADING)
            return new DroneException("STATE", "Drone not in loaing state");
        else if (drone.getBatteryCapacity() < 25)
            return new DroneException("Battery", "Drone Battery depleted");
        else if (drone.getCurrentLimit() > totalweight)
            return new DroneException("Capacity", "Drone capacity maxed");
        else
            return null;
    }

    private double getMedicationWeight(final List<MedicationDto> medications) {

        double weight = 0;

        for (MedicationDto med : medications)
            weight = weight + med.getWeight();

        return weight;
    }


    class DroneResponse<T>{
        private String message;
        private T data;

        public DroneResponse(final String messsage,T data)
        {
            this.message = messsage;
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage( final String message) {
            this.message = message;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
        
        
    }

}
