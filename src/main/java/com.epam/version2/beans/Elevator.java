package com.epam.version2.beans;

import com.epam.version2.modes.Move;
import com.epam.version2.modes.StatusPassenger;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.Thread.sleep;

public class Elevator implements Runnable {

    private String name;
    private int capacity;
    private int currentFloor;
    private int currentVolume;
    private Building building;
    private boolean running = true;
    private int numberFloors;

    private Move direction;
    private int xAxis;
    private int yAxis;
    private int width;
    private int height;
    private int doorWidth;
    private Move doorMode = Move.CLOSE;
    private List<Passenger> passengers = new CopyOnWriteArrayList<>();

    private Logger log = Logger.getLogger(Elevator.class);
    private final int DELAY;
    private final int FLOOR_DELAY = 6000;

    public Elevator(String name, int capacity, int currentFloor, Building building, int numberFloors, int xAxis, int yAxis) {
        this.name = name;
        this.capacity = capacity;
        this.currentFloor = currentFloor;
        this.currentVolume = 0;
        this.building = building;
        this.numberFloors = numberFloors;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.width = 100;
        this.height = 100;
        this.doorWidth = width / 2;
        this.direction = Move.UP;
        DELAY = building.getDelay() + 100;
    }

    public void draw(Graphics g) {

        for (Passenger passenger : passengers) {
            passenger.draw(g);
        }

        g.drawRect(xAxis, yAxis, width, height);
        g.setColor(Color.WHITE);
        g.fillRect(xAxis, yAxis, doorWidth, height);
        g.fillRect(xAxis + width - doorWidth, yAxis, doorWidth, height);
        g.setColor(Color.BLACK);
        g.drawRect(xAxis, yAxis, doorWidth, height);
        g.drawRect(xAxis + width - doorWidth, yAxis, doorWidth, height);
    }


    public int getCurrentFloor() {
        return currentFloor;
    }

    /**
     * Get elevator for passenger
     *
     * @param newFloor  Floor where needed
     * @param current   Current floor
     * @param passenger Passenger getting elevator
     */
    public synchronized int takeElevator(int newFloor, int current, Passenger passenger) {
        int distance = 22;
        if (current == currentFloor && currentVolume < capacity && running) {
            currentVolume++;
            log.info(passenger.getName() + " got " + toString() + " on floor " + current);
            System.out.println(passenger.getName() + " got " + toString() + " on floor " + current);
            passengers.add(passenger);
            while (passenger.getxAxis() != 615 + distance * passengers.indexOf(passenger)) {
                if (!running) {
                    break;
                }
                passenger.setxAxis(passenger.getxAxis() - 1);
                needSleep(DELAY);
            }

            passenger.setStatus(StatusPassenger.ON_ELEVATOR);

            while (newFloor != currentFloor) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    log.error("Not found needed floor", e);

                }
            }
            currentVolume--;
            return newFloor;
        } else {
            return current;
        }
    }


    @Override
    public void run() {
        while (running) {
            if (yAxis % 100 == 0) {
                doorMode = Move.OPEN;
                while (doorWidth > 0) {
                    needSleep(DELAY);
                    door();
                }
            }
            if (yAxis % 100 == 0) {
                needSleep(FLOOR_DELAY);
            }


            Floor temp = building.getFloors().get(currentFloor - 1);

            int onElevator = 0;
            for (Passenger passenger : temp.getPassengers()) {
                if (passenger.getStatus() == StatusPassenger.ON_ELEVATOR) {
                    onElevator++;
                }
            }

            if (building.getFloors().get(currentFloor - 1).getPassengers().size() == onElevator || currentVolume == capacity) {
                doorMode = Move.CLOSE;
                while (doorWidth < width / 2) {
                    needSleep(DELAY);
                    door();
                }
            }
            needSleep(DELAY);
            notifyPassengers();
            building.tellAt();
            step();
        }

    }

    private synchronized void notifyPassengers() {
        notifyAll();
    }

    public int getCurrentVolume() {
        return currentVolume;
    }

    /**
     * Change coordinate elevator
     */
    public synchronized void step() {
        switch (direction) {
            case UP:
                --yAxis;
                for (Passenger passenger : passengers) {
                    passenger.setyAxis(passenger.getyAxis() - 1);
                }
                if (yAxis % 100 == 0) {
                    currentFloor++;
                    if (currentFloor == numberFloors) {
                        direction = Move.DOWN;
                    }
                }
                break;
            case DOWN:
                ++yAxis;
                for (Passenger passenger : passengers) {
                    passenger.setyAxis(passenger.getyAxis() + 1);
                }
                if (yAxis % 100 == 0) {
                    --currentFloor;
                    if (currentFloor == 1) {
                        direction = Move.UP;
                    }
                }
                break;
            default:
                break;
        }
    }


    /**
     * Change mode door (Open or closed)
     */
    public void door() {
        switch (doorMode) {
            case OPEN:
                if (doorWidth > 0) {
                    --doorWidth;
                }
                break;
            case CLOSE:
                if (doorWidth < width / 2) {
                    ++doorWidth;
                }
                break;
            default:
                break;
        }
    }

    /**
     * Stops the stream for a specified number of milliseconds
     *
     * @param delay The number of milliseconds to which you want to pause the flow
     */
    public void needSleep(int delay) {
        try {
            sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e);
        }
    }

    public int getCapacity() {
        return capacity;
    }

    /***
     * Stop elevator
     */

    public void stopElevator() {
        running = false;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    @Override
    public String toString() {
        return name;
    }
}
