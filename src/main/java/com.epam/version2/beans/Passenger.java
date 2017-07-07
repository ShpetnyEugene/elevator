package com.epam.version2.beans;

import com.epam.version2.Mode;

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


    public Passenger(String name, int xAxis, int yAxis, int src, int dest, Building building) {
        this.name = name;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.width = 10;
        this.height = 50;
        this.sourceFloor = src;
        this.stopFloor = dest;
        this.currentFloor = src;
        this.destinationPosX = -1;
        this.building = building;

        this.status = Mode.WAIT;
    }

    public void setxAxis(int xAxis) {
        this.xAxis = xAxis;
    }

    public int getxAxis() {
        return xAxis;
    }

    public int getyAxis() {
        return yAxis;
    }

    public void setyAxis(int yAxis) {
        this.yAxis = yAxis;
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

    }


    private void setCoordinate() {
        switch (status) {
            case UP:
                --yAxis;
                break;
            case DOWN:
                ++yAxis;
                break;
            case RIGHT:
                ++xAxis;
                break;
            case LEFT:
                --xAxis;
                break;
            default:
                break;
        }
    }


    @Override
    public void run() {
        while (true) {
            if (currentFloor == stopFloor && status == Mode.ON_ELEVATOR) {

                System.out.println(this + "  LEFT FLOOR :" + stopFloor);

                building.getElevator().getPassengers().remove(this);

                while (xAxis > -20) {
                    status = Mode.LEFT;
                    needSleep(100);
                    setCoordinate();
                }
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
//
            needSleep(100);


            setCoordinate();
        }
    }


    public void needSleep(int delay) {
        try {
            sleep(delay);
        } catch (InterruptedException e) {
            // TODO
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
