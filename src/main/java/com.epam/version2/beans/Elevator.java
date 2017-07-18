package com.epam.version2.beans;

import com.epam.version2.*;
import com.epam.version2.Window;
import com.epam.version2.modes.Move;
import com.epam.version2.modes.StatusPassenger;
import com.epam.version2.services.MessageService;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.Thread.sleep;

public class Elevator implements Runnable, Drawable {

    private static final Logger log = Logger.getLogger(Elevator.class);
    private final int DELAY;
    private static final int FLOOR_DELAY = 5000;
    private static final int DISTANCE = 22;
    private static final int COORDINATE_ELEVATOR = 615;
    private static final int COORDINATE_FLOOR = 100;
    private static final int GROUND_FLOOR = 1;

    private String name;
    private int capacity;
    private int currentFloor;
    private int currentVolume;
    private Building building;
    private boolean running = true;
    private int numberFloors;

    private MessageService messageService = new MessageService(Window.getLogger());
    private Move direction;
    private int xAxis;
    private int yAxis;
    private int width;
    private int height;
    private int doorWidth;
    private Move doorMode = Move.CLOSE;
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
        this.direction = Move.UP;
        DELAY = 100;

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

    /**
     * Get elevator for passenger
     *
     * @param newFloor  Floor where needed
     * @param current   Current floor
     * @param passenger Passenger getting elevator
     */
    public synchronized int takeElevator(int newFloor, int current, Passenger passenger) {

        if (current == currentFloor && currentVolume < capacity && running) {
            currentVolume++;
            log.info(passenger.getName() + " got " + toString() + " on floor " + current);
            messageService.writeMessage(passenger.getName() + " got " + toString() + " on floor " + current);
            passengers.add(passenger);
            while (passenger.getxAxis() != COORDINATE_ELEVATOR + DISTANCE * passengers.indexOf(passenger)) {
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
                    messageService.writeMessage("Not found needed floor");
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
            if (yAxis % COORDINATE_FLOOR == 0) {
                doorMode = Move.OPEN;
                while (doorWidth > 0) {
                    needSleep(DELAY);
                    door();
                }
            }
            if (yAxis % COORDINATE_FLOOR == 0) {
                needSleep(FLOOR_DELAY);
            }
            
            if (building.getFloors().get(currentFloor - 1).getPassengers().size() == passengers.size() || currentVolume == capacity) {
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
                if (yAxis % COORDINATE_FLOOR == 0) {
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
                if (yAxis % COORDINATE_FLOOR == 0) {
                    --currentFloor;
                    if (currentFloor == GROUND_FLOOR) {
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

    public void setyAxis(int yAxis) {
        this.yAxis = yAxis;
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

    public int getCurrentFloor() {
        return currentFloor;
    }

    @Override
    public String toString() {
        return name;
    }
}
