package com.epam.version2;

import java.awt.*;

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
    private Mode doorMode = Mode.WAIT;

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
        this.doorWidth = width/2;
        this.direction = Mode.UP;

    }

    public void draw(Graphics g) {
        g.drawRect(xAxis, yAxis, width, height);
        g.setColor(Color.WHITE);
        g.fillRect(xAxis, yAxis, doorWidth, height);
        g.fillRect(xAxis + width - doorWidth, yAxis, doorWidth, height);
        g.setColor(Color.BLACK);
        g.drawRect(xAxis, yAxis, doorWidth, height);
        g.drawRect(xAxis + width - doorWidth, yAxis, doorWidth, height);


    }

    public int getxAxis() {
        return xAxis;
    }

    public void setyAxis(int yAxis) {
        this.yAxis = yAxis;
    }



    public void stopElevator() {
        running = true;
    }


    public int getHeight() {
        return height;
    }

    public int getyAxis() {
        return yAxis;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getCurrentVolume() {
        return currentVolume;
    }

    @Override
    public String toString() {
        return name;
    }

    public synchronized int takeElevator(int newFloor, int current, Passenger passenger) {
        if (current == currentFloor && currentVolume < capacity && running) {
            currentVolume++;
//            log.info(passenger.getNamePassenger() + " got " + toString() + " on floor " + current);

            System.out.println(passenger.getName() + " got " + toString() + " on floor " + current);
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

    @Override
    public void run() {


        while (true) {

            step();


            if(yAxis < 90){
                direction = Mode.DOWN;
            }

            if(yAxis % 100 == 0){
//                notifyPassengers();
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private synchronized void notifyPassengers() {
        notifyAll();
    }


    public synchronized void step() {
        switch (direction) {
            case UP:
                --yAxis;
                if (yAxis % 100 == 0) {
                    currentFloor++;
                    if (currentFloor == numberFloors) {
                        direction = Mode.DOWN;
                    }
                }
                break;
            case DOWN:
                ++yAxis;
                if (yAxis % 100 == 0) {
                    --currentFloor;
                    if (currentFloor == 0) {
                        direction = Mode.UP;
                    }
                }
                break;
            case OPEN:
                if (doorWidth > 0){
                    --doorWidth;
                }
                break;
            case CLOSE:
                if (doorWidth < width / 2){
                    ++doorWidth;
                }
                break;

            default:
                break;
        }
    }
}
