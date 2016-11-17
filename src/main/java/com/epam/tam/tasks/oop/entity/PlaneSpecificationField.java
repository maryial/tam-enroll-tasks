package com.epam.tam.tasks.oop.entity;

public enum PlaneSpecificationField {
	CAPACITY,
	CREW,
	FLIGHT_DISTANCE,
	MODEL,
	TYPE;
	
	
	public String toString() {
	    switch(this) {
	      case CAPACITY: return "capacity";
	      case CREW: return "crew";
	      case FLIGHT_DISTANCE: return "flightDistance";
	      case MODEL: return "model";
	      case TYPE: return "type";
	      default: throw new IllegalArgumentException();
	    }
	}
	
}
