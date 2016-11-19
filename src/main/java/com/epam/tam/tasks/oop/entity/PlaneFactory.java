package com.epam.tam.tasks.oop.entity;

import com.epam.tam.tasks.oop.exception.FieldInPlaneSpecificationNotFoundException;
import com.epam.tam.tasks.oop.exception.PlaneNotFoundException;
import com.epam.tam.tasks.oop.util.IProcessor;


public class PlaneFactory {
	
	private IProcessor dataProcessor;
	
	public PlaneFactory(IProcessor dataProcessor) {
		this.dataProcessor = dataProcessor;
	}
	
	public Plane createPlane(String model) throws PlaneNotFoundException, FieldInPlaneSpecificationNotFoundException {		
		try {
			PlaneType planeType = getPlaneType(model);
			int flightDistance = Integer.parseInt(getPlainSpecificationField(PlaneSpecificationField.FLIGHT_DISTANCE, model));
			int capacity;
			switch(planeType) {
				case PASSENGER_PLANE_WITH_STEWARD_CREW:
					int crewSize = Integer.parseInt(getPlainSpecificationField(PlaneSpecificationField.CREW, model));
					capacity = Integer.parseInt(getPlainSpecificationField(PlaneSpecificationField.CAPACITY, model));
					PassengerPlaneWithStewardCrew bigPassengerPlane = new PassengerPlaneWithStewardCrew(model, flightDistance, capacity, crewSize);
					return bigPassengerPlane;					
				case PASSENGER_PLANE:
					capacity = Integer.parseInt(getPlainSpecificationField(PlaneSpecificationField.CAPACITY, model));
					PassengerPlane passengerPlane = new PassengerPlane(model, flightDistance, capacity);
					return passengerPlane;
				case CARGO_PLANE:
					capacity = Integer.parseInt(getPlainSpecificationField(PlaneSpecificationField.CAPACITY, model));
					CargoPlane cargoPlane = new CargoPlane(model, flightDistance, capacity);
					return cargoPlane;
			}			
		} catch (NumberFormatException e) {
			System.out.println("Plane specification is ill-formatted, check with data provider");
			e.printStackTrace();
		}		
		return null;
	}	
	
		
	private PlaneType getPlaneType(String model) throws FieldInPlaneSpecificationNotFoundException, PlaneNotFoundException {
		String type = dataProcessor.getPlainSpecificationField(PlaneSpecificationField.TYPE, model);
		if (type != null) {
		      for (PlaneType plane : PlaneType.values()) {
		        if (type.equalsIgnoreCase(plane.toString())) {
		          return plane;
		        }
		      }
			}
		throw new PlaneNotFoundException(model);		  
	}
	
	
	private String getPlainSpecificationField(PlaneSpecificationField field, String model) throws FieldInPlaneSpecificationNotFoundException, PlaneNotFoundException {
		return dataProcessor.getPlainSpecificationField(field, model);	
	}
		
		
}
