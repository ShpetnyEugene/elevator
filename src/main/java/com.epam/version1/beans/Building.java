package com.epam.version1.beans;


import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Building {

    private final static Logger log = Logger.getLogger(Building.class);

    private int numberFloors;
    private int numberElevators;
    private List<Elevator> elevators = new ArrayList<>();

    public Building(int numberFloors, int numberElevators, int elevatorCapacity) {
        this.numberFloors = numberFloors;
        this.numberElevators = numberElevators;
        for (int i = 0; i < numberElevators; i++) {
            elevators.add(new Elevator("Elevator " + i, elevatorCapacity, 0, this, numberFloors));
        }
    }

    public void startElevators() {
        for (Elevator elevator : elevators) {
            elevator.start();
        }
    }

    public void stopElevators() {
        for (Elevator elevator : elevators) {
            elevator.stopElevator();
        }
    }

    public synchronized void tellAt() {
        notifyAll();
    }

    public synchronized Elevator callElevator(int passengerFloor) {
        while (true) {
            for (Elevator elevator : elevators) {
                if (elevator.getCurrentFloor() == passengerFloor && elevator.getCurrentVolume() < elevator.getCapacity()) {
                    return elevator;
                }
            }
            waitForElevator();
        }
    }

    public synchronized void waitForElevator() {
        try {
            wait();
        } catch (InterruptedException e) {
            log.error(e);
        }
    }
}
