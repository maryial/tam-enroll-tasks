package com.epam.tam.tasks.oop.exceptions;

public class PassengerPlaneOverloadedException extends PlaneOverloadedException{

	public PassengerPlaneOverloadedException(String model, int capacity, int actualPassengers) {
	   	  super(model, ": " + capacity + " passengers allowed, tried to load " + actualPassengers + " passengers.");
	}
}
