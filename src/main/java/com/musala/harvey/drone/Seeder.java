package com.musala.harvey.drone;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

public class Seeder {
    @Autowired
	private Environment env;

    @Autowired
	private DroneHandler handler;

@EventListener
public void seed(ContextRefreshedEvent event) {
    loadEnv();
    seedDrones();
    
}


private void seedDrones()
{
    int droneCount  = Env.NO_OF_SEEDED_DRONES  ;
    Random random = new Random();

    while(droneCount> 0)
    {
        System.out.println("seeding started=====================");
        try{
            DroneDto drone  = new DroneDto();
            drone.setBatteryCapacity(100);
            drone.setModel(DroneModel.Heavyweight);
            drone.setSerialNumber(""+random.nextInt());
    
            handler.addDrone(drone);
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
