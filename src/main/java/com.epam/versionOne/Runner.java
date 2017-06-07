package com.epam.versionOne;

import com.epam.versionOne.beans.Building;
import com.epam.versionOne.beans.Passenger;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Runner {

    private static final Logger log = Logger.getLogger(Runner.class);
    // Number of floors in building
    private static final int NUMBER_FLOORS = 3;
    // Number of elevators in building
    private static final int NUMBER_ELEVATORS = 2;
    // Number of people that each elevator can hold
    private static final int ELEVATOR_CAPACITY = 2;
    // number of passenger in building
    private static final int NUMBER_PASSENGERS = 3;

    public static void main(String[] args) {
        List<Passenger> passengers = new ArrayList<>();

        Building building = new Building(NUMBER_FLOORS,NUMBER_ELEVATORS,ELEVATOR_CAPACITY);

        Random random = new Random();
        int startFloor;
        int endFloor;
        for (int i = 0; i < NUMBER_PASSENGERS ; i++) {
            do {
                startFloor = random.nextInt(NUMBER_PASSENGERS);
                endFloor = random.nextInt(NUMBER_PASSENGERS);
            }while (startFloor == endFloor);

            passengers.add(new Passenger("Passenger " + i,endFloor,startFloor,building));
        }

        building.startElevators();

        for(Passenger passenger: passengers){
            passenger.start();
        }

        for (Passenger passenger: passengers){
            try {
                passenger.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        building.stopElevators();
        log.info("Successful");
    }
}
