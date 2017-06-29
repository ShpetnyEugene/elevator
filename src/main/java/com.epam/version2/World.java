package com.epam.version2;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class World extends JFrame {

    public World() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1400, 700);
        setTitle("Elevator");

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0xBECFFB));

        add(topPanel);
        add(topPanel, BorderLayout.PAGE_START);

        JButton start = new JButton("Start");
        JButton stop = new JButton("Pause");

        JScrollPane log = new JScrollPane();
        add(log);

        JScrollBar scrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
        scrollBar.setPreferredSize(new Dimension(100, 15));
        topPanel.add(start);
        topPanel.add(stop);




        JLabel jLabel = new JLabel("Speed");
        topPanel.add(jLabel);
        topPanel.add(scrollBar);

        JPanel left = new JPanel();
        add(left, BorderLayout.LINE_END);

        JPanel bottom = new JPanel();
        add(bottom, BorderLayout.PAGE_END);


        JTextPane numberPassengers = new JTextPane();
        JTextPane numberFloors = new JTextPane();
        JTextPane elevatorCapacity = new JTextPane();

//
//        JTextArea jTextArea = new JTextArea();
//        jTextArea.setLineWrap(true);
//        jTextArea.setColumns(2);
//
//        left.add(jTextArea);
//
//        left.add(numFlo);
//        left.add(jTextArea);


        JTextArea textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(20,20));
        textArea.setLineWrap(true);
        left.add(textArea);

        JLabel numFlo = new JLabel("Number floors");

        JTextArea jTextArea = new JTextArea();
        jTextArea.setLineWrap(true);

        JLabel numPasse = new JLabel("Number passe");
        JTable jTable = new JTable(1,1);


        left.add(numFlo,BorderLayout.LINE_START);
        left.add(jTable);
        left.add(numPasse,BorderLayout.CENTER);


        JScrollPane scroll = new JScrollPane();
        scroll.setBorder(new TitledBorder(new EtchedBorder(), "Display Area"));


        JTextArea display = new JTextArea(8, 60);
        display.setEditable(false); // set textArea non-editable
        scroll = new JScrollPane(display);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        bottom.add(scroll);


        Passenger passenger = new Passenger();




    }


    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        setBackground(Color.WHITE);
        Passenger passenger = new Passenger();
        passenger.draw(g);
    }
}
