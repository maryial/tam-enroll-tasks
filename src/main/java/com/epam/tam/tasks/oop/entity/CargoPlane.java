package com.epam.tam.tasks.oop.entity;

import com.epam.tam.tasks.oop.exception.CargoPlaneOverloadedException;


public class CargoPlane extends Plane implements ICarrier{

	private int cargoWeightCapacity;
	private int actualCargoWeight = 0;
	
	public CargoPlane(String model, int flightDistance, int capacity) {
		super(model, flightDistance);
		cargoWeightCapacity = capacity;
	}
	
	@Override
	public int getCapacity() {
		return cargoWeightCapacity;
	}

	@Override
	public String fly() {
		return super.fly() + " with " + actualCargoWeight + " kg cargo on board";
	}
	
	@Override
	public void load(int actual) throws CargoPlaneOverloadedException {
		if(actual > cargoWeightCapacity) {
			throw new CargoPlaneOverloadedException(model, cargoWeightCapacity, actual);
		}
		actualCargoWeight = actual;
	}

	@Override
	public void unload() {
		actualCargoWeight = 0;		
	}
	
	public void unload(int part) {
		if(part > actualCargoWeight) {
			actualCargoWeight = 0;
		}
		actualCargoWeight =- part;		
	}
	
	@Override
	public String toString() {
		return super.toString() + " and carry " + cargoWeightCapacity + " kg on board";
	}
}
