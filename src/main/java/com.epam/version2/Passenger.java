package com.epam.version2;

import java.awt.*;

import static java.lang.Thread.sleep;

public class Passenger implements Runnable {

    private String name;
    private int xAxis;
    private int yAxis;
    private int width;
    private int height;
    private int currentFloor;
    private Mode direction = Mode.LEFT;
    private int stopFloor;
    private int sourceFloor;
    private Building building;
    private Mode status;
    private int destinationPosX;


    public Passenger(String name, int xAxis, int yAxis, int src, int dest) {
        this.name = name;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.width = 10;
        this.height = 50;
        this.sourceFloor = src;
        this.stopFloor = dest;
        this.currentFloor = src;
        this.destinationPosX = -1;

        this.status = Mode.WAIT;
    }

    public String getName() {
        return name;
    }

    public void draw(Graphics g) {
        g.fillRect(xAxis, yAxis, width, height);
        g.drawString("S: " + (sourceFloor), xAxis - 5, yAxis - 18);
        g.drawString("D: " + (stopFloor), xAxis - 5, yAxis - 5);

//        if (xAxis == destinationPosX) {
//            status = Mode.WAIT;
//        } else {
//            status = direction == Mode.LEFT ? Mode.LEFT : Mode.RIGHT;
//        }
        status = Mode.RIGHT;

        setCoordinate();
    }


    private void setCoordinate() {
        switch (status) {
            case UP:
                --yAxis;
                break;
            case DOWN:
                ++yAxis;
                break;
            case LEFT:
                ++xAxis;
                break;
            case RIGHT:
                --xAxis;
                break;
            default:
                break;
        }
    }


    @Override
    public void run() {
        try {
            sleep(500);
        } catch (InterruptedException e) {
//            log.error(e);
        }

        while (true) {
            if (currentFloor == stopFloor && status == Mode.ON_ELEVATOR) {
                System.out.println(name + " left on the floor " + stopFloor);
//                log.info(name + " left on the floor " + stopFloor);
                break;
            } else {
                Elevator elevator = building.callElevator(currentFloor);
                status = Mode.ON_ELEVATOR;
                currentFloor = elevator.takeElevator(stopFloor, currentFloor, this);
                if (currentFloor != stopFloor) {
                    status = Mode.WAIT;
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
