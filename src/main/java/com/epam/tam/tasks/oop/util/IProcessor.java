package com.epam.tam.tasks.oop.util;

import java.util.Map;

import com.epam.tam.tasks.oop.entity.PlaneSpecificationField;
import com.epam.tam.tasks.oop.exception.FieldInPlaneSpecificationNotFoundException;
import com.epam.tam.tasks.oop.exception.PlaneNotFoundException;

public interface IProcessor {
	public Map<String, String> readAllPlanes();
	public String getPlainSpecificationField(PlaneSpecificationField field, String name) throws FieldInPlaneSpecificationNotFoundException, PlaneNotFoundException;
	public void write(Map<String, String> data);	
}
