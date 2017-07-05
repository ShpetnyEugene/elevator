package com.epam.version2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Building extends JFrame implements ActionListener {


    private Random random;

    private int inputNumberPassengers;
    private int inputNumberFloors;
    private int inputCapacity;
    private Elevator elevator;
    private int lastFloor;

    private Timer timer = new Timer(1000, this);
    private int counter;
    private int timeElapsedInSecs;
    private boolean running = true;

    private List<Floor> floors = new CopyOnWriteArrayList<>();

    public int getInputNumberPassengers() {
        return inputNumberPassengers;
    }

    public int getInputNumberFloors() {
        return inputNumberFloors;
    }

    public int getInputCapacity() {
        return inputCapacity;
    }


    public Building() {

        timer.start();
        random = new Random();
        elevator = new Elevator("Elevator: ", getInputCapacity(), 1, this, getInputNumberFloors(), 600, 400);


        JTextArea logger = new JTextArea(8, 60);
        System.setOut(new PrintStream(new PrintStreamCapturer(logger, System.out)));


        // Show and setting work window
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1400, 700);
        setTitle("Elevator Simulation");


        // Show top panel in work window
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0xBECFFB));
        add(topPanel, BorderLayout.PAGE_START);

        //  Add number passengers
        JLabel numPassengers = new JLabel("Number passengers");
        JTextArea numPassenger = new JTextArea(1, 2);
        numPassenger.setPreferredSize(new Dimension(1, 1));
        topPanel.add(numPassengers);
        topPanel.add(numPassenger);


        // Add number floors
        JLabel numFloors = new JLabel("Number floors");
        JTextArea numFloor = new JTextArea(1, 2);
        numFloor.setPreferredSize(new Dimension(1, 1));
        topPanel.add(numFloors);
        topPanel.add(numFloor);

        // Add elevator capacity

        JLabel elevatorsCapacity = new JLabel("Elevator capacity");
        JTextArea elevatorCapacity = new JTextArea(1, 2);
        elevatorCapacity.setPreferredSize(new Dimension(1, 2));
        topPanel.add(elevatorsCapacity);
        topPanel.add(elevatorCapacity);


        // Add two buttons start and stop
        JButton start = new JButton("Start");

        // Add action for start button
        start.addActionListener((e) -> {


            // TODO Add check input
            inputNumberPassengers = Integer.parseInt(numPassenger.getText().trim());

            inputNumberFloors = Integer.valueOf(numFloor.getText().trim());

            inputCapacity = Integer.parseInt(elevatorCapacity.getText().trim());

            new Thread(elevator).start();

            for (int i = 0; i < getInputNumberFloors(); i++) {
                floors.add(new Floor(20, 600, 700, 1200, 100 * (5 - i), i + 1));
            }
            spawn();


        });


        JButton stop = new JButton("Pause");
        topPanel.add(start);
        topPanel.add(stop);


        //CENTER


        // PAGE END

        // Add textArea for writing logText

        logger.setEditable(false);
        JScrollPane scroll = new JScrollPane(logger);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scroll, BorderLayout.PAGE_END);

    }

    @Override
    public synchronized void paint(Graphics g) {
        super.paint(g);
        for (Floor floor : floors) {
            floor.draw(g);
        }

        if (inputNumberFloors != 0) {
            elevator.draw(g);
        }
    }


    public synchronized void tellAt() {
        notifyAll();
    }

    public synchronized Elevator callElevator(int passengerFloor) {
        while (true) {
//            for (Elevator elevator : elevators) {
//                if (elevator.getCurrentFloor() == passengerFloor && elevator.getCurrentVolume() < elevator.getCapacity()) {
//                    return elevator;
//                }
//            }
            waitForElevator();
        }
    }

    public synchronized void waitForElevator() {
        try {
            wait();
        } catch (InterruptedException e) {
//            log.error(e);
        }
    }


    public void spawn() {
        for (int i = 0; i < inputNumberPassengers; i++) {
            int scrFloor = random.nextInt(inputNumberFloors);
            int destFloor;


            do {
                destFloor = random.nextInt(inputNumberFloors);
            } while (destFloor == scrFloor);

            floors.get(scrFloor).addPassengers(new Passenger("Passenger" + i, 800 - i * 2, 100 * (5 - scrFloor) - 50, scrFloor + 1, destFloor + 1));
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            repaint();
        }
    }
}
