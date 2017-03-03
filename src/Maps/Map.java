package Maps;

import java.awt.Shape;

import Particles.Particle;

public interface Map extends PrimitiveMap {	
	
	public  boolean crossesWall(Particle p, double x,double y);
	public boolean outsideBounds(double x, double y);
	public void updateRealLocation();
	
}
