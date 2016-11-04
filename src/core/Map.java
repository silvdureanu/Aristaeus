package core;

import java.util.List;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.PrecisionModel;


public class Map {
	
	private static MultiLineString map;
	private static GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel());
	private static Point realLocation;
	
	public Map(List<Integer> coords) {
		//geometryFactory.createLineString(coordinates);
		
		//TODO: generate map layout here
	}
	
	public static boolean crossesWall(Particle p, double newX, double newY) {
		Coordinate[] coords = new Coordinate[2];
		coords[0] = new Coordinate(p.getX(),p.getY());
		coords[1] = new Coordinate(newX, newY);
		LineString particlePath = geometryFactory.createLineString(coords);
		return particlePath.intersects(map);			
	}
}
