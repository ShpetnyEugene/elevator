package com.epam.versionOne.beans;

import org.apache.log4j.Logger;

public class Passenger extends Thread {

    private static final Logger log = Logger.getLogger(Passenger.class);

    private final static int WAITING = 0;
    private final static int ON_ELEVATOR = 1;
    private final static int DELAY = 1000;

    private String name;
    private int stopFloor;
    private int status = WAITING;
    private int currentFloor;
    private Building building;

    public Passenger(String name, int stopFloor, int currentFloor, Building building) {
        this.name = name;
        this.stopFloor = stopFloor;
        this.currentFloor = currentFloor;
        this.building = building;
    }


    public String getNamePassenger() {
        return name;
    }

    @Override
    public void run() {
        try {
            sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (true) {
            if (currentFloor == stopFloor && status == ON_ELEVATOR) {
                log.info(name + " left on the floor " + stopFloor);
                break;
            } else {
                Elevator elevator = building.callElevator(currentFloor);
                status = ON_ELEVATOR;
                currentFloor = elevator.takeElevator(stopFloor, currentFloor, this);
                if (currentFloor != stopFloor) {
                    status = WAITING;
                    building.waitForElevator();
                }
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
