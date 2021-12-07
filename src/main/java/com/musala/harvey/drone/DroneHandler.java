package com.musala.harvey.drone;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DroneHandler {

    @Autowired
    private DroneRepository droneRepo;

    public Mono<DroneResponse<Drone>> addDrone(final DroneDto drone) {
        return droneRepo.save(new Drone(drone))
        .onErrorReturn(new Drone()).map(newDrone->{
            if (null == newDrone.getId())
            return new DroneResponse<>("Drone already added", null,400);
              else
            return new DroneResponse<>("Success", newDrone);
        });
    }

    public Mono<DroneResponse> addMedication(final List<MedicationDto> medications, final String id) {

        double totalweight = this.getMedicationWeight(medications);
        return droneRepo.findById(id)
                .switchIfEmpty(Mono.just(new Drone()))
                .flatMap(
                        drone -> {
                            Exception exp = validateDrone(drone, totalweight);
                            if (exp == null) {
                                return addMedicationToDrone(drone, medications, totalweight)
                                 .map(updatedDrone-> {
                                     if(updatedDrone.getClass()==Exception.class)
                                     return new DroneResponse<>("Success", updatedDrone,500);
                                     else
                                     return new DroneResponse<>("Success", updatedDrone);

                                    });
                            } else
                                return Mono.just(new DroneResponse<>("Validation Error", exp,400));
                        });
    }

    public Mono<?> getDroneMedication(final String id) {
        return droneRepo.findById(id)
                .switchIfEmpty(Mono.just(new Drone()))
                .map(drone -> {
                    if (null == drone.getId())
                        return new DroneResponse<>("Drone not found", null);
                    else if (null != drone.getMedications())
                        return new DroneResponse<>("Success", drone.getMedications());
                    else
                        return new DroneResponse<>("No Medication found", null);
                });
    }

    public Flux<Drone> getAllAvailableDrones() {
        return droneRepo.findAllByStateOrState(DroneState.IDLE, DroneState.LOADING);
    }

    public Mono<?> getDroneBatteryLife(final String id) {
        return droneRepo.findById(id)
                .switchIfEmpty(Mono.just(new Drone()))
                .map(drone -> {
                    if (null == drone.getId())
                    return new DroneResponse<>("Drone not found", null);
                      else 
                    return new DroneResponse<>("Success", "" + drone.getBatteryCapacity());
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

    private Mono<?> addMedicationToDrone(Drone drone, final List<MedicationDto> medications,
            final double totalweight) {

                try{
                    List<MedicationDto> currentMedications = null;
                    if (null == drone.getMedications())
                        currentMedications = new ArrayList<MedicationDto>();
                    else
                        currentMedications = drone.getMedications();
            
                    for (MedicationDto med : medications)
                        currentMedications.add(med);
            
                    drone.setCurrentLimit(drone.getCurrentLimit() + totalweight);
                    drone.setMedications(currentMedications);
            
                    if (drone.getCurrentLimit() == drone.getWeightLimit())
                        drone.setState(DroneState.valueOf("LOADED"));
            
                    return droneRepo.save(drone);
                } catch (Exception e)
                {
                    e.printStackTrace(); //send to logs
                    return Mono.just(new DroneException("Execution", "Error occoured while loading meds")); 
                }
       

    }

    private Exception validateDrone(final Drone drone, final double totalweight) {

        if(null == drone.getId())
        return new DroneException("STATE", "Drone not found");
        else if (drone.getState() != DroneState.IDLE && drone.getState() != DroneState.LOADING)
            return new DroneException("STATE", "Drone not in loaing state");
        else if (drone.getBatteryCapacity() < 25)
            return new DroneException("Battery", "Drone Battery depleted");
        else if (drone.getCurrentLimit() + totalweight > drone.getWeightLimit())
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

    public class DroneResponse<T> {
        private String message;
        private T data;
        private int status;

        public DroneResponse(final String messsage, final T data) {
            this.message = messsage;
            this.data = data;
            this.status = 200;
        }

        public DroneResponse(final String messsage, final T data,final int status) {
            this.message = messsage;
            this.data = data;
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(final String message) {
            this.message = message;
        }

        public T getData() {
            return data;
        }

        public void setData(final T data) {
            this.data = data;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(final int status) {
            this.status = status;
        }

        

    }

}
