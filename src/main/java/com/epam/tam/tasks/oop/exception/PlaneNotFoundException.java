package com.epam.tam.tasks.oop.exception;

public class PlaneNotFoundException extends Exception{
	
	
	      //Parameterless Constructor
	      public PlaneNotFoundException(String model) {
	    	  super("Plane " + model + " is not found in the specification list");
	      }
}
