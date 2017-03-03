package Maps;

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

import Particles.Particle;
import Particles.SkeletonParticle;
import core.Main;


public class UltimateSkeleton implements SkeletonMap {
	
	double screenFactor = 13;
	private  MultiLineString map;
	private  GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel());
	private  ShapeWriter shapeWriter = new ShapeWriter();
	private  Point realLocation;
	private GridIntersector gridIntersector;

	

	static double[][] skeleton = new double[][] {
		{7.23,54.23,29.38,54.23},
		{29.38,54.23,38.23,54.23},
		{38.23,54.23,74.38,54.23},
		{29.38,51.38,29.38,54.23},
		{29.38,54.23,29.38,62.53},
		{38.23,4.61,38.23,15.23},
		{38.23,15.23,38.23,54.23},
		{74.38,54.23,80.38,54.23},
		{74.38,4.61,74.38,54.23},
		{74.38,54.23,74.38,69.38},
		{7.69,15.23,38.23,15.23},
		{38.23,15.23,73,15.23},
		{38.23,54.23,38.23,57.07}			
	};

	
	static Bone[] bones = new Bone[skeleton.length];
	
	//static double[][] segments = basicDonut;
	static double[][] segments = WGBParser.getSegs(0);
	
	
	public void setUpMap() {
		LineString[] points = new LineString[segments.length];
		for(int i=0; i<segments.length; i++) {
			Coordinate[] visCoords = new Coordinate[2];
			visCoords[0] = new Coordinate(screenFactor*segments[i][0],screenFactor*segments[i][1]);
			visCoords[1] = new Coordinate(screenFactor*segments[i][2],screenFactor*segments[i][3]);			

			points[i] = geometryFactory.createLineString(visCoords);
		}		
		map = geometryFactory.createMultiLineString(points);
		
		for(int i=0; i<skeleton.length; i++) 
			bones[i] = new Bone(skeleton[i][0],skeleton[i][1],skeleton[i][2],skeleton[i][3]);
		
		
		bones[0].addConnection(29.38,54.23, bones[1]);
		bones[0].addConnection(29.38,54.23, bones[3]);
		bones[0].addConnection(29.38,54.23, bones[4]);
		
		
		bones[1].addConnection(29.38, 54.23, bones[0]);
		bones[1].addConnection(29.38, 54.23, bones[3]);
		bones[1].addConnection(29.38, 54.23, bones[4]);
		
		bones[1].addConnection(38.23,54.23,bones[2]);
		bones[1].addConnection(38.23,54.23,bones[6]);
		bones[1].addConnection(38.23,54.23,bones[12]);
		
		bones[2].addConnection(38.23,54.23,bones[1]);
		bones[2].addConnection(38.23,54.23,bones[6]);
		bones[2].addConnection(38.23,54.23,bones[12]);
		
		bones[2].addConnection(74.38, 54.23, bones[7]);
		bones[2].addConnection(74.38, 54.23, bones[8]);		
		bones[2].addConnection(74.38, 54.23, bones[9]);
		
		
		bones[3].addConnection(29.38, 54.23, bones[0]);
		bones[3].addConnection(29.38, 54.23, bones[1]);		
		bones[3].addConnection(29.38, 54.23, bones[4]);		
		
		
		bones[4].addConnection(29.38, 54.23, bones[0]);
		bones[4].addConnection(29.38, 54.23, bones[1]);		
		bones[4].addConnection(29.38, 54.23, bones[3]);
		
		bones[5].addConnection(38.23, 15.23, bones[6]);
		bones[5].addConnection(38.23, 15.23, bones[10]);
		bones[5].addConnection(38.23, 15.23, bones[11]);		
		
		bones[6].addConnection(38.23, 15.23, bones[5]);
		bones[6].addConnection(38.23, 15.23, bones[10]);
		bones[6].addConnection(38.23, 15.23, bones[11]);
		
		bones[6].addConnection(38.23,54.23,bones[1]);
		bones[6].addConnection(38.23,54.23,bones[2]);
		bones[6].addConnection(38.23,54.23,bones[12]);	
		
		bones[7].addConnection(74.38, 54.23, bones[2]);
		bones[7].addConnection(74.38, 54.23, bones[8]);		
		bones[7].addConnection(74.38, 54.23, bones[9]);
		
		bones[8].addConnection(74.38, 54.23, bones[2]);
		bones[8].addConnection(74.38, 54.23, bones[7]);		
		bones[8].addConnection(74.38, 54.23, bones[9]);
		
		bones[9].addConnection(74.38, 54.23, bones[2]);
		bones[9].addConnection(74.38, 54.23, bones[7]);		
		bones[9].addConnection(74.38, 54.23, bones[8]);
		
		bones[10].addConnection(38.23, 15.23, bones[6]);
		bones[10].addConnection(38.23, 15.23, bones[5]);
		bones[10].addConnection(38.23, 15.23, bones[11]);	
		
		
		bones[11].addConnection(38.23, 15.23, bones[6]);
		bones[11].addConnection(38.23, 15.23, bones[10]);
		bones[11].addConnection(38.23, 15.23, bones[5]);		
		
		
		bones[12].addConnection(38.23,54.23,bones[2]);
		bones[12].addConnection(38.23,54.23,bones[6]);
		bones[12].addConnection(38.23,54.23,bones[1]);		
		
		
		
		
	}
	
	public UltimateSkeleton(double initX, double initY) {
		realLocation = geometryFactory.createPoint(new Coordinate(screenFactor*initX,screenFactor*initY));	
		setUpMap();
	}
	
	public void updateRealLocation() {
		double[] i = Main.inputGenerator.generateRealInput();
		double x = realLocation.getX();
		double y = realLocation.getY();
		
		double newX = x + screenFactor*i[0] * Math.cos(Math.toRadians(i[1]));
		double newY = y- screenFactor*i[0] * Math.sin(Math.toRadians(i[1])); // Y coordinates start at the top, so need to invert
		realLocation = geometryFactory.createPoint(new Coordinate(newX,newY));
	}
	
	public Bone[] getSkeleton() {
		return bones;
	}

	public Shape getWalls() {
		return shapeWriter.toShape(map);
	}
	
	public Shape getRealLocation() {
		return shapeWriter.toShape(realLocation);
	}
	
	public Shape getParticles() {
		MultiPoint particles;		
		List<SkeletonParticle> particleList = Main.skeleFilter.getParticles();	
		Point[] pointList = new Point[particleList.size()];		
		int i=0;		
		for(SkeletonParticle p: particleList) {
			pointList[i] = geometryFactory.createPoint(new Coordinate(screenFactor*p.getX()-9,screenFactor*p.getY()-31));
			i++;
		}		
		particles = geometryFactory.createMultiPoint(pointList);
		return shapeWriter.toShape(particles);
	}
	
	
	
}
