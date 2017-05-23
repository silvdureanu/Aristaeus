package Maps;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bone {
	private Point2D firstPoint;
	private Point2D secondPoint;
	private double len;
	private List<Joint> firstConnections;
	private List<Joint> secondConnections;
	private Random randomSeed = new Random();
	
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
	
	
	public Point2D getInter(Bone that) {
		//first, check for case 1 intersections		
		if(this.firstPoint.equals(that.firstPoint) ||this.firstPoint.equals(that.secondPoint))
			return firstPoint;
		
		if(this.secondPoint.equals(that.firstPoint)|| this.secondPoint.equals(that.secondPoint))
			return secondPoint;
		
		double x0 = this.firstPoint.getX();
		double y0 = this.firstPoint.getY();
		double x1 = this.secondPoint.getX();
		double y1 = this.secondPoint.getY();
		
		double x2 = that.firstPoint.getX();
		double y2 = that.firstPoint.getY();
		double x3 = that.secondPoint.getX();
		double y3 = that.secondPoint.getY();
		
		if(x0==x1&&x2==x3) {  //treat verticals separately, and before rest, to avoid div by zero
			double yInter=0;  //first case, both vertical, next two, just one
			if(y0==y2 || y0==y3)
				yInter=y0;
			if(y1==y2||y1==y3)
				yInter = y1;			
			return new Point2D.Double(x0,yInter);
		}
			
		if (x0==x1) {       
			double a1 = (y3-y2)/(x3-x2);	
			double b1 = y2-a1*x2;
			double interY = a1*x0 + b1;
			return new Point2D.Double(x0,interY);
		}
		
		if(x2==x3) {
			double a0 = (y1-y0)/(x1-x0);	
			double b0 = y0-a0*x0;
			double interY = a0*x2 + b0;
			return new Point2D.Double(x2,interY);
		}
		
		double a0 = (y1-y0)/(x1-x0);  //y=ax + b
		double a1 = (y3-y2)/(x3-x2);
		
		double b0 = y0-a0*x0;
		double b1 = y2-a1*x2;
		
		double interX = (b1-b0)/(a0-a1);
		double interY = a0*interX + b0;
		return new Point2D.Double(interX,interY);		
	}
	
	
	public int getInterType(Bone that) {
		if (Line2D.linesIntersect(this.getFirstPoint().getX(),
			this.getFirstPoint().getY(), 
			this.getSecondPoint().getX(),
			this.getSecondPoint().getY(),
			that.getFirstPoint().getX(),
			that.getFirstPoint().getY(), 
			that.getSecondPoint().getX(),
			that.getSecondPoint().getY())) {
			
			if(this.firstPoint.equals(that.firstPoint)||
					this.firstPoint.equals(that.secondPoint)||
					this.secondPoint.equals(that.firstPoint)||
					this.secondPoint.equals(that.secondPoint)) 
				return 1;
				
			return 2;
			
		}
		return 0;
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
	
	// returns random to avoid "overfit"; 
	public Joint nextFirstBone(double heading) {		
		int next = randomSeed.nextInt(firstConnections.size());
		return firstConnections.get(next);
	}
	
	public Joint nextSecondBone(double heading) {
		int next = randomSeed.nextInt(secondConnections.size());
		
		return secondConnections.get(next);
	}
	
	public Boolean hasNextFirst() {
		return firstConnections.size()!=0;
	}
	
	public Boolean hasNextSecond() {
		return secondConnections.size()!=0;
	}
	
}
