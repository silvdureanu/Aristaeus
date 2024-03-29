package Maps;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;

import com.vividsolutions.jts.awt.ShapeWriter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import Particles.SkeletonParticle;
import core.ClusterParticleWrapper;
import core.Main;


public class UltimateSkeleton implements SkeletonMap {
	
	double screenFactor = 40;
	private  MultiLineString map;
	private  GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel());
	private  ShapeWriter shapeWriter = new ShapeWriter();
	private  Point realLocation;
	private GridIntersector gridIntersector;	
	double realx, realy, realh;

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
		{38.23,54.23,38.23,57.07},
		{103.0/13,190.0/13,103.0/13,735.0/13},
		{155.0/13,677.0/13,155.0/13,897.0/13}
	};*/
	static double[][] donutskeleton = new double[][] {
		{50,70,50,970},{50,970,450,970},{450,970,450,70},{450,70,50,70}
	};
	
	static double[][] turnskeleton = new double[][] {
		{6,5.5,6,9.5},{6,9.5,11.5,9.5},{11.5,9.5,11.5,6},{11.5,6,14.5,6},{14.5,6,14.5,9.5}
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
	
	static double[][] skeleton = turnskeleton;
	
	/*static double[][] skeleton = new double[][] {
		{15,15,15,50},
		{7,40,30,40},
		{15,15,15,5},
		{30,40,30.02,40}
	};*/
	
	static double[][] basicDonut = new double[][]{
		{5,5,5,995},{5,995,495,995},{495,995,495,5},{495,5,5,5},
		{100,100,100,900},{100,900,400,900},{400,900,400,100},{400,100,100,100}
	};

	static ArrayList<Bone> bones = new ArrayList<Bone>(skeleton.length);
	
	static double[][] segments = bigMap;
	//static double[][] segments = WGBParser.getSegs(0);
	
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
	
	public UltimateSkeleton(double initX, double initY, double initH) {
		realx=initX;
		realy=initY;
		realh=initH;
		realLocation = geometryFactory.createPoint(new Coordinate(screenFactor*initX-0,screenFactor*initY-0));	
		setUpMap();
	}
	
	public void updateRealLocation() {
		double[] i = Main.inputGenerator.generateRealInput();
		double mx = realLocation.getX()+0;
		double my = realLocation.getY()+0;
		realh = ((int)realh+i[1])%360;
		
		realx = realx + i[0] * Math.cos(Math.toRadians(realh));
		realy = realy- i[0] * Math.sin(Math.toRadians(realh));
		
		double newX = mx + screenFactor*i[0] * Math.cos(Math.toRadians(realh));
		double newY = my- screenFactor*i[0] * Math.sin(Math.toRadians(realh)); // Y coordinates start at the top, so need to invert
		realLocation = geometryFactory.createPoint(new Coordinate(newX-0,newY-0));
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
	
	
	public double getAccuracy(Cluster<ClusterParticleWrapper> c) {
		double totx,toty;
		totx=toty=0;
		List<ClusterParticleWrapper> l = c.getPoints();
		for(ClusterParticleWrapper r: l) {
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
	
	
	
	
	public Shape getParticles() {
		MultiPoint particles;		
		List<SkeletonParticle> particleList = Main.skeleFilter.getParticles();	
		Point[] pointList = new Point[particleList.size()];		
		int i=0;		
		for(SkeletonParticle p: particleList) {
			pointList[i] = geometryFactory.createPoint(new Coordinate(screenFactor*p.getX()-0,screenFactor*p.getY()-0));// 9 31
			i++;
		}		
		particles = geometryFactory.createMultiPoint(pointList);
		return shapeWriter.toShape(particles);
	}
}
