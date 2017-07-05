package com.epam.version2;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Floor {
    private int x1;
    private int x2;
    private int x3;
    private int x4;
    private int y;
    private int numberFloor;


    private int lastPoint;

    private List<Passenger> passengers = new ArrayList<>();

    public Floor(int x1, int x2, int x3, int x4, int y, int numberFloor) {
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.x4 = x4;
        this.y = y;
        this.numberFloor = numberFloor;

    }


    public void draw(Graphics g) {

        g.drawLine(x1, y, x2, y);
        g.drawLine(x3, y, x4, y);

        g.drawString("Floor: " + numberFloor, 35, y - 20);

        for (Passenger passenger : passengers) {
            passenger.draw(g);
        }
    }

    public void addPassengers(Passenger passenger) {

        passengers.add(passenger);
    }
}
