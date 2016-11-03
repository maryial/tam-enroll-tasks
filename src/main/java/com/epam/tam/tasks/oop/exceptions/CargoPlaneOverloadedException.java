package com.epam.tam.tasks.oop.exceptions;

public class CargoPlaneOverloadedException extends PlaneOverloadedException{

	public CargoPlaneOverloadedException(String model, int capacity, int actualCargoWeight) {
	   	  super(model, ": " + capacity + " kg cargo allowed, tried to load " + actualCargoWeight + " kg.");
	}
}
