package com.epam.tam.tasks.oop.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.epam.tam.tasks.oop.exceptions.FieldInPlaneSpecificationNotFoundException;
import com.epam.tam.tasks.oop.exceptions.PlaneNotFoundException;
import com.epam.tam.tasks.oop.entities.*;
public class JsonProcessor implements IProcessor {
	
	private final static String planesJson = "/planes.json";
	private final static String planesArrayName = "planes";
	private URL resource = JsonProcessor.class.getResource(planesJson);
	private JSONParser parser;
	//private JSONObject planeSpecification;
	private JSONArray planes;
	private Object obj;
	private JSONObject jsonObject;
	
	public JsonProcessor() {
		parser = new JSONParser();
		readData();
		//planeSpecification = new JSONObject(Utils.readResourceFile(planesJson));
		//planes = planeSpecification.getJSONArray("planes");
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
		
		//JSONObject obj = new JSONObject(Utils.readResourceFile(planesJson));
		//JSONArray planes = obj.getJSONArray(planesArrayName);		
		
		String model;
		StringBuilder plane = new StringBuilder();
		for (Object o : planes) {
			plane.setLength(0);
			JSONObject planeObj = (JSONObject) o;
			model = (String) planeObj.get(PlaneSpecificationField.model.name());
			plane.append((String)planeObj.get(PlaneSpecificationField.type.name()));
			plane.append(": model ");
		    plane.append(model);
		    plane.append(" - flight distance ");
		    plane.append((String)planeObj.get(PlaneSpecificationField.flightDistance.name()));
		    if(planeObj.containsKey(PlaneSpecificationField.capacity.name())) {
		    	plane.append(", with capacity ");
			    plane.append(planeObj.get(PlaneSpecificationField.capacity.name()));
		    }
		    if(planeObj.containsKey(PlaneSpecificationField.crew.name())) {
		    	plane.append(", with steward crew of ");
			    plane.append(planeObj.get(PlaneSpecificationField.crew.name()));
		    }
		    availableModels.put(model, plane.toString());	
		}
		return availableModels;
	}

	public String getPlainSpecificationField(PlaneSpecificationField field, String model) throws FieldInPlaneSpecificationNotFoundException, PlaneNotFoundException {
		for (Object o : planes) {
			JSONObject planeObj = (JSONObject) o;
			if(model.equals((String) planeObj.get(PlaneSpecificationField.model.name()))) {
				return (String) planeObj.get(field.name());
			}			
		}
		if(field.equals(PlaneSpecificationField.model.name())) {
			throw new PlaneNotFoundException(model);
		}
		else {
			throw new FieldInPlaneSpecificationNotFoundException(field.name(), model);
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
