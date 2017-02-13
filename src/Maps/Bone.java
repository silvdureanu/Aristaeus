package SkeletonMaps;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Bone {
	private Point2D firstPoint;
	private Point2D secondPoint;
	private List<Bone> firstConnections;
	private List<Bone> secondConnections;
	
	public Bone(double x1, double y1, double x2, double y2) {
		if((x1<x2) || (x1==x2 && y1<y2)) {
			firstPoint = new Point2D.Double(x1,y1);
			secondPoint = new Point2D.Double(x2,y2);
		}
		else  {
			firstPoint = new Point2D.Double(x2,y2);
			secondPoint = new Point2D.Double(x1,y1);	
		}
		
		firstConnections = new ArrayList<Bone>();
		secondConnections = new ArrayList<Bone>();

	}
	
	public Point2D getFirstPoint() {
		return firstPoint;
	}
	
	public Point2D getSecondPoint() {
		return secondPoint;
	}	
	
	public void addConnection(double x, double y, Bone s) {
		if(x==firstPoint.getX()&&y==firstPoint.getY())
			firstConnections.add(s);
		if(x==secondPoint.getX()&&y==secondPoint.getY())
			secondConnections.add(s);		
	}
	
}
