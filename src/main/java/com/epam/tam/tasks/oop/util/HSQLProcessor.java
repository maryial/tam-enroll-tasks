package com.epam.tam.tasks.oop.util;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.hsqldb.Server;

import com.epam.tam.tasks.oop.entity.PlaneSpecificationField;
import com.epam.tam.tasks.oop.exception.FieldInPlaneSpecificationNotFoundException;
import com.epam.tam.tasks.oop.exception.PlaneNotFoundException;

public class HSQLProcessor implements IProcessor{

	Server hsqlServer = null;
	Connection connection = null;
	ResultSet rs = null;
	private URL resource = HSQLProcessor.class.getResource("/db");
	
	public HSQLProcessor() {
		hsqlServer = null;
		hsqlServer = new Server();
		hsqlServer.setLogWriter(null);
		hsqlServer.setSilent(true);
		hsqlServer.setDatabaseName(0, "iva");
		try {
			hsqlServer.setDatabasePath(0, "file:" + Paths.get(resource.toURI()).toAbsolutePath() + "/ivadb");
		} catch (URISyntaxException e) {
			System.out.println("Check availability of db folder in resources");			
		}	
		hsqlServer.start();		
		testIfDbFilledWithInitialData();
	}
	
	
	private void firstLaunch() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/iva", "sa", ""); // can through sql exception
			connection.prepareStatement("drop table planes if exists;").execute();
			connection.prepareStatement("create table planes (model varchar(40) not null, type varchar(40) not null, flightDistance integer not null, capacity integer, crew integer);").execute();
			connection.prepareStatement("insert into planes (model, type, flightDistance, capacity, crew)"
					+ "values ('Boeing 737', 'PassengerPlaneWithStewardCrew', 5000, 150, 3 );").execute();
			connection.prepareStatement("insert into planes (model, type, flightDistance, capacity, crew)"
					+ "values ('Airbus 310', 'PassengerPlaneWithStewardCrew', 6000, 200, 4 );").execute();
			connection.prepareStatement("insert into planes (model, type, flightDistance, capacity, crew)"
					+ "values ('AN 24', 'PassengerPlaneWithStewardCrew', 1000, 52, 2 );").execute();
			connection.prepareStatement("insert into planes (model, type, flightDistance, capacity)"
					+ "values ('Gulfstream G450', 'PassengerPlane', 1000, 14 );").execute();
			connection.prepareStatement("insert into planes (model, type, flightDistance, capacity)"
					+ "values ('Mriya', 'CargoPlane', 15400, 190000 );").execute();
			connection.prepareStatement("insert into planes (model, type, flightDistance, capacity)"
					+ "values ('Boeing C-17 Globemaster III', 'CargoPlane', 4400, 50000 );").execute();
			
			rs = connection.prepareStatement("select *  from planes;").executeQuery();
		} catch (SQLException e2) {
			e2.printStackTrace();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		}
		try {
			rs.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void testIfDbFilledWithInitialData () {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/iva", "sa", ""); // can through sql exception
			rs = connection.prepareStatement("select *  from planes;").executeQuery();
			if(!rs.next()) {
				firstLaunch();
			}
			
		} catch (SQLException e2) {
			firstLaunch();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		}		
	}
	
	@Override
	public Map<String, String> readAllPlanes() {
		Map<String, String> planes = new HashMap<String, String>();
		StringBuilder plane = new StringBuilder();
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/iva", "sa", ""); // can through sql exception
			rs = connection.prepareStatement("select * from planes;").executeQuery();
			do{	
				rs.next();	
				plane.setLength(0);
				plane.append(String.format("model: %1s, type: %1s, distance: %1d, capacity: %1d", rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
				if(rs.getInt(5)>0) {
					plane.append(String.format(", crew: %1d", rs.getInt(5)));
				}
				planes.put(rs.getString(1), plane.toString());
			} while(!rs.isLast());
		} catch (SQLException e2) {
			e2.printStackTrace();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		}
		try {
			rs.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return planes;
	}

	@Override
	public String getPlainSpecificationField(PlaneSpecificationField field,
			String name) throws FieldInPlaneSpecificationNotFoundException,
			PlaneNotFoundException {
		String foundField = "";
		ResultSet rs = null;
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/iva", "sa", ""); 
			rs = connection.prepareStatement("select " + field.toString() + " from planes where model = '" + name + "';").executeQuery();
			if(!rs.next()) {
				throw new PlaneNotFoundException(name);
			}			
			foundField = rs.getObject(1) + "";		
		} catch (SQLException e2) {
			throw new FieldInPlaneSpecificationNotFoundException(field.toString(), name);
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		}		
		try {
			rs.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return foundField;
	}

	@Override
	public void write(Map<String, String> data) {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/iva", "sa", ""); // can through sql exception
			if(data.containsKey(PlaneSpecificationField.CREW)) {
				connection.prepareStatement("insert into planes (model, type, flightDistance, capacity, crew) "
						+ "values ('" + data.get(PlaneSpecificationField.MODEL.toString()) 
						+ "', '" + data.get(PlaneSpecificationField.TYPE.toString())
						+ "', " + Integer.parseInt(data.get(PlaneSpecificationField.FLIGHT_DISTANCE.toString()))
						+ ", " + Integer.parseInt(data.get(PlaneSpecificationField.CAPACITY.toString())) 
						+ ", " + Integer.parseInt(data.get(PlaneSpecificationField.CREW.toString())) 
						+ ");").execute();
			}
			else {
				connection.prepareStatement("insert into planes (model, type, flightDistance, capacity) "
						+ "values ('" + data.get(PlaneSpecificationField.MODEL.toString()) 
						+ "', '" + data.get(PlaneSpecificationField.TYPE.toString())
						+ "', " + Integer.parseInt(data.get(PlaneSpecificationField.FLIGHT_DISTANCE.toString()))
						+ ", " + Integer.parseInt(data.get(PlaneSpecificationField.CAPACITY.toString())) 
						+ ");").execute();
			}					
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		try {
			rs.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public void stop() {
		hsqlServer.stop();
		hsqlServer = null;
	}

}
