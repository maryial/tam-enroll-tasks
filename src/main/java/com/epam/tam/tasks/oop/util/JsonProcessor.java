package com.epam.tam.tasks.oop.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.epam.tam.tasks.oop.exception.FieldInPlaneSpecificationNotFoundException;
import com.epam.tam.tasks.oop.exception.PlaneNotFoundException;
import com.epam.tam.tasks.oop.entity.*;
public class JsonProcessor implements IProcessor {
	
	private final static String planesJson = "/planes.json";
	private final static String planesArrayName = "planes";
	private URL resource = JsonProcessor.class.getResource(planesJson);
	private JSONParser parser;
	private JSONArray planes;
	private Object obj;
	private JSONObject jsonObject;
	
	public JsonProcessor() {
		parser = new JSONParser();
		readData();
	}
	public JsonProcessor(String filename) {
		this();
		resource = JsonProcessor.class.getResource(filename);
	}
	
	private void readData() {
		try {						
			obj = parser.parse(new FileReader(Paths.get(resource.toURI()).toFile()));
		} catch (FileNotFoundException e) {
			System.out.println("Specification file is not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("File was not properly read");
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("File is ill formatted");
			e.printStackTrace();
		} catch (URISyntaxException e) {
			System.out.println("Specification file is not found");
			e.printStackTrace();
		}
		jsonObject = (JSONObject) obj;
		planes = (JSONArray) jsonObject.get(planesArrayName);
	}
	
	@Override
	public Map<String, String> readAllPlanes() {
		Map<String, String> availableModels = new HashMap<String, String>();
		String model;
		StringBuilder plane = new StringBuilder();
		for (Object o : planes) {
			plane.setLength(0);
			JSONObject planeObj = (JSONObject) o;
			model = (String) planeObj.get(PlaneSpecificationField.MODEL.toString());
			plane.append((String)planeObj.get(PlaneSpecificationField.TYPE.toString()));
			plane.append(": model ");
		    plane.append(model);
		    plane.append(" - flight distance ");
		    plane.append((String)planeObj.get(PlaneSpecificationField.FLIGHT_DISTANCE.toString()));
		    if(planeObj.containsKey(PlaneSpecificationField.CAPACITY.toString())) {
		    	plane.append(", with capacity ");
			    plane.append(planeObj.get(PlaneSpecificationField.CAPACITY.toString()));
		    }
		    if(planeObj.containsKey(PlaneSpecificationField.CREW.toString())) {
		    	plane.append(", with steward crew of ");
			    plane.append(planeObj.get(PlaneSpecificationField.CREW.toString()));
		    }
		    availableModels.put(model, plane.toString());	
		}
		return availableModels;
	}

	public String getPlainSpecificationField(PlaneSpecificationField field, String model) throws FieldInPlaneSpecificationNotFoundException, PlaneNotFoundException {
		for (Object o : planes) {
			JSONObject planeObj = (JSONObject) o;
			if(model.equals((String) planeObj.get(PlaneSpecificationField.MODEL.toString()))) {
				return (String) planeObj.get(field.toString());
			}			
		}
		if(field.equals(PlaneSpecificationField.MODEL.toString())) {
			throw new PlaneNotFoundException(model);
		}
		else {
			throw new FieldInPlaneSpecificationNotFoundException(field.toString(), model);
		}
	}

	@Override
	public void write(Map<String, String> data) {
		JSONObject customPlane = new JSONObject();
		customPlane.putAll(data);
		planes.add(customPlane);
		jsonObject.put(planesArrayName, planes);
          try {

        	  FileWriter file = new FileWriter(Paths.get(resource.toURI()).toFile());                
        	  jsonObject.writeJSONString(file);
        	  file.flush();
        	  file.close();

		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
