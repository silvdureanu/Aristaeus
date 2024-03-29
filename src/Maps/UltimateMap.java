package Maps;

import java.awt.Shape;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;

import com.vividsolutions.jts.awt.ShapeWriter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import Particles.Particle;
import core.ClusterSkeletonParticleWrapper;
import core.Main;


public class UltimateMap implements Map {
	
	double screenFactor = 50;
	private  MultiLineString map;
	private MultiLineString visMap;
	private  GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel());
	private  ShapeWriter shapeWriter = new ShapeWriter();
	private  Point realLocation;
	private GridIntersector gridIntersector;
	double realx,realy,realh;
	static double[][] basicDonut = new double[][]{
		{5,5,5,995},{5,995,495,995},{495,995,495,5},{495,5,5,5},
		{100,100,100,900},{100,900,400,900},{400,900,400,100},{400,100,100,100}
	};
	
	static double[][] trapDonut= new double[][] {
		{5,5,5,995},{5,995,495,995},{495,995,495,5},{495,5,5,5},
		{100,100,100,830},{100,830,300,830},{300,830,300,870},{300,870,100,870},{100,870,100,900},{100,900,400,900},{400,900,400,100},{400,100,100,100}		
	};
	
	static double[][] bigMap= new double[][] {
		{5,5,7,5},
		{7,5,7,9},
		{7,9,11,9},
		{11,9,11,5},
		{11,5,15,5},
		{15,5,15,10},
		{15,10,14,10},
		{14,10,14,7},
		{14,7,12,7},
		{12,7,12,10},
		{12,10,7,10},
		{7,10,5,10},
		{5,10,5,5}
	};
	
	//static double[][] segments = basicDonut;
	//static double[][] segments = WGBParser.getSegs(0);
	static double[][] segments=bigMap;
	
	
	public void setUpMap() {
		LineString[] points = new LineString[segments.length];
		LineString[] interPoints = new LineString[segments.length];
		for(int i=0; i<segments.length; i++) {
			Coordinate[] visCoords = new Coordinate[2];
			visCoords[0] = new Coordinate(screenFactor*segments[i][0],screenFactor*segments[i][1]);
			visCoords[1] = new Coordinate(screenFactor*segments[i][2],screenFactor*segments[i][3]);
			
			Coordinate[] interCoords = new Coordinate[2];
			interCoords[0] = new Coordinate(segments[i][0],segments[i][1]);
			interCoords[1] = new Coordinate(segments[i][2],segments[i][3]);			
			points[i] = geometryFactory.createLineString(visCoords);
			interPoints[i]=geometryFactory.createLineString(interCoords);
		}		
		map = geometryFactory.createMultiLineString(points);
		visMap = geometryFactory.createMultiLineString(interPoints);
		gridIntersector = new GridIntersector(10,10,20,20,segments);//10 10 20 20
	}
	
	public UltimateMap(double initX, double initY, double initH) {
		realx=initX;
		realy=initY;
		realh=initH;
		realLocation = geometryFactory.createPoint(new Coordinate(screenFactor*initX,screenFactor*initY));	
		setUpMap();
	}
	
	public void updateRealLocation() {
		double[] i = Main.inputGenerator.generateRealInput();
		double mx = realLocation.getX()+9;
		double my = realLocation.getY()+31;
		realh = ((int)realh+i[1])%360;
		
		realx = realx + i[0] * Math.cos(Math.toRadians(realh));
		realy = realy- i[0] * Math.sin(Math.toRadians(realh));
		
		double newX = mx + screenFactor*i[0] * Math.cos(Math.toRadians(realh));
		double newY = my- screenFactor*i[0] * Math.sin(Math.toRadians(realh)); // Y coordinates start at the top, so need to invert
		realLocation = geometryFactory.createPoint(new Coordinate(newX-9,newY-31));
	}
	
	public boolean crossesWall(Particle p, double newX, double newY) {
		return gridIntersector.crossesWall(p.getX(), p.getY(), newX, newY);	
	}
	
	public boolean outsideBounds(double newX, double newY) {

		
		Geometry envelope = visMap.getEnvelope();
		return !envelope.covers(geometryFactory.createPoint(new Coordinate(newX,newY)));

	}
	
	public Shape getWalls() {
		return shapeWriter.toShape(map);
	}
	
	public Shape getRealLocation() {
		return shapeWriter.toShape(realLocation);
	}
	
	//TODO Make filters push updates to map - since there's just one map. 
	public Shape getParticles() {
		MultiPoint particles;		
		List<Particle> particleList = Main.filter.getParticles();	
		Point[] pointList = new Point[particleList.size()];		
		int i=0;		
		for(Particle p: particleList) {
			pointList[i] = geometryFactory.createPoint(new Coordinate(screenFactor*p.getX(),screenFactor*p.getY()));
			i++;
		}		
		particles = geometryFactory.createMultiPoint(pointList);
		return shapeWriter.toShape(particles);
	}
	
	public double getAccuracy(Cluster<ClusterSkeletonParticleWrapper> c) {
		double totx,toty;
		totx=toty=0;
		List<ClusterSkeletonParticleWrapper> l = c.getPoints();
		for(ClusterSkeletonParticleWrapper r: l) {
			double[] pt = r.getPoint();
			//totacc+=(Math.sqrt(pt[0]*realx+pt[1]*realy));
			totx+=pt[0];
			toty+=pt[1];
			
		}
		totx/=l.size();
		toty/=l.size();
		
		return Math.sqrt((totx-realx)*(totx-realx)+(toty-realy)*(toty-realy));
	}
	public void reset(double a, double b, double c) {
		realx=a;
		realy=b;
		realh=c;
	}
	
}
