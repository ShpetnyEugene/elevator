package com.epam.version1.beans;

/**
 * The status in which the passenger is now
 */
public enum Status {
    ON_ELEVATOR(1), WAITING(0);

    private int status;

    Status(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
