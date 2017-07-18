package com.epam.version2.services;


import javax.swing.*;

/**
 * @author Shpetny Eugene
 * @version 1.0
 */
public class MessageService {

    private JTextArea log;

    public MessageService(JTextArea log) {
        this.log = log;
    }

    /**
     * Writes the specified message to JTextArea
     *
     * @param message - Message to be placed on the panel
     */
    public void writeMessage(String message) {
        log.append(message + "\n");
    }
}
