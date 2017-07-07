package com.epam.version2.beans;

import com.epam.version2.Mode;

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

    private Mode direction;
    private int xAxis;
    private int yAxis;
    private int width;
    private int height;
    private int doorWidth;
    private Mode doorMode = Mode.CLOSE;

    private List<Passenger> passengers = new CopyOnWriteArrayList<>();

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
        this.direction = Mode.UP;
    }

    public int getCurrentVolume() {
        return currentVolume;
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

    public synchronized int takeElevator(int newFloor, int current, Passenger passenger) {
        if (current == currentFloor && currentVolume < capacity && running) {
            currentVolume++;
            System.out.println(passenger.getName() + " got " + toString() + " on floor " + current);
            passengers.add(passenger);

            // Enter in the elevator
            while (passenger.getxAxis() != 650) {
                passenger.setxAxis(passenger.getxAxis() - 1);
                needSleep(100);
            }

            System.out.println("NAME: " + passenger.getName());
            while (newFloor != currentFloor) {
                try {
                    wait();
                } catch (InterruptedException e) {
//                    log.error(e);

                }
            }
            currentVolume--;
            return newFloor;
        } else {
            return current;
        }
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public void run() {
        while (running) {

            while (doorWidth > 0) {
                doorMode = Mode.OPEN;
                door();
            }
            // UP or DOWN
            if (yAxis == 500 - 100 * numberFloors) {
                direction = Mode.DOWN;
            }
            if (yAxis > 400) {
                direction = Mode.UP;
            }


            needSleep(100);
            notifyPassengers();
            building.tellAt();
            step();
            door();
        }
    }

    /**
     * TODO
     */
    private synchronized void notifyPassengers() {
        notifyAll();
    }

    /**
     * TODO
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
                        direction = Mode.DOWN;
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
                        direction = Mode.UP;
                    }
                }
                break;
            default:
                break;
        }
    }


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
     * TODO
     */
    public void needSleep(int delay) {
        try {
            sleep(delay);
        } catch (InterruptedException e) {
            // TODO
            e.printStackTrace();
        }
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    @Override
    public String toString() {
        return name;
    }
}
