package com.epam.tam.tasks.oop.entity;

import com.epam.tam.tasks.oop.exception.PlaneOverloadedException;

public interface ICarrier {
	public int getCapacity();
	public void load(int actual) throws PlaneOverloadedException;
	public void unload();
}
