package com.epam.tam.tasks.oop.runner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Stream;

import com.epam.tam.tasks.oop.entities.CargoPlane;
import com.epam.tam.tasks.oop.entities.ICarrier;
import com.epam.tam.tasks.oop.entities.PassengerPlane;
import com.epam.tam.tasks.oop.entities.Plane;
import com.epam.tam.tasks.oop.entities.PlaneFactory;
import com.epam.tam.tasks.oop.entities.PlaneSpecificationField;
import com.epam.tam.tasks.oop.entities.PlaneType;
import com.epam.tam.tasks.oop.exceptions.FieldInPlaneSpecificationNotFoundException;
import com.epam.tam.tasks.oop.exceptions.PlaneNotFoundException;
import com.epam.tam.tasks.oop.util.IProcessor;
import com.epam.tam.tasks.oop.util.Utils;


public class PlaneShop {

	private IProcessor dataProcessor;	
	private Map<String, String> availableModels;
	private PlaneFactory planeFactory;
	private List<Plane> garage;
	private Scanner in;
	
	public PlaneShop(IProcessor dataProcessor) {
		this.dataProcessor = dataProcessor;
		availableModels = new HashMap<String, String>();
		availableModels.putAll(getAvailablePlaneModels());
		planeFactory = new PlaneFactory(dataProcessor);
		garage = new ArrayList<Plane>();
		in = new Scanner(System.in);
	}

	public List<Plane> runShop() {
		int planesForSaleAction = 2;
		System.out.println();
		System.out.println("Input the number of your next action:");
		System.out.println("1 - View the list of available planes");
		System.out.println("2 - Buy a plane");
		System.out.println("3 - HOT SALE: Buy " + planesForSaleAction + " random planes for the price of one plane - get lucky!");
		System.out.println("4 - Sort and filter your bought planes");
		System.out.println("5 - Add your own plane model");
		System.out.println("0 - Exit the shop");
		String choice = in.nextLine();
		switch (choice) {
			case("1"):
				availableModels.values().forEach(x -> System.out.println(x));
				break;
			case("2"):
				buyOnePlane();
				break;
			case("3"):
				buySalePlanes(planesForSaleAction);
				break;
			case ("4"):
				showSortingOptions();
				break;
			case ("5"):
				addCustomPlaneModel();
				break;
			case("0"):
				exitShop();
				break;
			default:
				System.out.println("You entered not allowerd option, try again");
		}
		if(!choice.equals("0")) {
			runShop();
		}
		in.close();
		return garage;
	}	

	
	private Map<String, String> getAvailablePlaneModels() {
		return dataProcessor.readAllPlanes();
	}
	
	
	private void buyOnePlane() {
		buyPlanes();
		System.out.println("The planes which you bought so far: ");
		garage.forEach(System.out::println);
		System.out.println();
	}
	
	private void buySalePlanes(int planesForSaleAction) {
		buyPlanes(planesForSaleAction);
		System.out.println("------------------------------------------------------------");
		System.out.println("The random planes which you bought for the price of one plane: ");
		for(int i = garage.size() - planesForSaleAction; i < garage.size(); i++) {
			System.out.println(garage.get(i));
		}
	}
	
	private void exitShop() {
		System.out.println("Thank you for visiting our shop! You are the best customer!");
		System.out.println("The planes which you bought so far: ");
		garage.forEach(System.out::println);
		System.out.println();
	}
	
	private Plane buyPlane(String model) {
		Plane plane = null;
		try {
			plane = planeFactory.createPlane(model);
			System.out.println("Successfully added plane " + model + " into your garage");			
		} catch (PlaneNotFoundException e) {
			System.out.println("Looks like the plane factory does not have the plane requested");
		} catch (FieldInPlaneSpecificationNotFoundException e) {
			System.out.println("Looks like the plane factory specifications are outdated, the requested plane cannot be built");
		}
		return plane;
	}
	
	private void buyPlanes(int randomPlanesNumber) {
		Random rand = new Random();
		String randomModel;
		List<String> models = new ArrayList<String>(availableModels.keySet());
		for(int i = 0; i < randomPlanesNumber; i++) {
			randomModel = models.get(rand.nextInt(models.size()));
			garage.add(buyPlane(randomModel));
		}
	}

	private void buyPlanes() {
		boolean exit = true;
		boolean successful = true;
		System.out.println("Specify the model to buy:");
		String model = in.nextLine();		
		if(availableModels.containsKey(model)) {
			garage.add(buyPlane(model));
		}
		else {
			System.out.println("The specified model is not found.");
			successful = false;
			buyPlanes();
		}
		if(successful) { 
			do {
				exit = true;
				System.out.println("Do you want to buy anything else? (y/n)");
				String proceed = in.nextLine();
				if(proceed.equalsIgnoreCase("y")) {
					buyPlanes();
				}
				else if(!proceed.equalsIgnoreCase("n")) {
					System.out.println("Allowed answers are: y, n");
					exit = false;
				}
			} while (!exit);
		}
	}
	
	private void showSortingOptions() {
		boolean exitSorting = false;
		boolean correctInput = true;
		String choice = "";
		String planeType = "";
		Class plane;
		Map<String, Class> planeTypes = new HashMap();
		planeTypes.put("a", Plane.class);
		planeTypes.put("b", PassengerPlane.class);
		planeTypes.put("c", CargoPlane.class);
		String[] choices = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "0"};
		
		do {
			correctInput = true;
			System.out.println("Select available sorting and filter options: ");
			System.out.println("1 - Sort planes by distance max to min");
			System.out.println("2 - Sort planes by distance min to max");
			System.out.println("3 - Sort planes by capacity max to min");
			System.out.println("4 - Sort planes by capacity min to max");
			System.out.println("5 - Filter planes by distance more than");
			System.out.println("6 - Filter planes by distance less than");
			System.out.println("7 - Filter planes by capacity more than");
			System.out.println("8 - Filter planes by capacity less than");
			System.out.println("0 - Exit sorting and filtering");
			choice = in.nextLine();
			if(!Arrays.asList(choices).contains(choice)) {
				correctInput = false;
				System.out.println("Allowed options are 0-8");
			}
			else if (choice.equals("0")) {
				exitSorting = true;
			}
			else {
				do{
					correctInput = true;
					System.out.println("Apply sorting and filter options on: all planes - A, passenger planes - B, cargo planes - C");
					planeType = in.nextLine().toLowerCase();
					if(!planeTypes.containsKey(planeType)) {
						correctInput = false;
						System.out.println("Allowed options are A, B, C");
					}
				} while (!correctInput);
			}			
		} while (!correctInput);
		
		if(!exitSorting) {
			plane = planeTypes.get(planeType);			
			switch (choice) {
				case ("1"):
					System.out.println("Planes sorted by distance from max to min: ");
					sortPlanesByDistanceDesc(plane).forEach(System.out::println);
					System.out.println();
					break;
				case ("2"):
					System.out.println("Planes sorted by distance from min to max: ");
					sortPlanesByDistanceAsc(plane).forEach(System.out::println);
					System.out.println();
					break;
				case ("3"): 
					System.out.println("Planes sorted by capacity from max to min: ");
					sortPlanesByCapacityDesc(plane).forEach(System.out::println);
					System.out.println();
					break;
				case ("4"):
					System.out.println("Planes sorted by capacity from min to max: ");
					sortPlanesByCapacityAsc(CargoPlane.class).forEach(System.out::println);
					System.out.println();
					break;
				case ("5"):
					System.out.println("Input the distance, planes with more than that distance will be shown:");
					int distance = Utils.parseNumber();	
					System.out.println("All planes with flight distance more than "+ distance + ":");
					filterPlanesWithFlightDistanceMoreThan(plane, distance).forEach(System.out::println);
					System.out.println();
					break;
				case ("6"):
					System.out.println("Input the distance, planes with less than that distance will be shown:");
					int distanceLess = Utils.parseNumber();	
					System.out.println("All planes with flight distance less than "+ distanceLess + ":");
					filterPlanesWithFlightDistanceLessThan(plane, distanceLess).forEach(System.out::println);
					System.out.println();
					break;
				case ("7"):
					System.out.println("Input the capacity, planes with more than that capacity will be shown:");
					int capacityMore = Utils.parseNumber();	
					System.out.println("All planes with flight capacity more than "+ capacityMore + ":");
					filterPlanesWithCapacityMoreThan(plane, capacityMore).forEach(System.out::println);
					System.out.println();	
					break;
				case ("8"):
					System.out.println("Input the capacity, planes with less than that capacity will be shown:");
					int capacityLess = Utils.parseNumber();	
					System.out.println("All planes with flight capacity less than "+ capacityLess + ":");
					filterPlanesWithCapacityLessThan(plane, capacityLess).forEach(System.out::println);
					System.out.println();
					break;
			}
			showSortingOptions();
		}
	}
	

	
	
	private <T extends Plane> Stream<Plane> sortPlanesByDistanceDesc(Class<T> className) {
		//System.out.println("Planes of type " + className.getName() + " sorted by flight distance from max to min: ");		
		return garage.stream()
				.filter(x -> className.isInstance(x))
				.sorted((p1, p2) -> Integer.compare(p2.getFlightDistance(), p1.getFlightDistance()));			
	}
	
	private <T extends Plane> Stream<Plane> sortPlanesByDistanceAsc(Class<T> className) {
		//System.out.println("Planes of type " + className.getName() + " sorted by flight distance from min to max: ");		
		return garage.stream()
				.filter(x -> className.isInstance(x))
				.sorted((p1, p2) -> Integer.compare(p1.getFlightDistance(), p2.getFlightDistance()));			
	}
	
	private <T extends Plane> Stream<Plane> sortPlanesByCapacityDesc(Class<T> className) {
		//System.out.println("Planes of type " + className.getName() + " sorted by capacity distance from max to min: ");		
		return garage.stream()
				.filter(x -> className.isInstance(x) && x instanceof ICarrier)
				.sorted((p1, p2) -> Integer.compare(((ICarrier) p2).getCapacity(), ((ICarrier) p1).getCapacity()));			
	}
	
	private <T extends Plane> Stream<Plane> sortPlanesByCapacityAsc(Class<T> className) {
		//System.out.println("Planes of type " + className.getName() + " sorted by capacity distance from min to max: ");		
		return garage.stream()
				.filter(x -> className.isInstance(x) && x instanceof ICarrier)
				.sorted((p1, p2) -> Integer.compare(((ICarrier) p1).getCapacity(), ((ICarrier) p2).getCapacity()));			
	}
	
	private <T extends Plane> Stream<Plane> filterPlanesWithCapacityMoreThan(Class<T> className, int min) {		
		//System.out.println("Planes of type " + className.getName() + " with more than " + min + " capacity: ");		
		return garage.stream()
				.filter(x -> x instanceof ICarrier && className.isInstance(x))
				.filter((x) ->  ((ICarrier) x).getCapacity() > min);				
	}
	
	private <T extends Plane> Stream<Plane> filterPlanesWithCapacityLessThan(Class<T> className, int max) {		
		//System.out.println("Planes of type " + className.getName() + " with less than " + max + " capacity: ");		
		return garage.stream()
				.filter(x -> x instanceof ICarrier && className.isInstance(x))
				.filter((x) ->  ((ICarrier) x).getCapacity() < max);				
	}
	
	private <T extends Plane> Stream<Plane> filterPlanesWithFlightDistanceMoreThan(Class<T> className, int min) {		
		//System.out.println("Planes of type " + className.getName() + " with more than " + min + " flight distance: ");		
		return garage.stream()
				.filter(x -> className.isInstance(x))
				.filter((x) ->  x.getFlightDistance() > min);				
	}
	
	private <T extends Plane> Stream<Plane> filterPlanesWithFlightDistanceLessThan(Class<T> className, int max) {		
		//System.out.println("Planes of type " + className.getName() + " with less than " + max + " flight distance: ");		
		return garage.stream()
				.filter(x -> className.isInstance(x))
				.filter((x) ->  x.getFlightDistance() < max);				
	}	
	
	private void addCustomPlaneModel() {
		boolean correctInput = true;
		Map<String, String> customPlane = new HashMap<String, String>();
		List<String> allowedPlanes = new ArrayList();
		allowedPlanes.add("a");
		allowedPlanes.add("b");
		allowedPlanes.add("c");
		String planeType = "";
		do{
			correctInput = true;
			System.out.println("Select the type of plane you will add: passenger plane with steward crew - A, passenger planes - B, cargo planes - C");
			planeType = in.nextLine().toLowerCase().trim();
			if(!allowedPlanes.contains(planeType)) {
				correctInput = false;
				System.out.println("Allowed options are A, B, C");
			}
		} while (!correctInput);
		System.out.println("Input plane model: ");	
		customPlane.put(PlaneSpecificationField.model.name(), in.nextLine());
		System.out.println("Input plane capacity: ");
		int capacity = Utils.parseNumber();
		customPlane.put(PlaneSpecificationField.capacity.name(), capacity + "");
		System.out.println("Input plane flight distance ");
		int distance = Utils.parseNumber();
		customPlane.put(PlaneSpecificationField.flightDistance.name(), distance + "");
		if(planeType.equals("a")) {
			customPlane.put(PlaneSpecificationField.type.name(), PlaneType.PassengerPlaneWithStewardCrew.name());
			System.out.println("Input plane crew number ");
			int crew = Utils.parseNumber();
			customPlane.put(PlaneSpecificationField.crew.name(), crew + "");
		}
		else if(planeType.equals("b")) {
			customPlane.put(PlaneSpecificationField.type.name(), PlaneType.PassengerPlane.name());
		}
		else {
			customPlane.put(PlaneSpecificationField.type.name(), PlaneType.CargoPlane.name());
		}
		dataProcessor.write(customPlane);
		availableModels.putAll(getAvailablePlaneModels());
	}
}

