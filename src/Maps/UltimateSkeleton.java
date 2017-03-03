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
	static double[][] basicDonut = new double[][]{
		{5,5,5,99},{5,99,49,99},{49,99,49,5},{49,5,5,5},
		{10,10,10,90},{10,90,40,90},{40,90,40,10},{40,10,10,10}
	};
	/*static double[][] basicDonut = new double[][]{
		{5,5,5,995},{5,995,495,995},{495,995,495,5},{495,5,5,5},
		{100,100,100,900},{100,900,400,900},{400,900,400,100},{400,100,100,100}
	};*/
	

	static double[][] skeleton = new double[][] {
		{7,7,95,7},{95,7,95,45},{95,45,7,45},{7,45,7,7}
	};
	
	/*static int[][] skeleton = new int[][] {
		{50,50,950,50},{950,50,950,450},{950,450,50,450},{50,450,50,50}
	};*/
	
	static Bone[] bones = new Bone[skeleton.length];
	
	static double[][] segments = basicDonut;
	//static double[][] segments = WGBParser.getSegs(0);
	
	
	public void setUpMap() {
		LineString[] points = new LineString[segments.length];
		for(int i=0; i<segments.length; i++) {
			Coordinate[] visCoords = new Coordinate[2];
			visCoords[0] = new Coordinate(screenFactor*segments[i][1],screenFactor*segments[i][0]);
			visCoords[1] = new Coordinate(screenFactor*segments[i][3],screenFactor*segments[i][2]);			

			points[i] = geometryFactory.createLineString(visCoords);
		}		
		map = geometryFactory.createMultiLineString(points);
		
		for(int i=0; i<skeleton.length; i++) 
			bones[i] = new Bone(skeleton[i][0],skeleton[i][1],skeleton[i][2],skeleton[i][3]);
		
		
		bones[0].addConnection(95, 7, bones[1]);
		bones[0].addConnection(7, 7, bones[3]);
		
			
		bones[1].addConnection(95, 45, bones[2]);
		bones[1].addConnection(95, 7, bones[0]);
			
		bones[2].addConnection(7, 45, bones[3]);
		bones[2].addConnection(95,45,bones[1]);
			
		bones[3].addConnection(7, 7, bones[0]);
		bones[3].addConnection(7, 45, bones[2]);
		
		/*bones[0].addConnection(950, 50, bones[1]);		
		bones[0].addConnection(50, 50, bones[3]);
			
		bones[1].addConnection(950, 450, bones[2]);
		bones[1].addConnection(950, 50, bones[0]);
			
		bones[2].addConnection(50, 450, bones[3]);
		bones[2].addConnection(950,450,bones[1]);
			
		bones[3].addConnection(50, 50, bones[0]);
		bones[3].addConnection(50, 450, bones[2]);*/
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
			pointList[i] = geometryFactory.createPoint(new Coordinate(screenFactor*p.getX(),screenFactor*p.getY()));
			i++;
		}		
		particles = geometryFactory.createMultiPoint(pointList);
		return shapeWriter.toShape(particles);
	}
	
	
	
}
