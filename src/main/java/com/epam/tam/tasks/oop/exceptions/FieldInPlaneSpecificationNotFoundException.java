package com.epam.tam.tasks.oop.exceptions;

public class FieldInPlaneSpecificationNotFoundException extends Exception{

      public FieldInPlaneSpecificationNotFoundException(String field, String model) {
    	  super("Field " + field + " is not found in the specification list for model " + model);
      }
}
