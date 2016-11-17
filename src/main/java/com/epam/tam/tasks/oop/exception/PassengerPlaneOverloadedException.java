package com.epam.tam.tasks.oop.exception;

public class PassengerPlaneOverloadedException extends PlaneOverloadedException{

	public PassengerPlaneOverloadedException(String model, int capacity, int actualPassengers) {
	   	  super(model, ": " + capacity + " passengers allowed, tried to load " + actualPassengers + " passengers.");
	}
}
