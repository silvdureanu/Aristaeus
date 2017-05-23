package Maps;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.awt.ShapeWriter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import Particles.SkeletonParticle;
import core.Main;


public class TestingSkeleton implements SkeletonMap {
	
	double screenFactor = 12;
	private  MultiLineString map;
	private  GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel());
	private  ShapeWriter shapeWriter = new ShapeWriter();
	private  Point realLocation;
	private GridIntersector gridIntersector;	
	
	
	static double[][] basicDonut = new double[][]{
		{5,5,5,95},{5,95,49,95},{49,95,49,5},{49,5,5,5},
		{10,10,10,90},{10,90,40,90},{40,90,40,10},{40,10,10,10}
	};
	
	static double[][] trapDonut= new double[][] {
		{5,5,5,995},{5,995,495,995},{495,995,495,5},{495,5,5,5},
		{100,100,100,830},{100,830,300,830},{300,830,300,870},{300,870,100,870},{100,870,100,900},{100,900,400,900},{400,900,400,100},{400,100,100,100}		
	};


	
	static double[][] skeleton = new double[][] {
		{15,15,15,50},
		{7,40,30,40},
		{15,15,15,5},
		{30,40,30.02,40}
	};

	
	static ArrayList<Bone> bones = new ArrayList<Bone>(skeleton.length);
	
	static double[][] segments = basicDonut;
	
	Point2D inter;
	Bone b1,b2;
	
	
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
			bones.add( new Bone(skeleton[i][0],skeleton[i][1],skeleton[i][2],skeleton[i][3]));		
		boolean changed = true;
		while(changed) { // if lines intersect, split
			changed = false;			
			for(int i=0; (i<bones.size())&&!changed; i++)
				for(int j=i+1; (j<bones.size())&&!changed; j++) 
					if (bones.get(i).getInterType(bones.get(j))==2) {
						inter = bones.get(i).getInter(bones.get(j));
						if(inter.equals(bones.get(i).getFirstPoint())||inter.equals(bones.get(i).getSecondPoint())) {
							b2=bones.get(j);
							bones.remove(b2);
							bones.add(new Bone(b2.getFirstPoint().getX(),b2.getFirstPoint().getY(),
									inter.getX(),inter.getY()));
							bones.add(new Bone(inter.getX(),inter.getY(),
									b2.getSecondPoint().getX(),b2.getSecondPoint().getY()));
							changed = true;
						}
						else if (inter.equals(bones.get(j).getFirstPoint())||inter.equals(bones.get(j).getSecondPoint())) {
							b2=bones.get(i);
							bones.remove(b2);
							bones.add(new Bone(b2.getFirstPoint().getX(),b2.getFirstPoint().getY(),
									inter.getX(),inter.getY()));
							bones.add(new Bone(inter.getX(),inter.getY(),
									b2.getSecondPoint().getX(),b2.getSecondPoint().getY()));		
							changed = true;
						}				//previous 2 cases are T-cases		
						else {		
							b1 = bones.get(i);
							b2= bones.get(j);
							bones.remove(b1);
							bones.remove(b2);
							bones.add(new Bone(b1.getFirstPoint().getX(),b1.getFirstPoint().getY(),
									inter.getX(),inter.getY()));
							bones.add(new Bone(inter.getX(),inter.getY(),
									b1.getSecondPoint().getX(),b1.getSecondPoint().getY()));	
							bones.add(new Bone(b2.getFirstPoint().getX(),b2.getFirstPoint().getY(),
									inter.getX(),inter.getY()));
							bones.add(new Bone(inter.getX(),inter.getY(),
									b2.getSecondPoint().getX(),b2.getSecondPoint().getY()));	
							changed = true;
						}						
					}
		}
		
			
		for(int i=0; i<bones.size(); i++) // join the lines intersecting at the tips
			for(int j=i+1; j<bones.size(); j++) 
				if (bones.get(i).getInterType(bones.get(j))==1) {
					inter = bones.get(i).getInter(bones.get(j));
					bones.get(i).addConnection(inter.getX(), inter.getY(), bones.get(j));
					bones.get(j).addConnection(inter.getX(), inter.getY(), bones.get(i));
					//add angle here, use BS (or even linear honestly) to select best one
			}
		
	}
	
	public TestingSkeleton(double initX, double initY) {
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
		return bones.toArray(new Bone[bones.size()]);
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
