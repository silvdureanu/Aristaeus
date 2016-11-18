package core;

import java.awt.Shape;
import java.util.List;

import com.vividsolutions.jts.awt.ShapeWriter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;


public class Map {
	
	private  MultiLineString map;
	private  GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel());
	private  ShapeWriter shapeWriter = new ShapeWriter();
	private  Point realLocation = geometryFactory.createPoint(new Coordinate(10,10));	
	
	public Map(int[][] segments) {
		LineString[] points = new LineString[segments.length];
		for(int i=0; i<segments.length; i++) {
			Coordinate[] coords = new Coordinate[2];
			coords[0] = new Coordinate(segments[i][1],segments[i][0]);
			coords[1] = new Coordinate(segments[i][3],segments[i][2]);
			points[i] = geometryFactory.createLineString(coords);
		}		
		map = geometryFactory.createMultiLineString(points);		
	}
	
	public  boolean crossesWall(Particle p, double newX, double newY) {
		Coordinate[] coords = new Coordinate[2];
		coords[0] = new Coordinate(p.getX(),p.getY());
		coords[1] = new Coordinate(newX, newY);
		LineString particlePath = geometryFactory.createLineString(coords);
		return particlePath.intersects(map);			
	}
	
	public  boolean outsideBounds(double newX, double newY) {
		Geometry envelope = map.getEnvelope();
		return !envelope.covers(geometryFactory.createPoint(new Coordinate(newX,newY)));
	}
	
	public  Shape getWalls() {
		return shapeWriter.toShape(map);
	}
	
	public  Shape getRealLocation() {
		return shapeWriter.toShape(realLocation);
	}
	
	public  Shape getParticles() {
		MultiPoint particles;		
		List<Particle> particleList = Particle.getParticles();	
		Point[] pointList = new Point[particleList.size()];		
		int i=0;		
		for(Particle p: particleList) {
			pointList[i] = geometryFactory.createPoint(new Coordinate(p.getX(),p.getY()));
			i++;
		}		
		particles = geometryFactory.createMultiPoint(pointList);
		return shapeWriter.toShape(particles);
	}
	
	
	
}
