package Maps;

import java.awt.Shape;

import core.Particle;

public interface Map {	
	
	public  boolean crossesWall(Particle p, double x,double y);
	public boolean outsideBounds(double x, double y);
	public  Shape getWalls();	
	public  Shape getRealLocation();	
	public  Shape getParticles();
	
}
