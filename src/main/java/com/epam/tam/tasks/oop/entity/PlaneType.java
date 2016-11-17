package com.epam.tam.tasks.oop.entity;

public enum PlaneType {
		PASSENGER_PLANE, 
		PASSENGER_PLANE_WITH_STEWARD_CREW,
		CARGO_PLANE;
		
		public String toString() {
		    switch(this) {
		      case PASSENGER_PLANE: return "PassengerPlane";
		      case PASSENGER_PLANE_WITH_STEWARD_CREW: return "PassengerPlaneWithStewardCrew";
		      case CARGO_PLANE: return "CargoPlane";
		      default: throw new IllegalArgumentException();
		    }
		}
}
