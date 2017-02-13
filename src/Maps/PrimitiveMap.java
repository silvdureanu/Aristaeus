package Maps;

import java.awt.Shape;

import Particles.Particle;

public interface PrimitiveMap {	

	public void setUpMap();
	public  Shape getWalls();	
	public  Shape getRealLocation();	
	public  Shape getParticles();
}
