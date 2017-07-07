package com.epam.version2.beans;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shpetny Eugene
 * @version 1.0
 * @since 07/2017
 */
public class Floor {
    private int x1;
    private int x2;
    private int x3;
    private int x4;
    private int y;
    private int number;
    private List<Passenger> passengers = new ArrayList<>();

    public Floor(int x1, int x2, int x3, int x4, int y, int number) {
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.x4 = x4;
        this.y = y;
        this.number = number;
    }

    /**
     * Displays the floors that are on their passengers
     *
     * @param g Object classes Graphics
     */
    public void draw(Graphics g) {
        g.drawLine(x1, y, x2, y);
        g.drawLine(x3, y, x4, y);
        g.drawString("Floor: " + number, 35, y - 20);

        for (Passenger passenger : passengers) {
            passenger.draw(g);
        }
    }

    /**
     * Adds a passenger to this floor, and also sets the desired coordinate (X)
     *
     * @param passenger Passenger to be placed on this floor
     */
    public void addPassengers(Passenger passenger) {
        passenger.setxAxis(!passengers.isEmpty() ? passengers.get(passengers.size() - 1).getxAxis() + 25 : 750);
        passengers.add(passenger);
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }
}
