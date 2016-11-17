package com.epam.tam.tasks.oop.entity;

import java.util.List;

public class PassengerPlaneWithStewardCrew extends PassengerPlane{

	private StewardCrew crew;
	
	public PassengerPlaneWithStewardCrew(String model, int flightDistance, int passengers, int crewSize) {
		super(model, flightDistance, passengers);
		this.crew = new StewardCrew(crewSize);
	}

		
	@Override
	public String toString() {
		return super.toString() + " with " + crew;
	}
	
}
