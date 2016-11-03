package com.epam.tam.tasks.oop.exceptions;

public class PlaneOverloadedException extends Exception{
	
	
	      //Parameterless Constructor
	      public PlaneOverloadedException(String model, String text) {
	    	  super("Plane " + model + " is overloaded" + text);
	      }
}
