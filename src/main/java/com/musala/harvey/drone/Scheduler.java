package com.musala.harvey.drone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class Scheduler {

    @Autowired
    private DroneHandler handler;

    Logger logger = LoggerFactory.getLogger(Scheduler.class);

    @Scheduled(fixedRate = Env.BATTERY_LEVELS_CHECK_TIME) 
	public void logBatteryLife() {
        System.out.println("logging started=====================");
        handler.getAllDrones().doOnNext(drone->  logger.info(drone.getId(),drone.getBatteryCapacity()));

    }
	
	

    
}
