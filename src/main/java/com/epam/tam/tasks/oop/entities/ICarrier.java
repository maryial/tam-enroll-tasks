package com.epam.tam.tasks.oop.entities;

import com.epam.tam.tasks.oop.exceptions.PlaneOverloadedException;

public interface ICarrier {
	public int getCapacity();
	public void load(int actual) throws PlaneOverloadedException;
	public void unload();
}
