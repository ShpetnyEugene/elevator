package com.epam.version2.beans;

import com.epam.version2.*;
import com.epam.version2.Window;
import com.epam.version2.modes.Move;
import com.epam.version2.modes.StatusPassenger;
import com.epam.version2.services.MessageService;
import org.apache.log4j.Logger;

import java.awt.*;

import static java.lang.Thread.sleep;

/**
 * @author Shpetny Eugene
 * @version 1.0
 */
public class Passenger implements Runnable, Drawable {

    private static final Logger log = Logger.getLogger(Passenger.class);
    private static final int AWAY_COORDINATE = -10;

    private MessageService messageService = new MessageService(Window.getLogger());
    private String name;
    private int currentFloor;
    private int stopFloor;
    private int sourceFloor;
    private Building building;
    private boolean run;
    private StatusPassenger status;

    private Move direction = Move.LEFT;
    private int xAxis;
    private int yAxis;
    private int width;
    private int height;
    private final int DELAY = 100;

    public Passenger(String name, int xAxis, int yAxis, int src, int dest, Building building) {
        this.name = name;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.width = 10;
        this.height = 50;
        this.sourceFloor = src;
        this.stopFloor = dest;
        this.currentFloor = src;
        this.building = building;
        run = true;
        this.status = StatusPassenger.WAIT;
    }

    public void draw(Graphics g) {
        g.fillRect(xAxis, yAxis, width, height);
        g.drawString("S: " + (sourceFloor), xAxis - 5, yAxis - 18);
        g.drawString("D: " + (stopFloor), xAxis - 5, yAxis - 5);
    }


    /**
     * Changes the position of the passenger
     */
    private void setCoordinate() {
        switch (direction) {
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
        }
    }


    @Override
    public void run() {
        while (run) {
            if (currentFloor == stopFloor && status == StatusPassenger.ON_ELEVATOR) {
                log.info(this + " left elevator on floor " + stopFloor);
                messageService.writeMessage(this + " left elevator on floor " + stopFloor);
                building.getElevator().getPassengers().remove(this);
                direction = Move.LEFT;
                while (xAxis > AWAY_COORDINATE) {
                    if (!run) {
                        break;
                    }
                    setCoordinate();
                    needSleep(DELAY);
                }
                break;
            } else {
                Elevator elevator = building.callElevator(currentFloor);
                currentFloor = elevator.takeElevator(stopFloor, currentFloor, this);
                if (currentFloor != stopFloor) {
                    status = StatusPassenger.WAIT;
                    building.waitForElevator();
                }
            }
            needSleep(DELAY);
            setCoordinate();
        }
    }

    /**
     * Stop all passengers
     */
    public void stopPassenger() {
        run = false;
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
            log.error(e);
            e.printStackTrace();
        }
    }

    public void setxAxis(int xAxis) {
        this.xAxis = xAxis;
    }

    public int getxAxis() {
        return xAxis;
    }

    public StatusPassenger getStatus() {
        return status;
    }

    public void setStatus(StatusPassenger status) {
        this.status = status;
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

    @Override
    public String toString() {
        return name;
    }
}
