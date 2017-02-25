package Maps;

import java.awt.Shape;
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


public class FirstSkeleton implements SkeletonMap {
	
	private  MultiLineString map;
	private  GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel());
	private  ShapeWriter shapeWriter = new ShapeWriter();
	private  Point realLocation = geometryFactory.createPoint(new Coordinate(10,10));	
	static int[][] basicDonut = new int[][]{
		{5,5,5,995},{5,995,495,995},{495,995,495,5},{495,5,5,5},
		{100,100,100,900},{100,900,400,900},{400,900,400,100},{400,100,100,100}
	};
	
	static int[][] trapDonut= new int[][] {
		{5,5,5,995},{5,995,495,995},{495,995,495,5},{495,5,5,5},
		{100,100,100,830},{100,830,300,830},{300,830,300,870},{300,870,100,870},{100,870,100,900},{100,900,400,900},{400,900,400,100},{400,100,100,100}		
	};
	
	static int[][] skeleton = new int[][] {
		{50,50,950,50},{950,50,950,450},{950,450,50,450},{50,450,50,50}
	};
	
	static Bone[] bones = new Bone[skeleton.length];
	
	static int[][] segments = trapDonut;
	//static int[][] segments = WGBParser.getSegs(0);
	
	public void setUpMap() {
		LineString[] points = new LineString[segments.length];
		for(int i=0; i<segments.length; i++) {
			Coordinate[] coords = new Coordinate[2];
			coords[0] = new Coordinate(segments[i][1],segments[i][0]);
			coords[1] = new Coordinate(segments[i][3],segments[i][2]);
			//TODO fix this
			points[i] = geometryFactory.createLineString(coords);
		}		
		map = geometryFactory.createMultiLineString(points);
		
		for(int i=0; i<skeleton.length; i++)
			bones[i] = new Bone(skeleton[i][0],skeleton[i][1],skeleton[i][2],skeleton[i][3]);
			bones[0].addConnection(950, 50, bones[1]);
			bones[0].addConnection(50, 50, bones[3]);
			
			bones[1].addConnection(950, 450, bones[2]);
			bones[1].addConnection(950, 50, bones[0]);
			
			bones[2].addConnection(50, 450, bones[3]);
			bones[2].addConnection(950,450,bones[1]);
			
			bones[3].addConnection(50, 50, bones[0]);
			bones[3].addConnection(50, 450, bones[2]);
	}
	
	public FirstSkeleton() {
		setUpMap();
	}

	
	public Bone[] getSkeleton() {
		return bones;
	}

	public  Shape getWalls() {
		return shapeWriter.toShape(map);
	}
	
	public  Shape getRealLocation() {
		return shapeWriter.toShape(realLocation);
	}
	
	public  Shape getParticles() {
		MultiPoint particles;		
		List<SkeletonParticle> particleList = Main.skeleFilter.getParticles();	
		Point[] pointList = new Point[particleList.size()];		
		int i=0;		
		for(SkeletonParticle p: particleList) {
			pointList[i] = geometryFactory.createPoint(new Coordinate(p.getX(),p.getY()));
			i++;
		}		
		particles = geometryFactory.createMultiPoint(pointList);
		return shapeWriter.toShape(particles);
	}	
}



