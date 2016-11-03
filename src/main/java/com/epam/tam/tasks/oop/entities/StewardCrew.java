package com.epam.tam.tasks.oop.entities;


public class StewardCrew {
	private int stewardCrewSize;
	
	public StewardCrew(int crewSize) {
		stewardCrewSize = crewSize;
	}
	
	@Override
	public String toString() {
		return "steward crew of " + stewardCrewSize + " stewards";
	}
}
