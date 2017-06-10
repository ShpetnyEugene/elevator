package com.epam.version1.beans;

import org.apache.log4j.Logger;

import static java.lang.Thread.sleep;

public class Passenger implements Runnable {

    private static final Logger log = Logger.getLogger(Passenger.class);

    private final static int DELAY = 1000;

    private String name;
    private int stopFloor;
    private int status = Status.WAITING.getStatus();
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
            log.error(e);
        }

        while (true) {
            if (currentFloor == stopFloor && status == Status.ON_ELEVATOR.getStatus()) {
                log.info(name + " left on the floor " + stopFloor);
                break;
            } else {
                Elevator elevator = building.callElevator(currentFloor);
                status = Status.ON_ELEVATOR.getStatus();
                currentFloor = elevator.takeElevator(stopFloor, currentFloor, this);
                if (currentFloor != stopFloor) {
                    status = Status.WAITING.getStatus();
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
