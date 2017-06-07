package com.epam.versionOne.beans;

import org.apache.log4j.Logger;

public class Elevator extends Thread {

    private static final Logger log = Logger.getLogger(Elevator.class);

    private static final int DELAY = 500;
    // How long elevator stays on a floor
    private static final int FLOOR_TIME = 1000;
    // How long it takes to get to next floor
    private static final int TRAVEL_TIME = 100;

    private String name;
    private int capacity;
    private int currentFloor;
    private int currentVolume;
    private Building building;
    private boolean running = true;

    private int numberFloors;


    public Elevator(String name, int capacity, int currentFloor,  Building building, int numberFloors) {
        this.name = name;
        this.capacity = capacity;
        this.currentFloor = currentFloor;
        this.currentVolume = 0;
        this.building = building;
        this.numberFloors = numberFloors;
    }

    public int getCurrentVolume() {
        return currentVolume;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void stopElevator(){
        running = false;
    }

    @Override
    public void run() {
        log.info(toString() + " starting");
        boolean goFirst = false;

        try {
            sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        running = true;

        while (running){
            log.info(toString() + " now on floor " + currentFloor);
            if (currentFloor == numberFloors-1){
                goFirst = true;
            }else if (currentFloor == 0) {
                goFirst = false;
            }

            notifyPassengers();
            building.tellAt();

            try {
                sleep(FLOOR_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (goFirst) {
                currentFloor--;
            } else {
                currentFloor++;
            }
        }
        try {
            sleep(TRAVEL_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized int takeElevator(int newFloor,int current,Passenger passenger){
        if (current == currentFloor && currentVolume < capacity && running){
            currentVolume++;
            log.info(passenger.getNamePassenger() + " got " + toString() + " on floor " + current);

            while (newFloor != currentFloor){
                try {
                    wait();
                } catch (InterruptedException e) {
                    log.error(e);
                    e.printStackTrace();
                }
            }
            currentVolume--;
            return newFloor;
        }else {
            return current;
        }
    }

    @Override
    public String toString() {
        return name;
    }

    private synchronized void notifyPassengers() {
        notifyAll();
    }
}
