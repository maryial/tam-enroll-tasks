package com.epam.tam.tasks.oop.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.epam.tam.tasks.oop.entities.CargoPlane;
import com.epam.tam.tasks.oop.entities.ICarrier;
import com.epam.tam.tasks.oop.entities.PassengerPlane;
import com.epam.tam.tasks.oop.entities.Plane;
import com.epam.tam.tasks.oop.exceptions.PlaneOverloadedException;
import com.epam.tam.tasks.oop.util.HSQLProcessor;
import com.epam.tam.tasks.oop.util.IProcessor;
import com.epam.tam.tasks.oop.util.JsonProcessor;

public class CustomerGarage {


	public static void main(String[] args) {
		IProcessor dataProcessor = new HSQLProcessor();
		PlaneShop superShop = new PlaneShop(dataProcessor);
		List<Plane> garage = new ArrayList<Plane>();
		System.out.println("Welcome to the super plane shop!");
		garage.addAll(superShop.runShop());
		System.out.println("Customer launches all planes: ");
		launchPlanes(garage);
		if(dataProcessor instanceof HSQLProcessor) {
			((HSQLProcessor) dataProcessor).stop();
		}
	}
	
	private static void launchPlanes(List<Plane> garage) {
		if(!garage.isEmpty()) {
			garage.stream()
			.parallel()
			.forEach(x -> {
				try 
					{ 
						if(x instanceof ICarrier) {
							((ICarrier) x).load(new Random().nextInt(((ICarrier)x).getCapacity()));
						}
						System.out.println(x.fly());
						Thread.sleep(new Random().nextInt(x.getFlightDistance()));
						System.out.println(x.land());
						if(x instanceof ICarrier) {
							((ICarrier) x).unload();
						}
					} 					
				catch (PlaneOverloadedException e) { 
					System.out.println("You tried to overload the plane, ");
					e.getStackTrace();
				}
				catch (InterruptedException e) {
					System.out.println("Plane " + x.getModel() + " dissapeared from the radar! Alarm!");
					e.getStackTrace();
				}
			});
		}
	}
}
