package com.epam.version1;

import com.epam.version1.beans.Building;
import com.epam.version1.beans.Passenger;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Runner {

    private static final Logger log = Logger.getLogger(Runner.class);
    /**
     * Number of floors in building
     */
    private static final int NUMBER_FLOORS = 3;
    /**
     * Number of elevators in building
     */
    private static final int NUMBER_ELEVATORS = 2;
    /**
     * Number of people that each elevator can hold
     */
    private static final int ELEVATOR_CAPACITY = 2;
    /**
     * Number of passenger in building
     */
    private static final int NUMBER_PASSENGERS = 3;

    public static void main(String[] args) {
        List<Thread> passengersThread = new ArrayList<>();

        Building building = new Building(NUMBER_FLOORS, NUMBER_ELEVATORS, ELEVATOR_CAPACITY);

        Random random = new Random();
        int startFloor;
        int endFloor;
        for (int i = 0; i < NUMBER_PASSENGERS; i++) {
            do {
                startFloor = random.nextInt(NUMBER_PASSENGERS);
                endFloor = random.nextInt(NUMBER_PASSENGERS);
            } while (startFloor == endFloor);

            passengersThread.add(new Thread(new Passenger("Passenger " + i, endFloor, startFloor, building)));
        }


        passengersThread.forEach(Thread::start);

        building.startElevators();

        for (Thread passenger : passengersThread) {
            try {
                passenger.join();
            } catch (InterruptedException e) {
                log.error(e);
                e.printStackTrace();
            }
        }
        building.stopElevators();
        log.info("Successful");
    }
}
