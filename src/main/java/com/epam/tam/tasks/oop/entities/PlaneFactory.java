package com.epam.tam.tasks.oop.entities;

import com.epam.tam.tasks.oop.exceptions.FieldInPlaneSpecificationNotFoundException;
import com.epam.tam.tasks.oop.exceptions.PlaneNotFoundException;
import com.epam.tam.tasks.oop.util.IProcessor;


public class PlaneFactory {
	
	private IProcessor dataProcessor;
	
	public PlaneFactory(IProcessor dataProcessor) {
		this.dataProcessor = dataProcessor;
	}
	
	public Plane createPlane(String model) throws PlaneNotFoundException, FieldInPlaneSpecificationNotFoundException {		
		try {
			PlaneType planeType = getPlaneType(model);
			int flightDistance = Integer.parseInt(getPlainSpecificationField(PlaneSpecificationField.flightDistance, model));
			int capacity;
			switch(planeType) {
				case PassengerPlaneWithStewardCrew:
					int crewSize = Integer.parseInt(getPlainSpecificationField(PlaneSpecificationField.crew, model));
					capacity = Integer.parseInt(getPlainSpecificationField(PlaneSpecificationField.capacity, model));
					PassengerPlaneWithStewardCrew bigPassengerPlane = new PassengerPlaneWithStewardCrew(model, flightDistance, capacity, crewSize);
					return bigPassengerPlane;					
				case PassengerPlane:
					capacity = Integer.parseInt(getPlainSpecificationField(PlaneSpecificationField.capacity, model));
					PassengerPlane passengerPlane = new PassengerPlane(model, flightDistance, capacity);
					return passengerPlane;
				case CargoPlane:
					capacity = Integer.parseInt(getPlainSpecificationField(PlaneSpecificationField.capacity, model));
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
		String type = dataProcessor.getPlainSpecificationField(PlaneSpecificationField.type, model);
		if (type != null) {
		      for (PlaneType plane : PlaneType.values()) {
		        if (type.equalsIgnoreCase(plane.name())) {
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
