package com.epam.tam.tasks.oop.entity;

public abstract class Plane {

	protected String model;
	protected int flightDistance;
	
	public Plane(String model, int flightDistance) {
		this.model = model;
		this.flightDistance = flightDistance;
	}
	
	public int getFlightDistance() {
		return flightDistance;
	}

	public String fly() {
		return "Plane " + model + " departs";
	}
	
	public String land() {
		return "Plane " + model + " lands";
	}
	
	public String getModel() {
		return model;
	}
	
	@Override
	public String toString() {
		return "Plane " + model + " can fly " + flightDistance + " km";
	}
		
}
