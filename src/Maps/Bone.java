package Maps;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Bone {
	private Point2D firstPoint;
	private Point2D secondPoint;
	private double len;
	private List<Joint> firstConnections;
	private List<Joint> secondConnections;
	
	public Bone(double x1, double y1, double x2, double y2) {
		if((x1<x2) || (x1==x2 && y1<y2)) {
			firstPoint = new Point2D.Double(x1,y1);
			secondPoint = new Point2D.Double(x2,y2);
		}
		else  {
			firstPoint = new Point2D.Double(x2,y2);
			secondPoint = new Point2D.Double(x1,y1);	
		}
		
		firstConnections = new ArrayList<Joint>();
		secondConnections = new ArrayList<Joint>();
		
		len = firstPoint.distance(secondPoint);

	}
	
	
	public Point2D getFirstPoint() {
		return firstPoint;
	}
	
	public Point2D getSecondPoint() {
		return secondPoint;
	}	
	
	public double getLen () {
		return len;
	}
	
	public void addConnection(double x, double y, Bone s) {
		if(x==firstPoint.getX()&&y==firstPoint.getY()) {
			
			if(firstPoint.equals(s.getFirstPoint()))
				firstConnections.add(new Joint(s,1));
			
			else if (firstPoint.equals(s.getSecondPoint()))
				firstConnections.add(new Joint(s,2));
			else System.out.println("Error finding target joint from first point");
		}

		else if(x==secondPoint.getX()&&y==secondPoint.getY()) {
			
			if(secondPoint.equals(s.getFirstPoint()))
				secondConnections.add(new Joint(s,1));
			
			else if (secondPoint.equals(s.getSecondPoint()))
				secondConnections.add(new Joint(s,2));
			
			else System.out.println("Error finding target joint from second point");
		}
		
		else{
			System.out.println("Error finding source joint");
			System.out.println(this.getFirstPoint());
			System.out.println(this.getSecondPoint());
			System.out.println(x);
			System.out.println(y);
		}
		
	}
	
	//Basic version returns "random" (1st element)
	public Joint nextFirstBone(double heading) {
		return firstConnections.get(0);
	}
	
	public Joint nextSecondBone(double heading) {
		return secondConnections.get(0);
	}
	
}
