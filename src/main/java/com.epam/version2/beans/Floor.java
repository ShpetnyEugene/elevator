package com.epam.version2.beans;


import com.epam.version2.Drawable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shpetny Eugene
 * @version 1.0
 */
public class Floor implements Drawable {

    private static final int DISTANCE_PASSENGER = 25;
    private static final int START = 750;
    private static final int X_COORDINATE_LABEL = 35;
    private static final int Y_COORDINATE_LABEL = 20;

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
        g.drawString("Floor: " + number, X_COORDINATE_LABEL, y - Y_COORDINATE_LABEL);

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
        passenger.setxAxis(!passengers.isEmpty() ? passengers.get(passengers.size() - 1).getxAxis() + DISTANCE_PASSENGER : START);
        passengers.add(passenger);
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }
}
