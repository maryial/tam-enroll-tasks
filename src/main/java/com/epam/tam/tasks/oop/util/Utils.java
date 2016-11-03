package com.epam.tam.tasks.oop.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import org.json.*;

public class Utils {
	
	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	public static String readResourceFile(String path) {
		try(InputStream res = Utils.class.getResourceAsStream(path)) {		
			BufferedReader reader = new BufferedReader(new InputStreamReader(res));
			String line = null;
			StringBuilder planes = new StringBuilder();
			while ((line = reader.readLine()) != null) {
			    planes.append(line);
			}
			return planes.toString();		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return null;
	}
	
	public static int parseNumber() {		
		String text= "";
		boolean correct;
		int converted = 0;
		do {
			try {
				text = in.readLine();
			} catch (IOException e1) {
				System.out.println("Something unexpected happend, we'll try again");
				e1.printStackTrace();
				parseNumber();
			}
			correct = true;
			try {
				converted = Integer.parseInt(text);
			}
			catch (NumberFormatException e){
				System.out.println("The input must be of integer type, try again.");
				correct = false;
			}
		} while(!correct);
		return converted;
	}

}
