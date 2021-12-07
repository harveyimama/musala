package com.musala.harvey.drone;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
@Component
public class Seeder {
    @Autowired
	private Environment env;

    @Autowired
	private DroneHandler handler;

@EventListener(ApplicationReadyEvent.class)
public void seed() {
   
    loadEnv();
    seedDrones();
    
}


private void seedDrones()
{
    int droneCount  = Env.NO_OF_SEEDED_DRONES  ;
    Random random = new Random();

    while(droneCount> 0)
    {
        try{
            DroneDto drone  = new DroneDto();
            drone.setBatteryCapacity(100);
            drone.setModel(DroneModel.Heavyweight);
            drone.setSerialNumber(""+random.nextInt());
            System.out.println("Env loaded====================="+Env.DRONE_DEFAULT_CAPACITY);
            handler.addDrone(drone).subscribe();
            droneCount--;
        }catch(Exception e)
        {
            e.printStackTrace(); // to logger
        }
        
    }
}

private void loadEnv()
{
    Env.DRONE_DEFAULT_CAPACITY = Integer.parseInt(env.getProperty("droneCapacity"));
    Env.NO_OF_SEEDED_DRONES = Integer.parseInt(env.getProperty("defaultDroneProduced"));
}

}
