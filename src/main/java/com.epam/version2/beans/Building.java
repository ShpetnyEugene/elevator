package com.epam.version2.beans;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Building {

    private static final int X_COORDINATE_ELEVATOR = 600;
    private static final int Y_COORDINATE_ELEVATOR = 400;
    private static final int START_FLOOR = 1;

    private static final Logger log = Logger.getLogger(Building.class);
    private Elevator elevator;
    private List<Floor> floors = new CopyOnWriteArrayList<>();

    private int numberFloors;
    private int elevatorCapacity;
    private int numberPassengers;

    public Building(int numberFloors, int elevatorCapacity,int numberPassengers) {
        this.numberFloors = numberFloors;
        this.elevatorCapacity = elevatorCapacity;
        this.numberPassengers = numberPassengers;
        elevator = new Elevator("Elevator: ",elevatorCapacity,START_FLOOR,this,numberFloors,X_COORDINATE_ELEVATOR,Y_COORDINATE_ELEVATOR);
    }

    public synchronized void tellAt() {
        notifyAll();
    }

    public void setElevator(Elevator elevator) {
        this.elevator = elevator;
    }

    public synchronized Elevator callElevator(int passengerFloor) {
        while (true) {
            if (elevator.getCurrentFloor() == passengerFloor && elevator.getCurrentVolume() < elevator.getCapacity()) {
                return elevator;
            }
            waitForElevator();
        }
    }

    public Elevator getElevator() {
        return elevator;
    }

    /**
     * Stop all threads
     */
    public void stop() {
        elevator.stopElevator();
        for (Floor floor : floors) {
            for (Passenger passenger : floor.getPassengers()) {
                passenger.stopPassenger();
            }
        }
    }

    public synchronized void waitForElevator() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e);
        }
    }

    public List<Floor> getFloors() {
        return floors;
    }
}
