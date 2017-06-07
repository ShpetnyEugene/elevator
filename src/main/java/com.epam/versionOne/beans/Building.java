package com.epam.versionOne.beans;


public class Building {
    private int numberFloors;
    private int numberElevators;
    private Elevator[] elevators;

    private long delay;

    public Building(int numberFloors, int numberElevators, int elevatorCapacity) {
        this.numberFloors = numberFloors;
        this.numberElevators = numberElevators;
        elevators = new Elevator[numberElevators];
        for (int i = 0; i < numberElevators; i++) {
            elevators[i] = new Elevator("Elevator " + i,elevatorCapacity,0,this,numberFloors);
        }
    }

    public void startElevators(){
        for(Elevator elevator: elevators){
            elevator.start();
        }
    }

    public void stopElevators(){
        for (Elevator elevator: elevators){
            elevator.stopElevator();
        }
    }

    public synchronized void tellAt() {
        notifyAll();
    }

    public synchronized Elevator callElevator(int passengerFloor){
        while (true){
            for (Elevator elevator: elevators){
                if (elevator.getCurrentFloor() == passengerFloor && elevator.getCurrentVolume() < elevator.getCapacity()){
                    return elevator;
                }
            }
            waitForElevator();
        }
    }

    public synchronized void waitForElevator(){
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
