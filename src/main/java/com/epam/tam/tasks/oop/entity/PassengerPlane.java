package com.epam.tam.tasks.oop.entity;

import com.epam.tam.tasks.oop.exception.PassengerPlaneOverloadedException;

public class PassengerPlane extends Plane implements ICarrier{

	protected int passengerCapacity;
	protected int actualPassengers = 0;
	
	public PassengerPlane(String model, int flightDistance, int passengers) {
		super(model, flightDistance);
		this.passengerCapacity = passengers;
	}	

	
	@Override
	public String fly() {
		return super.fly() + " with " + actualPassengers + " passengers on board";
	}

	@Override
	public int getCapacity() {
		return passengerCapacity;
	}	
	
	@Override
	public String toString() {
		return super.toString() + " and can carry on board " + passengerCapacity + " passengers";
	}

	@Override
	public void load(int actual) throws PassengerPlaneOverloadedException {
		if(actual > passengerCapacity) {
			throw new PassengerPlaneOverloadedException(super.model, passengerCapacity, actual);
		}
		actualPassengers = actual;
	}

	@Override
	public void unload() {
		actualPassengers = 0;		
	}
}
