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


public class UltimateSkeleton implements SkeletonMap {
	
	double screenFactor = 20;
	private  MultiLineString map;
	private  GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel());
	private  ShapeWriter shapeWriter = new ShapeWriter();
	private  Point realLocation;
	private GridIntersector gridIntersector;	

	/*static double[][] skeleton = new double[][] {
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
	};*/
	
	static double[][] skeleton = new double[][] {
		{15,15,15,50},
		{7,40,30,40},
		{15,15,15,5},
		{30,40,30.02,40}
	};

	
	static ArrayList<Bone> bones = new ArrayList<Bone>(skeleton.length);
	
	//static double[][] segments = basicDonut;
	static double[][] segments = WGBParser.getSegs(0);
	
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
			}
		
		System.out.println(bones.size());
		
		
		/*bones.get(0).addConnection(29.38,54.23, bones.get(1));
		bones.get(0).addConnection(29.38,54.23, bones.get(3));
		bones.get(0).addConnection(29.38,54.23, bones.get(4));
		
		
		bones.get(1).addConnection(29.38, 54.23, bones.get(0));
		bones.get(1).addConnection(29.38, 54.23, bones.get(3));
		bones.get(1).addConnection(29.38, 54.23, bones.get(4));
		
		bones.get(1).addConnection(38.23,54.23,bones.get(2));
		bones.get(1).addConnection(38.23,54.23,bones.get(6));
		bones.get(1).addConnection(38.23,54.23,bones.get(12));
		
		bones.get(2).addConnection(38.23,54.23,bones.get(1));
		bones.get(2).addConnection(38.23,54.23,bones.get(6));
		bones.get(2).addConnection(38.23,54.23,bones.get(12));
		
		bones.get(2).addConnection(74.38, 54.23, bones.get(7));
		bones.get(2).addConnection(74.38, 54.23, bones.get(8));		
		bones.get(2).addConnection(74.38, 54.23, bones.get(9));
		
		
		bones.get(3).addConnection(29.38, 54.23, bones.get(0));
		bones.get(3).addConnection(29.38, 54.23, bones.get(1));		
		bones.get(3).addConnection(29.38, 54.23, bones.get(4));		
		
		
		bones.get(4).addConnection(29.38, 54.23, bones.get(0));
		bones.get(4).addConnection(29.38, 54.23, bones.get(1));		
		bones.get(4).addConnection(29.38, 54.23, bones.get(3));
		
		bones.get(5).addConnection(38.23, 15.23, bones.get(6));
		bones.get(5).addConnection(38.23, 15.23, bones.get(10));
		bones.get(5).addConnection(38.23, 15.23, bones.get(11));		
		
		bones.get(6).addConnection(38.23, 15.23, bones.get(5));
		bones.get(6).addConnection(38.23, 15.23, bones.get(10));
		bones.get(6).addConnection(38.23, 15.23, bones.get(11));
		
		bones.get(6).addConnection(38.23,54.23,bones.get(1));
		bones.get(6).addConnection(38.23,54.23,bones.get(2));
		bones.get(6).addConnection(38.23,54.23,bones.get(12));	
		
		bones.get(7).addConnection(74.38, 54.23, bones.get(2));
		bones.get(7).addConnection(74.38, 54.23, bones.get(8));		
		bones.get(7).addConnection(74.38, 54.23, bones.get(9));
		
		bones.get(8).addConnection(74.38, 54.23, bones.get(2));
		bones.get(8).addConnection(74.38, 54.23, bones.get(7));		
		bones.get(8).addConnection(74.38, 54.23, bones.get(9));
		
		bones.get(9).addConnection(74.38, 54.23, bones.get(2));
		bones.get(9).addConnection(74.38, 54.23, bones.get(7));		
		bones.get(9).addConnection(74.38, 54.23, bones.get(8));
		
		bones.get(10).addConnection(38.23, 15.23, bones.get(6));
		bones.get(10).addConnection(38.23, 15.23, bones.get(5));
		bones.get(10).addConnection(38.23, 15.23, bones.get(11));	
		
		
		bones.get(11).addConnection(38.23, 15.23, bones.get(6));
		bones.get(11).addConnection(38.23, 15.23, bones.get(10));
		bones.get(11).addConnection(38.23, 15.23, bones.get(5));		
		
		
		bones.get(12).addConnection(38.23,54.23,bones.get(2));
		bones.get(12).addConnection(38.23,54.23,bones.get(6));
		bones.get(12).addConnection(38.23,54.23,bones.get(1));		*/
		
		
		
		
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
