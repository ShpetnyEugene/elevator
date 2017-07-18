package com.epam.version2;

import com.epam.version2.beans.Building;
import com.epam.version2.beans.Floor;
import com.epam.version2.beans.Passenger;
import com.epam.version2.services.MessageService;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class Window extends JFrame implements ActionListener {

    private final static Logger log = Logger.getLogger(Window.class);
    private static final int DELAY = 1000;
    private static final int WIDTH_WINDOW = 1400;
    private static final int HEIGHT_WINDOW = 700;
    private static final int WIDTH_SCROLL = 100;
    private static final int HEIGHT_SCROLL = 20;
    private static final int ROWS = 1;
    private static final int COLUMNS = 2;
    private static final int ROWS_TEXT = 8;
    private static final int COLUMN_TEXT = 60;
    private Building building;

    private static JTextArea logger = new JTextArea(ROWS_TEXT, COLUMN_TEXT);
    private Timer timer = new Timer(DELAY, this);
    private MessageService messages = new MessageService(logger);
    private Random random = new Random();

    private int inputNumberPassengers;
    private int inputNumberFloors;
    private int inputElevatorCapacity;


    public Window() {
        timer.start();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH_WINDOW, HEIGHT_WINDOW);
        setTitle("Elevator Simulation");
        // Show top panel in work window
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0xBECFFB));
        add(topPanel, BorderLayout.PAGE_START);
        //  Add number passengers
        JLabel numPassengers = new JLabel("Number passengers");
        JTextArea numPassenger = new JTextArea(ROWS, COLUMNS);
        numPassenger.setPreferredSize(new Dimension(ROWS, ROWS));
        topPanel.add(numPassengers);
        topPanel.add(numPassenger);
        // Add number floors
        JLabel numFloors = new JLabel("Number floors");
        JTextArea numFloor = new JTextArea(ROWS, COLUMNS);
        numFloor.setPreferredSize(new Dimension(ROWS, ROWS));
        topPanel.add(numFloors);
        topPanel.add(numFloor);

        /**
         * Add elevator capacity
         * */
        JLabel elevatorsCapacity = new JLabel("Elevator capacity");
        JTextArea elevatorCapacity = new JTextArea(ROWS, COLUMNS);
        elevatorCapacity.setPreferredSize(new Dimension(ROWS, ROWS));
        topPanel.add(elevatorsCapacity);
        topPanel.add(elevatorCapacity);
        /**
         * Add two buttons start and stop
         * */
        JButton start = new JButton("Start");

        JScrollPane pane = new JScrollPane();

        JScrollBar speed = pane.createHorizontalScrollBar();
        speed.setPreferredSize(new Dimension(WIDTH_SCROLL, HEIGHT_SCROLL));
        topPanel.add(speed);

        start.addActionListener((e) -> {
            messages.writeMessage("Pressed Start button");
            log.info("Pressed Start button");


            inputNumberPassengers = inputData(numPassenger);
            inputNumberFloors = inputData(numFloor);
            inputElevatorCapacity = inputData(elevatorCapacity);

            building = new Building(inputNumberFloors, inputElevatorCapacity, inputNumberPassengers);

            startElevator();

            initFloors();
            spawn();
            startPassengers();


        });


        JButton stop = new JButton("Stop");
        stop.addActionListener((e) -> {
            messages.writeMessage("Pressed Stop button");
            log.info("Pressed Stop button");
            building.stop();
        });

        topPanel.add(start);
        topPanel.add(stop);


        logger.setEditable(false);
        JScrollPane scroll = new JScrollPane(logger);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scroll, BorderLayout.PAGE_END);


    }

    public void initFloors() {
        for (int i = 0; i < inputNumberFloors; i++) {
            building.getFloors().add(new Floor(20, 600, 700, 1200, 100 * (5 - i), i + 1));
        }
    }

    /**
     * Start thread with elevator
     */
    public void startElevator() {
        new Thread(building.getElevator()).start();
    }


    /**
     * Start all passengers (threads)
     */
    public void startPassengers() {
        List<Thread> threads = new ArrayList<>();
        for (Floor floor : building.getFloors()) {
            for (Passenger passenger : floor.getPassengers()) {
                threads.add(new Thread(passenger));
            }
        }
        threads.forEach(Thread::start);
    }

    public static JTextArea getLogger() {
        return logger;
    }

    /**
     * Randomly assigns the passengers the start and end floor
     */
    public void spawn() {
        for (int i = 0; i < inputNumberPassengers; i++) {
            int scrFloor = random.nextInt(inputNumberFloors);
            int destFloor;
            do {
                destFloor = random.nextInt(inputNumberFloors);
            } while (destFloor == scrFloor);
            building.getFloors().get(scrFloor).addPassengers(new Passenger("Passenger " + i, 800, 100 * (5 - scrFloor) - 50, scrFloor + 1, destFloor + 1, building));
        }
    }

    public int inputData(JTextArea area) {
        int result = 0;
        try {
            result = Integer.parseInt(area.getText());
        } catch (Exception e) {
            messages.writeMessage(e.getMessage());
            log.error(e);
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);


        if (inputNumberFloors != 0) {
            for (Floor floor : building.getFloors()) {
                floor.draw(g);
            }
            building.getElevator().draw(g);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            repaint();
        }
    }
}
